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
        sh 'java -jar /var/jenkins_home/.m2/repository/mandrejczuk/dcbot/1.0-SNAPSHOT/dcbot-1.0-SNAPSHOT.jar'
    }
}
    }
}
