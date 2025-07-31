FROM gradle:jdk21-alpine AS builder
WORKDIR /app
COPY . .
RUN ./gradlew --no-daemon clean build

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/account-balance-service*.jar /src/account-balance-service.jar

ENV SERVER_PORT=8080
EXPOSE 8080

CMD ["java", "-jar", "app.jar"]