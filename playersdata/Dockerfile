FROM openjdk:17-oracle

ARG JAR_FILE=target/*.jar

COPY ./jar/playersdata-0.0.1-SNAPSHOT.jar app.jar

COPY src/main/resources /app/resources

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"]
