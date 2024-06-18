#!/bin/sh

# customizable options here
export JAVA_OPTIONS="-XX:MaxRAMPercentage=75"

# Start the application
java $JAVA_OPTIONS -jar /app/app.jar \
          -Dspring.datasource.url=${SPRING_DATASOURCE_URL} \
          -Dspring.datasource.username=${SPRING_DATASOURCE_USERNAME} \
          -Dspring.datasource.password=${SPRING_DATASOURCE_PASSWORD}