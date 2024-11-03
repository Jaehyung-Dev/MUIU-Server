# Dockerfile
FROM openjdk:17-jdk-slim

# 환경 설정
ENV APP_HOME=/app
WORKDIR $APP_HOME

# JAR 파일 복사
COPY build/libs/muiu-back.jar app.jar

# Nginx 설치
RUN apt-get update && apt-get install -y nginx && rm -rf /var/lib/apt/lists/*

# Nginx와 Supervisor 설정 복사
COPY nginx.conf /etc/nginx/nginx.conf
COPY supervisord.conf /etc/supervisord.conf

# Supervisord 설치
RUN apt-get update && apt-get install -y supervisor

# 애플리케이션 실행 포트 노출
EXPOSE 9090

# Supervisor를 통해 Spring Boot 및 Nginx 실행
CMD ["supervisord", "-c", "/etc/supervisord.conf"]
