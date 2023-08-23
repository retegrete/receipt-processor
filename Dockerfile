FROM amazoncorretto:17-alpine-jdk

RUN apk add --no-cache maven

WORKDIR /app

COPY pom.xml .

RUN ["mvn", "clean", "install"]

COPY target/Receipt-Processor-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
