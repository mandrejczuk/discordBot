pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                // Uruchomienie Maven: clean install
                sh 'mvn clean install'
            }
        }
    }
}
