# Dockerfile
FROM openjdk:21-jdk-slim

RUN apt-get update && \
    apt-get install -y curl && \
    curl --version && \
    rm -rf /var/lib/apt/lists/*


# Point de montage pour l'application
VOLUME /tmp

# Copie du JAR dans le container
COPY target/book-flow-0.0.1-SNAPSHOT.jar app.jar

# Health check Docker natif
HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Commande de démarrage
ENTRYPOINT ["java","-jar","/app.jar"]