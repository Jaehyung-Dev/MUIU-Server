ENV SPRING_APPLICATION_JSON='{"server.port":9090, "server.address":"0.0.0.0"}'

# Stage 1: Build the application
FROM openjdk:17-jdk-alpine AS build
WORKDIR /app
# JAR 파일이 Dockerfile과 같은 디렉토리에 있는 경우
COPY muiu-0.0.1-SNAPSHOT.jar /app/app.jar

# Stage 2: Nginx + Spring Boot
FROM nginx:alpine
COPY /root/nginx.conf /etc/nginx/nginx.conf
COPY --from=build /app/app.jar /app/app.jar
RUN apk add --no-cache openjdk17-jre supervisor
COPY supervisord.conf /etc/supervisord.conf
EXPOSE 9090 80 443
CMD ["/usr/bin/supervisord", "-c", "/etc/supervisord.conf"]

