FROM openjdk:17-jdk-slim
LABEL authors="Michal"

# Ustawiamy katalog roboczy w kontenerze
WORKDIR /app

# Kopiujemy zbudowany JAR do katalogu /app w kontenerze
COPY target/dcbot-1.0-SNAPSHOT.jar /app/dcbot.jar

# Jeśli masz plik config.properties, skopiuj go do kontenera
COPY src/main/resources/config.properties /app/config.properties

# Otwieramy port, jeśli jest potrzebny do komunikacji (przykład: port 8080)
# EXPOSE 8080

# Komenda do uruchomienia aplikacji
ENTRYPOINT ["java", "-jar", "dcbot.jar"]
