node {
    stage('SCM') { 
        git([url: 'https://github.com/hpejustin/JFrog.git', branch: 'develop'])
        sh 'echo scm finished successfully.'
    }
    stage('UT') { 
        sh 'mvn clean test'
        sh 'echo ut test finished successfully.'
    }
    stage('Sonar') {  
        sh 'echo sonar scan goes here...'
        withSonarQubeEnv('sonar') {
            sh "/root/tools/sonar-runner/sonar-runner-2.4/bin/sonar-runner"
        }
        timeout(time: 1, unit: 'HOURS') { // Just in case something goes wrong, pipeline will be killed after a timeout
            def qg = waitForQualityGate() // Reuse taskId previously collected by withSonarQubeEnv
            if (qg.status != 'OK') {
                error "Pipeline aborted due to quality gate failure: ${qg.status}"
            }
        }
    }
    stage('Package') { 
        sh 'mvn clean install'
        sh 'echo Package finished successfully.'
    }
    stage('Image') {
        sh 'echo ${BUILD_ID}'
        sh 'docker build -t jfrog-cloud-demo:${BUILD_ID} .'
    }
    stage('Distribute') {
        sh 'docker login -u hpejustin -p#1234abCD'
        sh 'docker tag jfrog-cloud-demo:${BUILD_ID} hpejustin/jfrog-cloud-demo:${BUILD_ID}'
        sh 'docker push hpejustin/jfrog-cloud-demo:${BUILD_ID}'
        sh 'docker rmi jfrog-cloud-demo:${BUILD_ID} hpejustin/jfrog-cloud-demo:${BUILD_ID}'
        sh 'docker logout'
    }
    stage('Deploy') {
        sh 'echo $(pwd)'
        sh 'sed -i "s/{tag}/${BUILD_ID}/g" kube-app.json'
        sh 'curl -X DELETE http://39.106.21.94:8080/api/v1/namespaces/default/services/jfrog-cloud-svc'
        sh 'curl -X DELETE http://39.106.21.94:8080/apis/extensions/v1beta1/namespaces/default/deployments/jfrog-cloud-app-demo'
        sh 'sleep 10'
        sh 'curl -X POST http://39.106.21.94:8080/api/v1/namespaces/default/services -d@kube-svc.json -H "Content-Type: application/json"'
        sh 'curl -X POST http://39.106.21.94:8080/apis/extensions/v1beta1/namespaces/default/deployments -d@kube-app.json -H "Content-Type: application/json"'
        sh 'for i in {1..20}; do echo "waiting for app starting..."; sleep 1; done;'
        sh 'echo deploy finished successfully.'
    }
}

/*
pipeline {
    agent any 
    stages {
        stage('SCM') { 
            steps {
                git([url: 'https://github.com/hpejustin/JFrog.git', branch: 'develop'])
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
                withSonarQubeEnv('sonar') {
                    sh "/root/tools/sonar-runner/sonar-runner-2.4/bin/sonar-runner"
                }
                if (waitForQualityGate().status != 'OK') {
                    error "Pipeline aborted due to quality gate failure"
                }
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
                sh 'echo ${BUILD_ID}'
                sh 'docker build -t jfrog-cloud-demo:${BUILD_ID} .'
            }
        }
        stage('Distribute') {
            steps {
                sh 'docker login -u hpejustin -p#1234abCD'
                sh 'docker tag jfrog-cloud-demo:${BUILD_ID} hpejustin/jfrog-cloud-demo:${BUILD_ID}'
                sh 'docker push hpejustin/jfrog-cloud-demo:${BUILD_ID}'
                sh 'docker rmi jfrog-cloud-demo:${BUILD_ID} hpejustin/jfrog-cloud-demo:${BUILD_ID}'
                sh 'docker logout'
            }
        }
        stage('Deploy') {
            steps {
                sh 'echo $(pwd)'
                sh 'sed -i "s/{tag}/${BUILD_ID}/g" kube-app.json'
                sh 'curl -X DELETE http://39.106.21.94:8080/api/v1/namespaces/default/services/jfrog-cloud-svc'
                sh 'curl -X DELETE http://39.106.21.94:8080/apis/extensions/v1beta1/namespaces/default/deployments/jfrog-cloud-app-demo'
                sh 'sleep 10'
                sh 'curl -X POST http://39.106.21.94:8080/api/v1/namespaces/default/services -d@kube-svc.json -H "Content-Type: application/json"'
                sh 'curl -X POST http://39.106.21.94:8080/apis/extensions/v1beta1/namespaces/default/deployments -d@kube-app.json -H "Content-Type: application/json"'
                sh 'for i in {1..20}; do echo "waiting for app starting..."; sleep 1; done;'
                sh 'echo deploy finished successfully.'
            }
        }
    }
}
*/