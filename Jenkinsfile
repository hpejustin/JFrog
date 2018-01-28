node {
        stage('SCM') { 
            git([url: 'https://github.com/hpejustin/JFrog.git', branch: 'master'])
            sh 'echo scm finished successfully.'
        }
        stage('UT') {  
            sh 'mvn clean test'
            sh 'echo ut test finished successfully.'
        }
        stage('Sonar') {  
            sh 'echo sonar scan goes here...'
            def scannerHome = tool 'sonarClient';
            withSonarQubeEnv('sonar') {
                sh "${scannerHome}/bin/sonar-runner"
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
            sh 'docker login docker-release-local.demo.jfrogchina.com -u admin -pjfrogchina'
            sh 'docker tag jfrog-cloud-demo:${BUILD_ID} docker-release-local.demo.jfrogchina.com/jfrog-cloud-demo:${BUILD_ID}'
            sh 'docker push docker-release-local.demo.jfrogchina.com/jfrog-cloud-demo:${BUILD_ID}'
            sh 'docker rmi jfrog-cloud-demo:${BUILD_ID} docker-release-local.demo.jfrogchina.com/jfrog-cloud-demo:${BUILD_ID}'
            sh 'docker logout docker-release-local.demo.jfrogchina.com'
        }
        stage('Deploy') {
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
