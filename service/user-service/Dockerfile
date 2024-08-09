FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY ./build/libs/*.jar user-service.jar
ENTRYPOINT ["java", "-jar", "/user-service.jar"]