FROM eclipse-temurin:21.0.1_12-jre

LABEL maintainer="Andriy Kandzyuba"
LABEL email="andrey.kandzuba@gmail.com"
LABEL company="JobBeacon.ai"

ARG APP_GROUP=appgroup
ARG APP_USER=appuser
ARG APPLICATION_NAME=app
ARG APPLICATION_VERSION=0.0.1
ARG ACTIVE_PROFILE=default

ENV APPLICATION_PATH=${APPLICATION_NAME}-${APPLICATION_VERSION}.jar \
    LOG_PATH=/var/log \
    SERVER_PORT=8080 \
    SPRING_PROFILES_ACTIVE=${ACTIVE_PROFILE}

ENV OTEL_SERVICE_NAME=${APPLICATION_NAME} \
    OTEL_JAVAAGENT_ENABLED=false \
    OTEL_AGENT="-javaagent:opentelemetry-javaagent.jar"

WORKDIR /home/${APP_USER}

COPY target/*.jar ${APPLICATION_PATH}
COPY script/entrypoint.sh .
COPY script/opentelemetry-javaagent.jar .

RUN chmod +x ./entrypoint.sh
RUN chown 644 ./opentelemetry-javaagent.jar

EXPOSE ${SERVER_PORT}

RUN addgroup --system --gid 1002 ${APP_GROUP} && adduser --system --uid 1002 --gid 1002 ${APP_USER}
USER 1002

ENTRYPOINT ["./entrypoint.sh"]