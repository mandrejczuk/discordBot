pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                // Uruchomienie Maven: clean install
                sh 'mvn clean install'
            }
        }
stage('Run Bot') {
            steps {
                // Upewnij się, że ścieżka jest poprawna do pliku w target/
                sh 'java -jar target/dcbot-1.0-SNAPSHOT.jar'
            }
        }
    }
}
