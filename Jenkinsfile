pipeline {
    agent any 
    stages {
        stage('SCM') { 
            steps { 
                git("git://github.com/hpejustin/JFrog.git")
                sh 'echo scm finished successfully.'
            }
        }
        stage('Build') { 
            steps { 
                sh 'mvn clean test install'
                sh 'echo Build finished successfully.'
            }
        }
        stage('Package') {
            steps {
                sh 'docker build -t jfrog-cloud-demo:1.0 .'
            }
        }
        stage('Deploy') {
            steps {
                sh 'echo deploy'
            }
        }
    }
}
