FROM --platform=linux/amd64 maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app
COPY pom.xml .
COPY lib-core/pom.xml lib-core/pom.xml
COPY product-service/pom.xml product-service/pom.xml
COPY inventory-service/pom.xml inventory-service/pom.xml
COPY lib-core/src lib-core/src
COPY inventory-service/src inventory-service/src
# Build only inventory-service and its dependencies
RUN mvn clean package -pl inventory-service -am -DskipTests

FROM --platform=linux/amd64 eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/inventory-service/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
