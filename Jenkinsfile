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
        sh 'java -jar target/discordbot.jar'
    }
}
    }
}
