package com.portone.payment.client.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.portone.payment.client.PortOneTokenManager
import com.portone.payment.client.PortOneTokenService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestClient

@Configuration
class RestClientConfig(
    private val objectMapper: ObjectMapper,
    private val tokenManager: PortOneTokenManager,
    @Value("\${portone.api.imp-key}") private val impKey: String
) {

    @Bean
    fun portOneAuthInterceptor(tokenService: PortOneTokenService): PortOneAuthInterceptor {
        return PortOneAuthInterceptor(tokenManager, tokenService, impKey)
    }

    @Bean
    fun restClient(portOneAuthInterceptor: PortOneAuthInterceptor): RestClient {
        val messageConverter = MappingJackson2HttpMessageConverter()
        messageConverter.objectMapper = objectMapper
        messageConverter.supportedMediaTypes = listOf(MediaType.APPLICATION_JSON)

        return RestClient.builder()
            .requestInterceptor(portOneAuthInterceptor)
            .messageConverters { converters -> converters.add(messageConverter) }
            .build()
    }

    @Bean
    fun tokenRestClient(): RestClient {
        // 토큰 발급용 RestClient (인터셉터 없음)
        val messageConverter = MappingJackson2HttpMessageConverter()
        messageConverter.objectMapper = objectMapper
        messageConverter.supportedMediaTypes = listOf(MediaType.APPLICATION_JSON)

        return RestClient.builder()
            .messageConverters { converters -> converters.add(messageConverter) }
            .build()
    }
}
