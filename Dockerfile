FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/*.jar tofi.jar
ENTRYPOINT ["java","-jar","/tofi.jar"]
EXPOSE 8080