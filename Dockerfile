# Use official OpenJDK image as a base image
FROM gradle:8.6-jdk21-alpine as build

WORKDIR /workspace/app

COPY build.gradle .
COPY src src

# Run Gradle build
RUN gradle assemble

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /workspace/app/build/libs/app-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","-Dspring.profiles.active=production","/app/app.jar"]