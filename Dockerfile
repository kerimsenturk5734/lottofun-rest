FROM openjdk:17-jdk-slim AS build
WORKDIR /app

# Projedeki mvnw, pom.xml ve src klasörünü kopyala
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src ./src

# mvnw'ye çalıştırma izni ver (Linux için)
RUN chmod +x mvnw

# mvnw ile build yap, testleri atla
RUN ./mvnw clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app

COPY --from=build /app/target/lottofun-rest-1.0.0.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
