FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY ./build/libs/*.jar chat-service.jar
ENTRYPOINT ["java", "-jar", "/chat-service.jar"]