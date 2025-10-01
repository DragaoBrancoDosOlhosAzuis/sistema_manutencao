# Imagem base com Java 21
FROM openjdk:21-jdk-slim

# Diretório de trabalho
WORKDIR /app

# Copiar o JAR (você precisa buildar localmente primeiro)
COPY target/sistema-manutencao-*.jar app.jar

# Expor a porta
EXPOSE 8080

# Comando para executar
CMD ["java", "-jar", "app.jar"]