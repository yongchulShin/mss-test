# Multi-stage build를 사용하여 최종 이미지 크기 최적화
FROM openjdk:11-jdk-slim AS builder

# 작업 디렉토리 설정
WORKDIR /app

# Gradle wrapper와 build 파일 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Gradle wrapper 실행 권한 부여
RUN chmod +x ./gradlew

# 의존성 다운로드 (캐시 레이어 분리)
RUN ./gradlew dependencies --no-daemon

# 소스 코드 복사
COPY src src

# 애플리케이션 빌드
RUN ./gradlew build -x test --no-daemon

# 실행 단계
FROM openjdk:11-jre-slim

# 메타데이터 설정
LABEL maintainer="musinsa-backend"
LABEL version="1.0.0"
LABEL description="Musinsa Backend API"

# 작업 디렉토리 설정
WORKDIR /app

# 빌드된 JAR 파일 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# 로그 디렉토리 생성
RUN mkdir -p logs

# 포트 노출
EXPOSE 8080

# 헬스체크 설정
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"] 