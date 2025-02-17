# 🏓 Ping Pong

## 📌 프로젝트 소개
이 프로젝트는 **Spring Boot 3.0**과 **JDK 17**을 이용하여 **RESTful API 기반의 탁구 게임 매칭 시스템**을 구현한 것입니다.  
유저들은 게임 방을 생성하고 참가할 수 있으며, 단식(1v1)과 복식(2v2) 모드를 지원합니다.  
또한, **Swagger 문서화 및 JPA 기반의 데이터 관리**가 적용되었습니다.

---

## 🚀 **기술 스택**
| 카테고리 | 기술 |
|----------|----------------|
| **Backend** | Spring Boot 3.0, Java 17 |
| **Build Tool** | Gradle 8.x |
| **Database** | H2 Database (in-memory), Spring Data JPA |
| **Persistence** | JPA (ddl-auto: create) |
| **API 문서화** | SpringDoc OpenAPI (Swagger) |
| **Test** | JUnit 5, Spring Boot Test |

---

## 📜 **프로젝트 요구사항**
이 프로젝트는 아래와 같은 요구사항을 준수하여 개발되었습니다.

✔ **JDK 17을 사용하여 구현**  
✔ **Request 및 Response 스펙이 과제 문서와 정확히 일치**  
✔ **Spring Boot 애플리케이션의 포트를 8080으로 설정**  
✔ **H2 Database 사용 & JPA 적용 (`ddl-auto: create`)**  
✔ **Soft-delete 또는 Hard-delete 방식으로 데이터 삭제 처리**

---

## 🏗 **프로젝트 구조 (아키텍처 설계)**
이 프로젝트는 **클린 아키텍처 원칙**을 기반으로 구성되었으며, **각 책임을 분리**하여 유지보수성을 높였습니다.

✔ **Controller - Service - Repository 계층 분리 (Layered Architecture 적용)**  
✔ **DTO(Data Transfer Object) 활용으로 Request/Response 분리**  
✔ **Soft-delete 또는 Hard-delete 적용 가능**  
✔ **유지보수를 고려하여 패키지를 도메인 단위로 구성**

---

## 📜 **API 명세**
### **Swagger 문서화 적용** ✅
API 문서는 `Swagger (SpringDoc OpenAPI)`를 이용하여 자동화되었습니다.

### 📌 **Swagger UI 접근 방법**
```http://localhost:8080/swagger-ui/index.html```
Swagger UI에서 모든 API를 테스트할 수 있습니다.

---

## 🛠 **프로젝트 실행 방법**
1️⃣ **GitHub에서 프로젝트 클론**
```bash
git clone https://github.com/sihyunjojo/ping-pong.git
cd ping-pong
```
2️⃣ **Gradle 빌드 & 실행**

```bash
./gradlew build
./gradlew bootRun
```

3️⃣ **Swagger 문서 확인**

실행 후: http://localhost:8080/swagger-ui/index.html

4️⃣ **H2 Database Console 접근**

```bash
http://localhost:8080/h2-console
```
✔ JDBC URL: jdbc:h2:mem:testdb  
✔ Username: sa  
✔ Password: (빈 값)  

## 📌 주요 API 목록

| 기능       | HTTP Method | 엔드포인트                      | 설명            |
|------------|------------|---------------------------------|-----------------|
| 헬스체크   | GET        | `/api/health`                   | 서버 상태 확인  |
| 유저 조회 | GET        | `/api/users`                    | 전체 유저 목록  |
| 방 생성   | POST       | `/api/rooms`                    | 새로운 게임 방 생성 |
| 방 참가   | POST       | `/api/rooms/{roomId}/join`      | 특정 방 참가    |
| 방 나가기 | POST       | `/api/rooms/{roomId}/leave`     | 방 나가기       |
| 게임 시작 | POST       | `/api/games/{roomId}/start`     | 게임 시작       |
| 팀 변경   | PATCH      | `/api/games/{roomId}/change-team` | 팀 변경        |

## ✅ 품질 관리 및 코드 스타일
이 프로젝트는 아래의 사항을 고려하여 작성되었습니다.

✔ Swagger 문서화 적용 ✅  
✔ 아키텍처 구조화 - Controller, Service, Repository 분리 ✅  
✔ 중복 코드 최소화, 가독성 고려한 코드 스타일 적용 ✅  
✔ 테스트 코드 작성 - JUnit 5, Spring Boot Test 활용 ✅  
✔ SOLID 원칙 준수 - 각 컴포넌트의 책임 분리 ✅  

## 🧪 테스트 코드
- Spring Boot Test (JUnit5) 기반의 단위 테스트 및 통합 테스트 작성

테스트 실행 방법:

```bash
./gradlew test
```
