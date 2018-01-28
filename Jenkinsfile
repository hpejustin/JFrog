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
            sh 'docker login docker-snapshot-local.demo.jfrogchina.com -u admin -p AKCp2WXCWmSmLjLc5VKVYuSeumtarKV7TioZfboRAEwC1tqKAUvbniFJqp7xLfCyvJ7GxWuJZ'
            sh 'docker tag jfrog-cloud-demo:${BUILD_ID} docker-snapshot-local.demo.jfrogchina.com/jfrog-cloud-demo:${BUILD_ID}'
            sh 'docker push docker-snapshot-local.demo.jfrogchina.com/jfrog-cloud-demo:${BUILD_ID}'
            sh 'docker rmi jfrog-cloud-demo:${BUILD_ID} docker-snapshot-local.demo.jfrogchina.com/jfrog-cloud-demo:${BUILD_ID}'
            sh 'docker logout docker-snapshot-local.demo.jfrogchina.com'
        }
        stage('Preconditions') {
            sh 'kubectl -s kube-master:8080 deploy '
            sh 'kubectl -s kube-master:8080 delete svc jfrog-cloud-svc'
        }
        stage('Deploy') {
            sh 'echo $(pwd)'
            sh 'sed -i "s/{tag}/${BUILD_ID}/g" kube-app.json'
            sh 'sleep 10'
            sh 'kubectl -s kube-master:8080 create -f kube-svc.json'
            sh 'kubectl -s kube-master:8080 create -f kube-app.json'
            sh 'for i in {1..20}; do echo "waiting for app starting..."; sleep 1; done;'
            sh 'echo deploy finished successfully.'
        }
        stage('Data Collection') {  
            sh 'echo data collection here...'
            sh 'curl -X PUT \"http://demo.jfrogchina.com/artifactory/api/storage/docker-snapshot-local/jfrog-cloud-demo/${BUILD_ID}?properties=ut=paas;sonar=done;version=${BUILD_ID};envType=kube\" -uadmin:AKCp2WXCWmSmLjLc5VKVYuSeumtarKV7TioZfboRAEwC1tqKAUvbniFJqp7xLfCyvJ7GxWuJZ'
        }
        stage('Print') {
            sh 'echo pipeline ended, please access http://39.106.21.94:8081/hello'
        }
}
