FROM amazoncorretto:17

WORKDIR /latteve_spring

COPY latteve_spring-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]