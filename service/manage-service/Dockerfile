FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY ./build/libs/*.jar manage-service.jar
ENTRYPOINT ["java", "-jar", "/manage-service.jar"]