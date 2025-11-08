ARG JAVA_VERSION=21

FROM maven:3.9.6-eclipse-temurin-21 AS build

ARG SERVICE_NAME
WORKDIR /app

COPY pom.xml .
COPY services/auth/pom.xml ./services/auth/
COPY services/basket/pom.xml ./services/basket/
COPY services/config-server/pom.xml ./services/config-server/
COPY services/customer/pom.xml ./services/customer/
COPY services/discovery/pom.xml ./services/discovery/
COPY services/gateway/pom.xml ./services/gateway/
COPY services/notification/pom.xml ./services/notification/
COPY services/order/pom.xml ./services/order/
COPY services/payment/pom.xml ./services/payment/
COPY services/product/pom.xml ./services/product/
COPY services/search/pom.xml ./services/search/

RUN mvn -B dependency:go-offline
COPY services/ ./services/
RUN mvn -B -pl services/${SERVICE_NAME} -am clean package -DskipTests


FROM openjdk:26-ea-${JAVA_VERSION}-slim

WORKDIR /app

ARG SERVICE_NAME
ARG SERVICE_PORT

COPY --from=build /app/services/${SERVICE_NAME}/target/*.jar app.jar

EXPOSE ${SERVICE_PORT}

ENTRYPOINT ["java", "-jar", "app.jar"]