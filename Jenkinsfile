pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                // Uruchomienie Maven: clean install
                sh 'mvn clean install'
            }
        }
<<<<<<< HEAD
    }
}
=======
stage('Run Bot') {
            steps {
                // Upewnij się, że ścieżka jest poprawna do pliku w target/
                sh 'java -jar target/dcbot-1.0-SNAPSHOT.jar'
            }
        }
    }
}
>>>>>>> e8d0ed14bdca27551b3d4bc2e790c870ca2998a3
