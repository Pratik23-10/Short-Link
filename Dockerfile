FROM openjdk:28-ea-jdk
ADD target/short-url.jar short-url.jar
ENTRYPOINT ["java","-jar","/short-url.jar"]