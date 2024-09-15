FROM eclipse-temurin:17-jre

# Diretório padrão da aplicação
ARG APP_WORKDIR=/app

WORKDIR ${APP_WORKDIR}

COPY target/*.jar ${APP_WORKDIR}/api.jar

EXPOSE 8080

CMD [ "java", "-jar", "api.jar" ]