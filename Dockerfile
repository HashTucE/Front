FROM openjdk:19-alpine
COPY target/front-0.0.1-SNAPSHOT.jar front-0.0.1-SNAPSHOT.jar
EXPOSE 8083
ENTRYPOINT ["java","-jar","/front-0.0.1-SNAPSHOT.jar"]
