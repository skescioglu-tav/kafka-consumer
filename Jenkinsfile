pipeline {
    agent any

    tools{
        jdk 'jdk21'
    }

    stages {
        stage('Code Checkout') {
            steps {
                git branch: 'main', changelog: false, poll: false, url: 'https://github.com/skescioglu-tav/kafka-consumer.git'
            }
        }

        stage('Clean & Package'){
            steps{
                sh "mvn clean package -DskipTests"
            }
        }



       stage("Docker Build & Push"){
            steps{
                script{
                    withDockerRegistry(credentialsId: 'registrycredentials') {
                        def imageName = "kafka-consumer"
                        sh "docker build -t 10.0.0.101:30002/poc-apps/${imageName} -f Dockerfile ."
                        sh "docker push 10.0.0.101:30002/poc-apps/${imageName}"
                    }

                }
            }
        }


    }
}