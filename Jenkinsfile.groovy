def appName = "qddt-service"
def containerName = "qddt_service"
def imageName = "qddt/${appName}"
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

//    FIXME: for this to work the database need to be configurable via env variables, and not hardcoded in the .properties files
//    stage 'Test image'
//        docker.image('jenkins.nsd.lan:5000/docker-compose').inside('-v /var/run/docker.sock:/var/run/docker.sock') {
//            def project = 'qddtservice'
//            sh "docker-compose --project-name ${project} down"
//            def success = false;
//            try {
//                sh "docker-compose -f docker-compose.ci.yml --project-name ${project} up -d"
//                def ip = "\$(docker inspect --format '{{ .NetworkSettings.Networks.${project}_default.Gateway }}' ${containerName})"
//                retry(5) {
//                    sleep 5
//                    sh "curl -s -k http://${ip}:8080/management/info"
//                    success = true
//                }
//            } finally {
//                sh "docker-compose --project-name ${project} down"
//            }
//            if (!success)
//                error 'the application did not respond well when starting up'
//        }

    stage 'Push repository'
        docker.withRegistry("https://${dockerRegistry}") {
            docker.image("${imageName}").push('latest')
            docker.image("${imageName}").push("${env.BUILD_NUMBER}")
        }

    stage 'Deploy staging'
        sshagent(['8a47fce5-7c54-4d27-a329-65617ea92a55']) {
            def targetHost = 'deploy@qddt-dev.nsd.no'
            withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'gitlab-docker',
                              usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
                sh "ssh ${targetHost} sudo docker login --username=${env.USERNAME} --password=${env.PASSWORD} docker.nsd.no"
            }
            sh "ssh ${targetHost} sudo docker pull ${dockerRegistry}/${imageName}"
            sh "ssh ${targetHost} sudo docker stop ${containerName} || true"
            sh "ssh ${targetHost} sudo docker run -d -p 8081:5001 --name ${containerName} " +
                    "-v /home/deploy/deployment/test/uploads-to-qddt/:/home/deploy/deployment/test/uploads-to-qddt/ " +
                    "-e PROFILE=dev -e DB_HOST=TODO ${dockerRegistry}/${imageName}"
        }
}

stage 'Deploy production'
input 'Deploy to production?'
node('docker-slave') {
    sshagent(['8a47fce5-7c54-4d27-a329-65617ea92a55']) {
        def targetHost = 'deploy@qddt-dev.nsd.no'
        withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'gitlab-docker',
                          usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
            sh "ssh ${targetHost} sudo docker login --username=${env.USERNAME} --password=${env.PASSWORD} docker.nsd.no"
        }
        sh "ssh ${targetHost} sudo docker pull ${dockerRegistry}/${imageName}"
        sh "ssh ${targetHost} sudo docker stop ${containerName} || true"
        sh "ssh ${targetHost} sudo docker run -d -p 8082:5002 --name ${containerName} " +
                "-v /home/deploy/deployment/prod/uploads-to-qddt/:/home/deploy/deployment/prod/uploads-to-qddt/ " +
                "-e PROFILE=production -e DB_HOST=TODO ${dockerRegistry}/${imageName}"
    }
}
