FROM openjdk:8-jdk
WORKDIR /usr/src/myapp
COPY app.jar /usr/src/myapp/app.jar
ENV SERVICE_PORT=${PORT:-8080}
# EXPOSE 8080
CMD ["java", "-jar", "app.jar", "--server.port=${SERVICE_PORT}"]