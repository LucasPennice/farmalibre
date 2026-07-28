FROM maven:3.9.11-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
COPY src src

RUN mvn clean package -DskipTests

FROM tomcat:10.1-jdk21-temurin

RUN rm -rf /usr/local/tomcat/webapps/*

COPY --from=build \
    /app/target/farmalibre.war \
    /usr/local/tomcat/webapps/ROOT.war

# Configurar puerto dinámico para Railway ($PORT)
RUN sed -i 's/port="8080"/port="${http.port}"/' /usr/local/tomcat/conf/server.xml
ENV CATALINA_OPTS="-Dhttp.port=\${PORT:-8080}"

EXPOSE 8080

CMD ["catalina.sh", "run"]
