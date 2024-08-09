FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY ./build/libs/*.jar admin-service.jar
ENTRYPOINT ["java", "-jar", "/admin-service.jar"]