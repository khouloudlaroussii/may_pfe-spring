FROM openjdk:17-jdk-slim
EXPOSE 8090
ADD target/spring-security-0.0.1-SNAPSHOT.jar docker-spring-boot.jar
ENTRYPOINT ["java","-jar","/docker-spring-boot.jar"]
