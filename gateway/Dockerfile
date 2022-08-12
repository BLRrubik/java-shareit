FROM openjdk:17
COPY target/*.jar gateway.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/gateway.jar"]