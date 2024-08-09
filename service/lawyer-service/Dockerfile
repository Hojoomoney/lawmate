FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY ./build/libs/*.jar lawyer-service.jar
ENTRYPOINT ["java", "-jar", "/lawyer-service.jar"]