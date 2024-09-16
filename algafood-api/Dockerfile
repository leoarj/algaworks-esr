FROM eclipse-temurin:17-jre

# Diretório padrão da aplicação
ARG APP_WORKDIR=/app

WORKDIR ${APP_WORKDIR}

# Recebido do plugin do Maven para Docker
ARG JAR_FILE

COPY target/${JAR_FILE} ${APP_WORKDIR}/api.jar

EXPOSE 8080

CMD [ "java", "-jar", "api.jar" ]