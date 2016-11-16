def appName = "qddt-service"
def containerName = "qddt_service"
def targetDir = "/home/deploy/deployment/${appName}"
def imageName = "dasish/${appName}"
def dockerRegistry = 'docker.nsd.no'

node('docker-slave') {
    stage 'Checkout git'
        checkout scm

    stage 'Build'
        docker.image('localhost:5000/gradle-builder').inside() {
            sh 'gradle build -x test'
        }

    stage 'Bake image'
        docker.build("${imageName}")

    stage 'Test image'
        docker.image('jenkins.nsd.lan:5000/docker-compose').inside('-v /var/run/docker.sock:/var/run/docker.sock') {
            def project = 'qddtservice'
            sh "docker-compose -f docker-compose.ci.yml --project-name ${project} down"
            sh "docker-compose -f docker-compose.ci.yml --project-name ${project} up -d"
            def ip = "\$(docker inspect --format '{{ .NetworkSettings.Networks.${project}_default.Gateway }}' ${containerName})"
            def success = false;
            retry(5) {
                sleep 5
                sh "curl -s -k http://${ip}:8080/management/info"
                success = true
            }
            if (!success)
                error 'the application did not respond well when starting up'
        }

    stage 'Push repository'
        docker.withRegistry("https://${dockerRegistry}") {
            docker.image("${imageName}").push('latest')
            docker.image("${imageName}").push("${env.BUILD_NUMBER}")
        }

    stage 'Deploy staging'
        sshagent(['8a47fce5-7c54-4d27-a329-65617ea92a55']) {
            def targetHost = 'deploy@qddt-test.nsd.uib.no'
            sh "ssh ${targetHost} mkdir -p ${targetDir}"
            withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'gitlab-docker',
                              usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
                sh "ssh ${targetHost} sudo docker login --username=${env.USERNAME} --password=${env.PASSWORD} docker.nsd.no"
            }
            sh "ssh ${targetHost} sudo docker pull ${dockerRegistry}/${imageName}"
            sh "ssh ${targetHost} sudo docker stop ${containerName}"
            sh "ssh ${targetHost} sudo docker run -d -p 8080:8080 --name ${containerName} " +
                    "-e PROFILE=staging -e DB_HOST=qddt-test.nsd.no ${dockerRegistry}/${imageName}"
        }
}
