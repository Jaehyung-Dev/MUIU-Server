# Dockerfile 수정

# 베이스 이미지 설정
FROM openjdk:17-jdk-slim

# 작업 디렉터리 생성
WORKDIR /app

# JAR 파일 복사 (정확한 .jar 파일 이름 반영)
COPY build/libs/muiu-0.0.1-SNAPSHOT.jar app.jar

# Nginx 설치
RUN apt-get update && \
    apt-get install -y nginx && \
    rm -rf /var/lib/apt/lists/*

# Nginx 설정 파일 복사
COPY nginx.conf /etc/nginx/nginx.conf

# Supervisord 설정 파일 복사
COPY supervisord.conf /etc/supervisor/conf.d/supervisord.conf

# Supervisord 시작
CMD ["/usr/bin/supervisord"]
