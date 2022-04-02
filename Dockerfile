#FROM nginx:latest
#COPY ./scripts/default.conf /etc/nginx/conf.d/default.conf

FROM maven:3.8.4-jdk-11-slim AS build
RUN mkdir -p /home/app/src
RUN mkdir -p /home/uploads
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:11-jre-slim
RUN mkdir -p /usr/local/lib
COPY --from=build /home/app/target/vragu-1.0-SNAPSHOT.jar /usr/local/lib/vragu-1.0-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/usr/local/lib/vragu-1.0-SNAPSHOT.jar"]
