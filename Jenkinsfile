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
                sh 'echo deploy'
            }
        }
        stage('Deploy') {
            steps {
                sh 'echo deploy'
            }
        }
    }
}
