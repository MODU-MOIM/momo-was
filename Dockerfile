FROM openjdk:17
ARG JAR_FILE=build/libs/momo-was-0.0.1-SNAPSHOT.jar

# `ARG` 변수 출력
RUN echo "JAR_FILE is set to: ${JAR_FILE}"

# 파일 확인
RUN ls -lh ${JAR_FILE}

# JAR 파일 복사
COPY ${JAR_FILE} app.jar

# 복사 후 확인
RUN ls -lh app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
