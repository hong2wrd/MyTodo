# My Todo

> JWT 인증, 사용자별 카테고리, 탈퇴 회원 정리 배치를 구현한 풀스택 Todo 서비스

My Todo는 회원별로 Todo와 카테고리를 관리하는 웹 애플리케이션입니다. Spring Security와 JWT를 이용한 Stateless 인증, Redis 기반 Refresh Token 관리, 탈퇴 회원을 정리하는 스케줄 배치를 구현했습니다.

## 주요 기능

### 회원 및 인증

- 회원가입 및 아이디 중복 확인
- BCrypt 비밀번호 암호화
- JWT 기반 로그인 및 로그아웃
- 회원 정보 조회·수정 및 비밀번호 변경
- Access Token 만료 시 자동 재발급
- Refresh Token의 HttpOnly 쿠키 전달 및 Redis 저장
- 회원 탈퇴 시 `retired` 상태로 전환하는 소프트 삭제

### Todo

- Todo 등록, 조회, 수정 및 삭제
- 완료 상태 전환
- 카테고리별 Todo 조회
- Todo의 카테고리 변경
- 인증된 회원을 조회 조건에 포함해 사용자별 데이터 접근 제한

### 카테고리

- 회원별 카테고리 조회 및 등록
- 카테고리 이름 수정 및 삭제
- 카테고리 삭제 시 해당 카테고리에 속한 Todo 함께 삭제

### 탈퇴 회원 정리 배치

- 매일 오전 2시(`Asia/Seoul`)에 기본 실행
- `retired=true`인 회원과 관련 Todo·카테고리를 순서대로 삭제
- AOP를 이용해 배치 실행 전후를 공통 처리
- 실행 상태, 처리 건수, 오류 메시지와 시작·종료 시간을 이력으로 저장
- Cron 표현식과 시간대를 환경 변수로 변경 가능

## 기술 스택

| 구분 | 기술 |
| --- | --- |
| Frontend | React 19, React Router 7, Axios, Vite 8 |
| Backend | Java 17, Spring Boot 4.1, Spring MVC |
| Security | Spring Security, JWT, BCrypt |
| Persistence | Spring Data JPA, MySQL, H2(Test) |
| Token Storage | Redis |
| Batch | Spring Scheduling, Spring AOP |
| Build | Gradle, npm |

## 시스템 구성

```text
React Client (localhost:5173)
          │
          │ REST API + Access Token
          ▼
Spring Boot API (localhost:8080)
          │
          ├── MySQL: 회원, Todo, 카테고리, 배치 이력
          └── Redis: Refresh Token

Scheduled Batch
          └── 탈퇴 회원 및 관련 데이터 정리
```

## 인증 흐름

1. 사용자가 아이디와 비밀번호로 로그인을 요청합니다.
2. Spring Security가 회원 정보와 비밀번호를 검증합니다.
3. 서버가 Access Token과 Refresh Token을 발급합니다.
4. Access Token은 `Authorization` 응답 헤더로 전달합니다.
5. Refresh Token은 HttpOnly 쿠키로 전달하고 Redis에도 저장합니다.
6. 프런트엔드는 Access Token을 이후 요청의 `Authorization` 헤더에 포함합니다.
7. `401 Unauthorized` 응답을 받으면 Axios 인터셉터가 `/auth/refresh`를 호출합니다.
8. 서버가 쿠키와 Redis의 Refresh Token을 비교해 새로운 Access Token을 발급합니다.
9. 프런트엔드는 새 토큰을 저장하고 실패했던 요청을 다시 실행합니다.
10. 재발급에도 실패하면 토큰을 제거하고 로그인 화면으로 이동합니다.

## 주요 구현 내용

### Stateless JWT 인증

서버 세션을 생성하지 않고 `JwtAuthenticationFilter`와 `JwtAuthorizationFilter`에서 로그인 및 요청 인증을 처리합니다. 회원가입, 로그인, 로그아웃, 토큰 재발급과 아이디 중복 확인을 제외한 API는 인증이 필요합니다.

### 사용자별 데이터 접근 제한

Todo와 카테고리를 조회하거나 변경할 때 현재 인증된 회원을 조회 조건에 포함합니다. 리소스 ID만으로 다른 회원의 데이터에 접근하지 못하도록 서비스와 저장소 계층에서 소유권을 확인합니다.

### Access Token 자동 재발급

Axios 응답 인터셉터가 `401` 응답을 감지하면 Refresh Token을 이용해 Access Token을 재발급합니다. 재발급에 성공하면 원래 요청을 다시 보내고, 실패하면 로그인 화면으로 이동합니다.

### 탈퇴와 물리 삭제 분리

회원 탈퇴 요청에서는 회원을 바로 삭제하지 않고 `retired` 상태만 변경합니다. 스케줄러가 탈퇴 회원을 조회해 관련 Todo와 카테고리를 먼저 삭제한 다음 회원 데이터를 삭제합니다.

### 배치 이력 공통화

`@BatchLogging`이 선언된 배치 메서드를 AOP로 감싸 실행 이력을 기록합니다. 배치 이력은 별도 트랜잭션으로 시작되며 `RUNNING`, `SUCCESS`, `FAILED` 상태와 처리 건수 또는 오류 메시지를 저장합니다.

### 공통 API 응답과 예외 처리

일반적인 API 응답은 다음 형식을 사용합니다.

```json
{
  "code": 1,
  "msg": "Todo가 조회되었습니다.",
  "data": {}
}
```

- `code`: 성공 `1`, 실패 `-1`
- `msg`: 응답 메시지
- `data`: 응답 데이터

`@RestControllerAdvice`에서 비즈니스 예외와 입력값 검증 오류를 공통 형식으로 변환합니다. 유효성 검증 오류는 필드별 메시지를 `data`에 담아 반환합니다.

## 프로젝트 구조

```text
side/
├── front/
│   ├── public/
│   └── src/
│       ├── components/       # UI 컴포넌트와 입력 폼
│       ├── hooks/            # API 및 인증 관련 로직
│       └── pages/            # 라우트 페이지
│
└── back/
    └── src/
        ├── main/java/side/todo/
        │   ├── aop/          # 배치 실행 이력 공통 처리
        │   ├── batch/        # 스케줄러
        │   ├── config/       # Security 및 Redis 설정
        │   ├── contoller/    # REST API 컨트롤러
        │   ├── domain/       # JPA 엔티티
        │   ├── dto/          # 요청 및 응답 DTO
        │   ├── exception/    # 전역 예외 처리
        │   ├── repository/   # JPA 및 Redis 저장소
        │   ├── security/     # JWT 인증·인가
        │   └── service/      # 비즈니스 및 배치 로직
        ├── main/resources/   # 애플리케이션 설정
        └── test/             # 컨트롤러 및 JWT 테스트
```

## 실행 방법

### 사전 준비

- JDK 17
- Node.js 20.19 이상 또는 22.12 이상
- MySQL 8
- Redis

MySQL 데이터베이스를 생성합니다.

```sql
CREATE DATABASE mytodo
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
```

### 환경 변수 설정

백엔드의 `application.yaml`에서 다음 환경 변수를 사용합니다.

| 환경 변수 | 필수 여부 | 설명 | 개발 예시 |
| --- | --- | --- | --- |
| `ACTIVE` | 필수 | 활성 Spring Profile | `dev` |
| `DB_NAME` | 필수 | MySQL 데이터베이스명 | `mytodo` |
| `DB_USERNAME` | 필수 | MySQL 사용자명 | `root` |
| `DB_PASSWORD` | 필수 | MySQL 비밀번호 | 로컬 설정값 |
| `REDIS_PASSWORD` | 필수 | Redis 비밀번호 | 비밀번호가 없다면 빈 문자열 |
| `JWT_SECRET` | 필수 | JWT 서명 키 | 충분히 긴 임의 문자열 |
| `COOKIE_SECURE` | 필수 | HTTPS 전용 쿠키 여부 | 로컬은 `false` |
| `MEMBER_BATCH_CRON` | 선택 | 탈퇴 회원 정리 주기 | `0 0 2 * * *` |
| `MEMBER_BATCH_ZONE` | 선택 | 배치 기준 시간대 | `Asia/Seoul` |

PowerShell 예시:

```powershell
$env:ACTIVE = "dev"
$env:DB_NAME = "mytodo"
$env:DB_USERNAME = "root"
$env:DB_PASSWORD = "your-mysql-password"
$env:REDIS_PASSWORD = "your-redis-password"
$env:JWT_SECRET = "replace-with-a-long-random-secret"
$env:COOKIE_SECURE = "false"
```

Redis에 비밀번호가 없다면 다음과 같이 설정합니다.

```powershell
$env:REDIS_PASSWORD = ""
```

`MEMBER_BATCH_CRON`과 `MEMBER_BATCH_ZONE`을 생략하면 각각 `0 0 2 * * *`, `Asia/Seoul`이 사용됩니다.

### 백엔드 실행

환경 변수를 등록한 동일한 터미널에서 실행합니다.

```powershell
cd back
.\gradlew.bat bootRun
```

백엔드는 기본적으로 `http://localhost:8080`에서 실행됩니다.

### 프런트엔드 실행

새 터미널에서 실행합니다.

```powershell
cd front
npm install
npm run dev
```

브라우저에서 [http://localhost:5173](http://localhost:5173)에 접속합니다. 프런트엔드 API 주소는 `http://localhost:8080`, 백엔드 CORS 허용 주소는 `http://localhost:5173`으로 설정되어 있습니다.

## 화면 경로

| 경로 | 설명 |
| --- | --- |
| `/` | Todo 목록 |
| `/login` | 로그인 |
| `/join` | 회원가입 |
| `/new` | Todo 등록 |
| `/edit/:todoId` | Todo 수정 |
| `/todoType` | 카테고리 관리 |
| `/myPage` | 회원 정보 및 비밀번호 변경 |

## 주요 API

| 구분 | Method | Endpoint | 설명 | 인증 |
| --- | --- | --- | --- | --- |
| 인증 | POST | `/login` | 로그인 | 불필요 |
| 인증 | POST | `/logout` | 로그아웃 | 불필요 |
| 인증 | POST | `/auth/refresh` | Access Token 재발급 | 불필요 |
| 회원 | POST | `/member` | 회원가입 | 불필요 |
| 회원 | GET | `/member/{memberId}/conflict` | 아이디 중복 확인 | 불필요 |
| 회원 | GET | `/member` | 내 정보 조회 | 필요 |
| 회원 | PUT | `/member` | 회원 정보 수정 | 필요 |
| 회원 | PATCH | `/member` | 비밀번호 변경 | 필요 |
| 회원 | DELETE | `/member` | 회원 탈퇴 | 필요 |
| Todo | POST | `/todo` | Todo 등록 | 필요 |
| Todo | PUT | `/todo` | Todo 수정 | 필요 |
| Todo | GET | `/todo/{todoId}` | Todo 단건 조회 | 필요 |
| Todo | PATCH | `/todo/{todoId}` | 완료 상태 전환 | 필요 |
| Todo | PATCH | `/todo/{todoId}/{todoTypeId}` | 카테고리 변경 | 필요 |
| Todo | DELETE | `/todo/{todoId}` | Todo 삭제 | 필요 |
| Todo | GET | `/todo/todoType/{todoTypeId}` | 카테고리별 목록 조회 | 필요 |
| 카테고리 | GET | `/todoType` | 카테고리 목록 조회 | 필요 |
| 카테고리 | POST | `/todoType` | 카테고리 등록 | 필요 |
| 카테고리 | PATCH | `/todoType` | 카테고리 수정 | 필요 |
| 카테고리 | DELETE | `/todoType/{todoTypeId}` | 카테고리 삭제 | 필요 |

## 테스트 및 검사

백엔드 테스트는 H2 인메모리 데이터베이스를 사용합니다.

```powershell
cd back
.\gradlew.bat test
```

프런트엔드 린트와 프로덕션 빌드를 실행합니다.

```powershell
cd front
npm run lint
npm run build
```

## 개선 계획

- Docker Compose를 이용한 MySQL·Redis 실행 환경 구성
- API 문서화를 위한 Swagger 적용
- 배치 로직 테스트 및 운영 모니터링 보강
- 컨트롤러 응답 타입 구체화
- 통합 테스트 및 테스트 커버리지 확대
- 배포 환경별 설정 분리
- 반응형 UI 개선
