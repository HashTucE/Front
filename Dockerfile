FROM openjdk:19-alpine AS mediscreen_front
COPY target/front-0.0.1-SNAPSHOT.jar front-0.0.1-SNAPSHOT.jar
EXPOSE 8083
ENTRYPOINT ["java","-jar","/front-0.0.1-SNAPSHOT.jar"]
