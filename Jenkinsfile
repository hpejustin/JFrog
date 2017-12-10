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
                sh 'curl -X DELETE http://39.106.21.94:8080/api/v1/namespaces/default/services/jfrog-cloud-svc'
                sh 'curl -X DELETE http://39.106.21.94:8080/apis/extensions/v1beta1/namespaces/default/deployments/jfrog-cloud-app'
                sh 'sleep 10'
                sh 'curl -X POST http://39.106.21.94:8080/api/v1/namespaces/default/services -d@kube-svc.json -H "Content-Type: application/json"'
                sh 'curl -X POST http://39.106.21.94:8080/apis/extensions/v1beta1/namespaces/default/deployments -d@kube-app.json -H "Content-Type: application/json"'
                sh 'echo deploy'
            }
        }
    }
}
