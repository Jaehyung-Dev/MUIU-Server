#FROM openjdk:17-jdk-alpine AS build
#
#WORKDIR /app
#
#ARG JAR_FILE=build/libs/muiu-0.0.1-SNAPSHOT.jar
#
#COPY ${JAR_FILE} /app/app.jar
#
## Stage 2: Nginx + Spring Boot
#FROM nginx:alpine
#
## Nginx 설정 파일 복사
#COPY nginx.conf /etc/nginx/nginx.conf
#
## Spring Boot JAR 파일 복사
#COPY --from=build /app/app.jar /app/app.jar
#
## Install OpenJDK for running Spring Boot and Supervisor for process management
#RUN apk add --no-cache openjdk17-jre supervisor
#
## Supervisor 설정 파일 복사
#COPY supervisord.conf /etc/supervisord.conf
#
## Spring Boot와 Nginx 포트 노출
#EXPOSE 9090 80 443
#
## Supervisor를 사용해 Nginx와 Spring Boot를 동시에 실행
#CMD ["/usr/bin/supervisord", "-c", "/etc/supervisord.conf"]
#
#ENV JAVA_OPTS="-Xmx256m -Xms128m"
#CMD ["java", "$JAVA_OPTS", "-jar", "/app/app.jar"]

# # Stage 1: Build Spring Boot JAR
# FROM openjdk:17-jdk-alpine AS build

# WORKDIR /app

# ARG JAR_FILE=build/libs/muiu-0.0.1-SNAPSHOT.jar

# COPY ${JAR_FILE} /app/app.jar

# # Stage 2: Nginx + Spring Boot with Supervisor
# FROM nginx:alpine

# # Nginx 설정 파일 복사
# COPY nginx.conf /etc/nginx/nginx.conf

# # Spring Boot JAR 파일 복사
# COPY --from=build /app/app.jar /app/app.jar

# # Install OpenJDK for running Spring Boot and Supervisor for process management
# RUN apk add --no-cache openjdk17-jre supervisor

# # Supervisor 설정 파일 복사
# COPY supervisord.conf /etc/supervisord.conf

# # Spring Boot와 Nginx 포트 노출
# EXPOSE 9090 80 443

# ## JAVA_OPTS 환경 변수 설정
# #ENV JAVA_OPTS="-Xmx256m -Xms128m"

# # Supervisor를 사용해 Nginx와 Spring Boot를 동시에 실행
# CMD ["/usr/bin/supervisord", "-c", "/etc/supervisord.conf"]


# Stage 1: Build the application
FROM openjdk:17-jdk-alpine AS build
WORKDIR /app
# JAR 파일이 Dockerfile과 같은 디렉토리에 있는 경우
COPY muiu-0.0.1-SNAPSHOT.jar /app/app.jar

# Stage 2: Nginx + Spring Boot
FROM nginx:alpine
COPY nginx.conf /etc/nginx/nginx.conf
COPY --from=build /app/app.jar /app/app.jar
RUN apk add --no-cache openjdk17-jre supervisor
COPY supervisord.conf /etc/supervisord.conf
EXPOSE 9090 80 443
CMD ["/usr/bin/supervisord", "-c", "/etc/supervisord.conf"]

