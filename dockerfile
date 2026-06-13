FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY build/libs/notificacoes-0.0.1-SNAPSHOT.jar  /app/notificacoes.jar

EXPOSE 8083

CMD ["java", "-jar", "/app/notificacoes.jar"]