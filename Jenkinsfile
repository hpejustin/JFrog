node {
    def mvnHome
    def artiServer
    def rtMaven
    def buildInfo
    stage('Prepare') {
        artiServer = Artifactory.server('artiha-demo')
        buildInfo = Artifactory.newBuildInfo()
        buildInfo.env.capture = true
        rtMaven = Artifactory.newMavenBuild()
        rtMaven.tool = "maven"
        // Specific target repo
        rtMaven.deployer releaseRepo:'libs-release-local', snapshotRepo:'libs-snapshot-local', server: artiServer
        // Specific dependency resolve repo
        rtMaven.resolver releaseRepo:'libs-release', snapshotRepo:'libs-snapshot', server: artiServer

        sh 'kubectl -s kube-master:8080 --namespace=devops delete deploy --all'
        sh 'kubectl -s kube-master:8080 --namespace=devops delete svc --all'
        sh 'sleep 10'
    }
    stage('SCM') {
        // Checkout source code
        git([url: 'https://github.com/hpejustin/JFrog.git', branch: 'master'])
    }
    stage('Sonar') {
        sh 'echo sonar scan goes here...'
        def scannerHome = tool 'sonarClient';
        withSonarQubeEnv('sonar') {
            sh "${scannerHome}/bin/sonar-runner"
        }
    }
    stage('Build') {
        rtMaven.run pom: 'pom.xml', goals: 'clean test install', buildInfo: buildInfo
    }
    stage('Image') {
        def tagName='docker-snapshot-local.demo.jfrogchina.com/jfrog-cloud-demo:' + env.BUILD_NUMBER
        docker.build(tagName)
        def artDocker= Artifactory.docker('admin', 'AKCp2WXCWmSmLjLc5VKVYuSeumtarKV7TioZfboRAEwC1tqKAUvbniFJqp7xLfCyvJ7GxWuJZ')
        artDocker.push(tagName, 'docker-snapshot-local', buildInfo)
        artiServer.publishBuildInfo buildInfo
    }
    stage('Deploy to Kubernetes') {
        sh 'echo $(pwd)'
        sh 'sed -i "s/{tag}/${BUILD_ID}/g" kube-app.json'
        sh 'sleep 10'
        sh 'kubectl -s kube-master:8080 create -f kube-svc.json'
        sh 'kubectl -s kube-master:8080 create -f kube-app.json'
        sh 'for i in {1..120}; do echo "waiting for app starting,$[180-$i] second left..."; sleep 1; done;'
        sh 'echo deploy finished successfully.'
    }
    stage('Upload Metadata') {
        sh 'echo data collection here...'
        sh 'curl -X PUT \"http://demo.jfrogchina.com/artifactory/api/storage/docker-snapshot-local/jfrog-cloud-demo/${BUILD_ID}?properties=build.name=Cloud-Native-Demo-01;build.version=${BUILD_ID};ut=paas;ut.passRate=1;sonar=done;envType=kube;env.namespace=devops\" -u admin:jfrogchina'
    }
    stage('Pong') {
        sh 'echo pipeline ended, please access http://39.106.21.94:8081/hello'
    }
}
