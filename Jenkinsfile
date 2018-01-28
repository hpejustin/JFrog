pipeline {
    agent any 
    stages {
        stage('SCM') { 
            steps { 
                git([url: 'https://github.com/hpejustin/JFrog.git', branch: 'master'])
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
                def scannerHome = tool 'sonarClient';
                withSonarQubeEnv('sonar') {
                    sh "${scannerHome}/bin/sonar-runner"
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
                sh 'docker login docker-release-local.demo.jfrogchina.com -u admin -pjfrogchina'
                sh 'docker tag jfrog-cloud-demo:${BUILD_ID} docker-release-local.demo.jfrogchina.com/jfrog-cloud-demo:${BUILD_ID}'
                sh 'docker push docker-release-local.demo.jfrogchina.com/jfrog-cloud-demo:${BUILD_ID}'
                sh 'docker rmi jfrog-cloud-demo:${BUILD_ID} docker-release-local.demo.jfrogchina.com/jfrog-cloud-demo:${BUILD_ID}'
                sh 'docker logout docker-release-local.demo.jfrogchina.com'
            }
        }
        stage('Deploy') {
            steps {
                sh 'echo $(pwd)'
                sh 'sed -i "s/{tag}/${BUILD_ID}/g" kube-app.json'
                sh 'kubectl -s kube-master:8080 delete -f kube-svc.json'
                sh 'kubectl -s kube-master:8080 delete -f kube-app.json'
                sh 'sleep 10'
                sh 'kubectl -s kube-master:8080 create -f kube-svc.json'
                sh 'kubectl -s kube-master:8080 create -f kube-app.json'
                sh 'for i in {1..20}; do echo "waiting for app starting..."; sleep 1; done;'
                sh 'echo deploy finished successfully.'
            }
        }
    }
}
