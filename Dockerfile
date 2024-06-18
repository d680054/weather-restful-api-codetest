FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# create a non-root user: app(id=1001)
RUN addgroup -g 1001 app && adduser -u 1001 -G app -D app

# remove the apk tool
RUN apk --purge del apk-tools \
    && rm -rf /var/cache/apk/* \
    && unlink /usr/bin/wget \
    && chown app /app

# Copy the jar into the container
COPY --chown=1001 target/*.jar /app/app.jar

# Copy the start script into the container
COPY --chown=1001 scripts/start.sh /app
RUN chmod 0740 /app/start.sh

# Run as non-root user
USER 1001

EXPOSE 8080

CMD ["sh", "-c", "/app/start.sh"]