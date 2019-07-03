FROM openjdk:8-jdk
WORKDIR /usr/src/myapp
COPY app.jar /usr/src/myapp/app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]