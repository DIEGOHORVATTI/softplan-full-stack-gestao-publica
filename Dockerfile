# Usando uma imagem do OpenJDK 17
FROM openjdk:17-jdk-slim

# Diretório de trabalho no contêiner
WORKDIR /app

# Copiar o wrapper do Maven e o pom.xml
COPY mvnw pom.xml ./

# Baixar as dependências do Maven
RUN ./mvnw dependency:go-offline

# Copiar o código fonte da aplicação
COPY src ./src

# Construir o aplicativo usando o Maven
RUN ./mvnw clean package -DskipTests

# Copiar o JAR gerado para o contêiner
COPY target/*.jar app.jar

# Expor a porta 8080 para a aplicação
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
