# ✅ My Todo

JWT 인증을 기반으로 사용자별 할 일과 카테고리를 관리하는 풀스택 Todo 애플리케이션입니다.

## Features

- 회원가입 및 로그인
- JWT Access/Refresh Token 인증
- 만료된 Access Token 자동 재발급
- Todo 생성·조회·수정·삭제
- Todo 완료 상태 관리
- 사용자별 카테고리 관리
- 카테고리별 Todo 조회 및 이동

## Tech Stack

| 영역 | 기술 |
| --- | --- |
| Frontend | React 19, React Router, Axios, Vite |
| Backend | Java 17, Spring Boot 4.1, Spring Security |
| Database | MySQL, Redis |
| Authentication | JWT |
| ORM / Build | Spring Data JPA, Gradle |

## Architecture

```text
React Client
     │
     │ REST API + JWT
     ▼
Spring Boot Server
     │
     ├── MySQL ── 회원, Todo, 카테고리
     │
     └── Redis ── Refresh Token
```

## Authentication Flow

1. 사용자가 아이디와 비밀번호로 로그인합니다.
2. 서버가 Access Token과 Refresh Token을 발급합니다.
3. Access Token은 API 요청의 `Authorization` 헤더에 포함됩니다.
4. Refresh Token은 HttpOnly 쿠키와 Redis에 저장됩니다.
5. Access Token 만료 시 Refresh Token으로 자동 재발급합니다.

## Getting Started

### Requirements

- JDK 17
- Node.js 20.19 이상 또는 22.12 이상
- MySQL 8
- Redis

### Backend

다음 환경 변수를 설정합니다.

```properties
ACTIVE=dev
DB_NAME=mytodo
DB_USERNAME=root
DB_PASSWORD=your-password
REDIS_PASSWORD=your-password
JWT_SECRET=your-long-random-secret
COOKIE_SECURE=false
```

백엔드를 실행합니다.

```powershell
cd back
.\gradlew.bat bootRun
```

### Frontend

```bash
cd front
npm install
npm run dev
```

브라우저에서 [http://localhost:5173](http://localhost:5173)에 접속합니다.

## Routes

| 경로 | 설명 |
| --- | --- |
| `/` | Todo 목록 |
| `/login` | 로그인 |
| `/join` | 회원가입 |
| `/new` | Todo 등록 |
| `/edit/:todoId` | Todo 수정 |
| `/todoType` | 카테고리 관리 |

## Tests

```powershell
cd back
.\gradlew.bat test
```

```bash
cd front
npm run lint
npm run build
```
