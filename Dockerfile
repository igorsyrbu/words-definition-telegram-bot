FROM openjdk:8-jdk-alpine
ARG JAR_FILE
VOLUME /tmp
COPY target/english-words-definition-bot.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]