# 포트원(PortOne) 결제 API 연동 프론트엔드

이 프로젝트는 포트원(PortOne) 결제 API를 연동한 백엔드 서비스와 통합하여 결제 기능을 제공하는 프론트엔드 애플리케이션입니다.

## 주요 기능

- **결제 요청**: 포트원 결제 위젯을 통한 결제 요청 처리
- **결제 정보 조회**: 결제 내역 및 상세 정보 조회
- **결제 취소**: 사용자 요청에 따른 결제 취소 기능
- **결제 상태 관리**: 결제 진행 상태 실시간 업데이트

## 기술 스택

- **Next.js**: React 기반의 서버 사이드 렌더링 프레임워크
- **TypeScript**: 타입 안전성을 제공하는 JavaScript 확장 언어
- **Tailwind CSS**: 유틸리티 기반 CSS 프레임워크
- **Axios**: HTTP 클라이언트 라이브러리
- **ESLint/Prettier**: 코드 품질 및 스타일 관리 도구

## 프로젝트 구조

```
Fe/
├── public/          # 정적 파일
├── src/
│   ├── app/         # 페이지 및 라우팅
│   └── components/  # 재사용 가능한 컴포넌트
├── .env             # 환경 변수 (gitignore에 포함됨)
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

