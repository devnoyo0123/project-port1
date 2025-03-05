package com.portone.payment.aop

import mu.KotlinLogging
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Component
import kotlin.time.measureTimedValue

private val logger = KotlinLogging.logger {}

@Aspect
@Component
class LoggingAspect {
    // Controller로 끝나는 클래스의 모든 public 메서드
    @Around("execution(public * com.portone.payment.api..*Controller.*(..))")
    fun logController(joinPoint: ProceedingJoinPoint): Any? {
        return logMethod(joinPoint, "Controller")
    }

    // Service로 끝나는 클래스의 모든 public 메서드
    @Around("execution(public * com.portone.payment.service..*Service.*(..))")
    fun logService(joinPoint: ProceedingJoinPoint): Any? {
        return logMethod(joinPoint, "Service")
    }

    private fun logMethod(joinPoint: ProceedingJoinPoint, type: String): Any? {
        val signature = joinPoint.signature as MethodSignature
        val methodName = "${signature.declaringType.simpleName}.${signature.name}"
        val params = signature.parameterNames.zip(joinPoint.args).toMap()

        logger.info { "→ [$type] $methodName 시작 | 파라미터: $params" }

        return try {
            val (result, duration) = measureTimedValue {
                joinPoint.proceed()
            }
            logger.info { "← [$type] $methodName 종료 | 소요시간: ${duration.inWholeMilliseconds}ms" }
            result
        } catch (e: Exception) {
            logger.error(e) { "× [$type] $methodName 실패 | 에러: ${e.message}" }
            throw e
        }
    }
}
