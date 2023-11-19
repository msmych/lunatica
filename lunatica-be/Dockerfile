FROM eclipse-temurin:17-jdk as builder

WORKDIR /app

COPY gradlew settings.gradle.kts build.gradle.kts /app/

COPY gradle /app/gradle
COPY app /app/app
COPY complaints /app/complaints
COPY persistence /app/persistence
COPY persistence-pg /app/persistence-pg

RUN ./gradlew app:shadowJar

FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=builder /app/app/build/libs/app-all.jar /app/app-all.jar
COPY wait-for-it.sh /app/
RUN chmod +x /app/wait-for-it.sh

EXPOSE 8080

CMD ["java", "-jar", "app-all.jar"]
