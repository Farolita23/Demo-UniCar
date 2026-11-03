# --- Etapa de compilacion ---
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
# Las dependencias se descargan en su propia capa: solo se repite si cambia el pom.xml
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests -B \
    && java -Djarmode=layertools -jar target/UniCarDemo-0.0.1-SNAPSHOT.jar extract --destination /app/layers

# --- Imagen final ---
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Capas ordenadas de menos a mas cambiantes: reconstruir tras tocar el codigo solo copia "application"
COPY --from=build /app/layers/dependencies/ ./
COPY --from=build /app/layers/spring-boot-loader/ ./
COPY --from=build /app/layers/snapshot-dependencies/ ./
COPY --from=build /app/layers/application/ ./
RUN mkdir -p /app/uploads
ENV UPLOAD_DIR=/app/uploads
# Heap proporcional a la memoria del contenedor. Con 1-2 vCPU la JVM elige SerialGC (menos hilos y memoria
# que G1); para mas CPU basta con definir JAVA_OPTS=-XX:+UseG1GC. Se pueden anadir flags con JAVA_OPTS.
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75 -XX:InitialRAMPercentage=25 -XX:+ExitOnOutOfMemoryError -Djava.security.egd=file:/dev/./urandom"
EXPOSE 8081
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS org.springframework.boot.loader.launch.JarLauncher"]
