# ---------- BUILD STAGE ----------
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app
COPY pom.xml .
RUN mvn -q dependency:resolve

COPY src ./src
RUN mvn -q package -DskipTests

# ---------- RUN STAGE ----------
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/target/accountmanagement-1.0-SNAPSHOT.jar app.jar

EXPOSE 8080

ENV JWT_SECRET=53V3NPR41535T0L0RDJUR1CL0NGM4YHER31GNF0RH15RUL3W45PR0M153D
ENV DB_URL=jdbc:postgresql://db:5432/accountdb
ENV DB_USER=admin
ENV DB_PASSWORD=pass

ENTRYPOINT ["java", "-jar", "app.jar"]
