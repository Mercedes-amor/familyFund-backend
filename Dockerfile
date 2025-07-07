# Imagen base con Java 17
FROM openjdk:17

# Crear directorio para la app
WORKDIR /app

# Copiar el JAR al contenedor
COPY target/backend-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto por defecto de Spring Boot
EXPOSE 8080

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=8080"]
