#!/bin/bash

if [[ -n ${SERVER_PORT} ]]; then
  JAVA_OPTS="-Dserver.port=${SERVER_PORT} ${JAVA_OPTS}"
fi

if [[ -n ${SPRING_PROFILES_ACTIVE} ]]; then
  JAVA_OPTS="-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} ${JAVA_OPTS}"
fi

if [[ -n ${OTEL_AGENT} ]]; then
  JAVA_OPTS="${OTEL_AGENT} ${JAVA_OPTS}"
fi

if [[ -n ${APPLICATION_PATH} ]]; then
  echo "java ${JAVA_OPTS} -jar ${APPLICATION_PATH}"
  pwd
  id
  java ${JAVA_OPTS} -jar "${APPLICATION_PATH}"
else
  echo "ERROR: APPLICATION_PATH is not set. Exiting."
  exit  1
fi