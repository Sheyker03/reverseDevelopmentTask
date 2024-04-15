FROM gradle as builder
WORKDIR /src
COPY --chown=gradle:gradle . .
RUN gradle build

FROM openjdk:21
WORKDIR /app
COPY --from=builder ./src .

ENTRYPOINT ["java", "-jar", "/app/build/libs/testforreversedevelopment-0.0.1-SNAPSHOT.jar"]