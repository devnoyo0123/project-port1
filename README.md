# project-port1

# BE Readme

# 포트원(PortOne) 결제 API 연동 서비스

이 프로젝트는 포트원(PortOne) 결제 API를 연동하여 결제 정보 조회, 결제 취소 등의 기능을 제공하는 서비스입니다.

## 주요 기능

- **결제 정보 조회**: impUid를 이용한 단건 결제 정보 조회
- **결제 취소**: 결제 취소 처리
- **토큰 인증 관리**: 포트원 API 사용을 위한 인증 토큰 자동 관리

## 구현 사항

### 인증 토큰 관리

- **자동 토큰 발급**: HTTP 요청 인터셉터에서 인증 토큰을 자동으로 발급하고 관리합니다.
- **캐시 활용**: 인증 토큰을 캐시에 저장하여 반복적인 토큰 발급 요청을 방지합니다.
- **토큰 만료 관리**: 토큰 만료 시간을 확인하여 자동으로 갱신합니다.
- **사용자별 토큰 관리**: 여러 사용자(imp-key)에 대해 각각 독립적으로 토큰을 관리합니다.

### 오류 처리 및 재시도 로직

- **지수 백오프 재시도**: API 호출 실패 시 지수 백오프(Exponential Backoff) 전략을 사용하여 재시도합니다.
- **인증 오류 자동 복구**: 401 Unauthorized 오류 발생 시 토큰을 자동으로 갱신하고 요청을 재시도합니다.
- **최대 재시도 횟수 제한**: 무한 루프 방지를 위해 최대 재시도 횟수를 제한합니다.

### 로깅 및 모니터링

- **LoggingAspect**: AOP를 활용하여 API 호출 로깅을 구현했습니다.
- **상세 로그**: 요청/응답 정보, 오류 정보, 재시도 정보 등을 상세히 기록합니다.

### 클라이언트 구성

- **전용 API 클라이언트**: 포트원 API 호출을 위한 전용 클라이언트를 구성했습니다.
- **인터셉터 적용**: 모든 API 요청에 인증 토큰을 자동으로 적용합니다.
- **타입 안전성**: 응답 데이터를 위한 타입 안전한 모델 클래스를 제공합니다.

## 실행 방법

### cli에서 실행
환경 변수를 설정하여 애플리케이션을 실행합니다:

```bash
PORTONE_IMP_KEY="입력해주세요" PORTONE_IMP_SECRET="입력해주세요" ./gradlew bootRun
```

### IDE에서 실행 (IntelliJ IDEA)

1. **EnvFile 플러그인 설치**:
- IntelliJ IDEA의 Plugins 메뉴에서 "EnvFile" 플러그인을 검색하여 설치합니다.
- 플러그인 설치 후 IDE를 재시작합니다.

2. **.env 파일 생성**:
- 프로젝트 루트 디렉토리에 `.env` 파일을 생성합니다.
- 다음과 같이 필요한 환경 변수를 설정합니다:
  ```
  PORTONE_IMP_KEY=입력해주세요
  PORTONE_IMP_SECRET=입력해주세요
  ```
- `.gitignore` 파일에 `.env`를 추가하여 민감한 정보가 저장소에 커밋되지 않도록 합니다.

3. **Run Configuration 설정**:
- Run/Debug Configurations 메뉴를 엽니다.
- 애플리케이션 실행 구성을 선택합니다.
- "EnvFile" 탭을 선택합니다.
- "Enable EnvFile" 체크박스를 선택합니다.
- "+" 버튼을 클릭하여 `.env` 파일을 추가합니다.
- "Apply" 및 "OK" 버튼을 클릭하여 설정을 저장합니다.

4. **애플리케이션 실행**:
- 일반적인 방법으로 애플리케이션을 실행합니다.
- 환경 변수가 `.env` 파일에서 자동으로 로드됩니다.

## API 엔드포인트

### 결제 정보 조회
GET /api/payments/{impUid}

- `impUid`: 포트원에서 발급한 결제 고유 ID
-
## API 문서

Swagger UI를 통해 API 문서를 확인하고 테스트할 수 있습니다:
http://localhost:8080/swagger-ui.html

## 기술 스택

- Kotlin
- Spring Boot
- Spring Cache (caffeine)
- Spring AOP
- Spring RestClient
- Springdoc OpenAPI (Swagger)

