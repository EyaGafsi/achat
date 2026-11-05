
pipeline {
    agent any

    options {
        skipDefaultCheckout(true)
        timestamps()
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Recuperation du projet depuis GitHub'
                checkout scm
            }
        }

        stage('Verify Environment') {
            steps {
                echo 'Verification de Java'
                sh 'java -version'
                sh 'chmod +x mvnw'
                sh './mvnw -version'
            }
        }

        stage('Build and Test') {
            steps {
                echo 'Compilation et execution des tests'
                sh './mvnw -B clean verify'
            }
        }

        stage('Archive') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar',
                                 allowEmptyArchive: true
            }
        }
    }

    post {
        always {
            junit testResults: 'target/surefire-reports/*.xml',
                  allowEmptyResults: true
        }

        success {
            echo 'BUILD SUCCESSFUL'
        }

        failure {
            echo 'BUILD FAILED - Consultez la console'
        }
    }
}
