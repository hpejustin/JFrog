pipeline {
    agent any 
    stages {
        stage('SCM') { 
            steps { 
                git("git://github.com/hpejustin/JFrog.git")
                sh 'echo scm finished successfully.'
            }
        }
        stage('UT') { 
            steps { 
                sh 'mvn clean test'
                sh 'echo ut test finished successfully.'
            }
        }
        stage('Sonar') { 
            steps { 
                sh 'echo sonar scan goes here...'
                sh 'sleep 3'
            }
        }
        stage('Package') { 
            steps { 
                sh 'mvn clean install'
                sh 'echo Package finished successfully.'
            }
        }
        stage('Image') {
            steps {
                sh 'docker build -t jfrog-cloud-demo:1.0 .'
            }
        }
        stage('Distribute') {
            steps {
                sh 'docker login -u hpejustin -p#1234abCD'
                sh 'docker tag jfrog-cloud-demo:1.0 hpejustin/jfrog-cloud-demo:1.0'
                sh 'docker push hpejustin/jfrog-cloud-demo:1.0'
                sh 'docker rmi jfrog-cloud-demo:1.0 hpejustin/jfrog-cloud-demo:1.0'
                sh 'docker logout'
            }
        }
        stage('Deploy') {
            steps {
                sh 'curl -X DELETE http://39.106.21.94:8080/api/v1/namespaces/default/services/jfrog-cloud-svc'
                sh 'curl -X DELETE http://39.106.21.94:8080/apis/extensions/v1beta1/namespaces/default/deployments/jfrog-cloud-app'
                sh 'sleep 10'
                sh 'curl -X POST http://39.106.21.94:8080/api/v1/namespaces/default/services -d@kube-svc.json -H "Content-Type: application/json"'
                sh 'curl -X POST http://39.106.21.94:8080/apis/extensions/v1beta1/namespaces/default/deployments -d@kube-app.json -H "Content-Type: application/json"'
                sh 'echo deploy finished successfully.'
            }
        }
    }
}
