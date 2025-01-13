FROM openjdk:17

COPY build/libs/momo-was-0.0.1-SNAPSHOT.jar /app.jar

RUN echo "Checking /app.jar..." && ls -lh /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]

