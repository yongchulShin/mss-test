# Musinsa Backend Engineer 과제

## 구현 범위

### 1. 가격 조회 API
- **카테고리별 최저가 조회**: 각 카테고리에서 최저가 브랜드와 상품 정보, 총액 조회
- **브랜드별 전체 카테고리 최저가 조회**: 단일 브랜드로 모든 카테고리 상품 구매 시 최저가격 브랜드 조회
- **카테고리별 최저/최고가 조회**: 특정 카테고리의 최저가/최고가 브랜드와 상품 가격 조회

### 2. CRUD API
- **브랜드 관리**: 브랜드 생성, 수정, 삭제 API
- **상품 관리**: 상품 생성, 수정, 삭제 API
- **비즈니스 규칙**: 모든 브랜드는 각 카테고리에 정확히 1개의 상품만 등록 가능

### 3. 예외 처리
- **일관된 JSON 응답**: API 실패 시 실패값과 실패 사유를 일관된 JSON 형식으로 반환
- **공통 예외 처리**: `@RestControllerAdvice`를 통한 전역 예외 처리

### 4. 기술 스택
- **데이터베이스**: H2 인메모리 DB
- **ORM**: JPA/Hibernate
- **프레임워크**: Spring Boot 2.7.x
- **언어**: Java 11

## 코드 빌드, 테스트, 실행 방법

### 1. 환경 설정
```bash
# Java 11 설치 확인
java -version

# Gradle 설치 확인 (또는 Gradle Wrapper 사용)
./gradlew --version
```

### 2. 프로젝트 빌드
```bash
# 프로젝트 클린 빌드
./gradlew clean build

# 테스트 제외하고 빌드
./gradlew clean build -x test
```

### 3. 애플리케이션 실행
```bash
# Spring Boot 애플리케이션 실행
./gradlew bootRun

# 또는 빌드된 JAR 파일 실행
java -jar build/libs/mss-test-0.0.1-SNAPSHOT.jar
```

### 4. 테스트 실행
```bash
# 전체 테스트 실행 (단위 + 통합)
./gradlew test

# 단위 테스트만 실행
./gradlew test --tests ProductControllerTest

# 통합 테스트만 실행
./gradlew test --tests MusinsaApplicationTests

# 테스트 커버리지 확인
./gradlew test jacocoTestReport
```

### 5. H2 데이터베이스 콘솔 접속
- **URL**: http://localhost:8080/h2-console
- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: (비워둠)

### 6. Swagger API 문서
- **URL**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/api-docs

### 7. Spring Boot Actuator
- **Health Check**: http://localhost:8080/actuator/health
- **Info**: http://localhost:8080/actuator/info
- **Metrics**: http://localhost:8080/actuator/metrics

#### Swagger 응답 예시 설정법
```java
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "조회 성공",
        content = @Content(mediaType = "application/json",
            examples = @ExampleObject(
                name = "성공 예시",
                value = "{\n" +
                        "  \"카테고리\": [\n" +
                        "    {\n" +
                        "      \"카테고리\": \"상의\",\n" +
                        "      \"최저가\": {\n" +
                        "        \"브랜드\": \"Nike\",\n" +
                        "        \"가격\": 10000\n" +
                        "      }\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"총액\": 10000\n" +
                        "}"
            )
        )
    ),
    @ApiResponse(responseCode = "400", description = "조회 실패",
        content = @Content(mediaType = "application/json",
            examples = @ExampleObject(
                name = "실패 예시",
                value = "{\n" +
                        "  \"success\": false,\n" +
                        "  \"message\": \"에러 메시지\"\n" +
                        "}"
            )
        )
    )
})
```

### 8. API 테스트
```bash
# 카테고리별 최저가 조회
curl -X GET http://localhost:8080/product/summary/lowest

# 브랜드별 전체 카테고리 최저가 조회
curl -X GET http://localhost:8080/product/summary/brand-lowest

# 카테고리별 최저/최고가 조회
curl -X GET http://localhost:8080/product/summary/category/상의

# 브랜드 생성
curl -X POST http://localhost:8080/brand \
  -H "Content-Type: application/json" \
  -d '{"name": "새로운브랜드"}'

# 브랜드 수정
curl -X PUT http://localhost:8080/brand/1 \
  -H "Content-Type: application/json" \
  -d '{"name": "수정된브랜드"}'

# 브랜드 삭제
curl -X DELETE http://localhost:8080/brand/1
```

## 기타 추가 정보

### 1. 도메인별 패키지 구조

프로젝트는 **도메인 주도 설계(DDD)** 원칙을 적용하여 도메인별로 패키지를 구성했습니다.

```
src/main/java/com/musinsa/
├── MusinsaApplication.java
├── brand/           # 브랜드 도메인
│   ├── controller/  # 브랜드 관련 API 컨트롤러
│   ├── dto/         # 브랜드 데이터 전송 객체
│   ├── model/       # 브랜드 엔티티
│   └── repository/  # 브랜드 데이터 접근 계층
├── category/        # 카테고리 도메인
│   ├── model/       # 카테고리 엔티티
│   └── repository/  # 카테고리 데이터 접근 계층
├── common/          # 공통 기능
│   └── exception/   # 공통 예외 처리
└── product/         # 상품 도메인
    ├── controller/  # 상품 관련 API 컨트롤러
    ├── dto/         # 상품 데이터 전송 객체
    ├── model/       # 상품 엔티티
    ├── repository/  # 상품 데이터 접근 계층
    └── service/     # 상품 비즈니스 로직
```

#### 도메인별 패키지 구조의 장점:
- **응집도 향상**: 관련된 기능들이 한 패키지에 모여있어 코드 이해도 증가
- **결합도 감소**: 도메인 간 의존성을 명확히 분리
- **확장성**: 새로운 도메인 추가 시 독립적인 패키지로 구성 가능
- **유지보수성**: 도메인별로 독립적인 개발 및 수정 가능

### 2. 공통 예외 처리

`GlobalExceptionHandler`를 통해 모든 API에서 일관된 예외 응답을 제공합니다. 예외 발생 시 `success: false`와 함께 구체적인 에러 메시지를 반환합니다.

#### 예외 응답 형식:
```json
{
  "success": false,
  "message": "브랜드명이 중복됩니다: Nike"
}
```

#### 처리되는 예외 유형:
- **ApiException (400 Bad Request)**: 브랜드명 중복, 상품 제약 조건 위반 등 비즈니스 예외
- **Exception (500 Internal Server Error)**: 데이터베이스 오류, 내부 서버 오류 등 시스템 예외

### 3. 주요 비즈니스 규칙

#### 브랜드-카테고리 제약 조건:
- 각 브랜드는 모든 카테고리에 정확히 1개의 상품만 등록 가능
- 브랜드명은 유일해야 함
- 상품 가격은 양수여야 함

#### 검증 로직:
```java
// 브랜드별 카테고리당 1개 상품 제약 조건 검증
private void validateBrandCategoryConstraint(Brand brand, Category category) {
    if (productRepository.existsByBrandAndCategory(brand, category)) {
        throw new ApiException("이미 해당 브랜드의 카테고리 상품이 존재합니다");
    }
}
```

### 4. 테스트 전략

#### 단위 테스트 (ProductControllerTest):
- **컨트롤러 테스트**: MockMvc를 사용한 HTTP 요청/응답 검증
- **CRUD 테스트**: 브랜드 생성, 수정, 삭제 API 검증
- **예외 처리 테스트**: 다양한 예외 상황에 대한 응답 검증
- **조회 API 테스트**: 카테고리별 최저가, 브랜드별 최저가 등 조회 기능 검증

#### 통합 테스트 (MusinsaApplicationTests):
- **전체 애플리케이션 테스트**: 실제 데이터베이스 연동 검증
- **API 엔드포인트 테스트**: 브랜드 생성→조회→수정→삭제 전체 플로우 검증
- **실제 DB 연동**: H2 인메모리 DB와의 실제 연동 테스트

### 5. Docker 실행
```bash
# Docker 이미지 빌드
docker build -t musinsa-backend .

# Docker 컨테이너 실행
docker run -p 8080:8080 musinsa-backend

# Docker Compose 실행 (docker-compose.yml이 있는 경우)
docker-compose up -d
```

### 6. 로깅
- **로그 파일**: `logs/musinsa.log`
- **로그 레벨**: INFO (개발 환경: DEBUG)
- **로그 포맷**: `yyyy-MM-dd HH:mm:ss.SSS [thread] LEVEL logger - message`
- **로그 롤링**: 일별, 10MB 단위, 30일 보관

### 7. 모니터링
- **Health Check**: Actuator를 통한 애플리케이션 상태 모니터링
- **Metrics**: JVM, HTTP 요청 등 다양한 메트릭 수집

### 8. Frontend 실행
```bash
# Frontend 의존성 설치
cd frontend
npm install

# Frontend 개발 서버 실행
npm start
```

### 9. 향후 개선 방향

- **캐싱**: Redis를 활용한 성능 최적화
- **모니터링**: Grafana를 통한 대시보드 구축
- **보안**: Spring Security를 통한 인증/인가 구현
- **API 버전 관리**: API 버전별 관리 체계 구축
- **CI/CD**: GitHub Actions를 통한 자동 배포 파이프라인 구축
- **Frontend 개선**: 차트 라이브러리 추가, 실시간 업데이트

## CORS 설정 (프론트엔드와 통신)

Spring Boot 백엔드에서 프론트엔드(React 등)와 통신하기 위해 CORS(Cross-Origin Resource Sharing) 설정이 필요합니다.

### 예시: 전체 도메인 허용 (개발용)
```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000") // 프론트엔드 주소
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
```

> 운영 환경에서는 `allowedOrigins`에 실제 프론트엔드 도메인만 허용하세요.

---

## Frontend 구조

- frontend는 **대시보드(Dashboard)**와 **상품분석(ProductAnalysis)** 페이지로 구성되어 있습니다.
    - `src/pages/Dashboard.js`: 대시보드 페이지
    - `src/pages/ProductAnalysis.js`: 상품 분석 페이지

- 자세한 내용은 [Frontend.md](frontend%2FFrontend.md) 참고
---

