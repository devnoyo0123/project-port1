# 포트원(PortOne) 결제 API 연동 프론트엔드

이 프로젝트는 포트원(PortOne) 결제 API를 연동한 백엔드 서비스와 통합하여 결제 기능을 제공하는 프론트엔드 애플리케이션입니다.

## 주요 기능

- **결제 요청**: 포트원 결제 SDK을 통한 결제 요청 처리

## 기술 스택

- **Next.js**
- **TypeScript**
- **Tailwind CSS**
- **Axios**
- **ESLint/Prettier**

## 프로젝트 구조

```
Fe/
├── public/          # 정적 파일
├── src/
│   ├── app/         # 페이지 및 라우팅
│   └── components/  # 재사용 가능한 컴포넌트
├── .env             # 환경 변수 (gitignore에 포함됨)
├── .env-example     # 환경 변수 예시 파일
└── ...              # 기타 설정 파일
```

## 설치 및 실행

### 필수 조건

- Node.js 18.0.0 이상
- npm 또는 yarn

### 설치

```bash
# 의존성 설치
npm install
# 또는
yarn install
```

### 환경 변수 설정

`.env-example` 파일을 참고하여 `.env` 파일을 프로젝트 루트에 생성하고 다음 변수를 설정합니다:

```
NEXT_PUBLIC_PORTONE_MERCHANT_UID="입력해주세요"
NEXT_PUBLIC_PORTONE_CHANNEL_KEY="입력해주세요"
```

**중요**: 
- 포트원 SDK를 사용하기 위해서는 반드시 위 환경 변수들을 올바르게 설정해야 합니다. 환경 변수가 설정되지 않으면 결제 기능이 작동하지 않습니다.

### 개발 서버 실행

```bash
npm run dev
# 또는
yarn dev
```

개발 서버는 기본적으로 http://localhost:3000 에서 실행됩니다.

## 배포

```bash
# 프로덕션 빌드
npm run build
# 또는
yarn build

# 프로덕션 서버 실행
npm run start
# 또는
yarn start
```

