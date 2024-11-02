pipeline {
    agent any
    environment {
        DOCKER_CREDENTIAL_ID = 'dockerhub-credentials'  // Jenkins에서 등록한 자격 증명 ID
        DOCKER_IMAGE = 'your-dockerhub-username/muiu-backend'  // Docker Hub 이미지 이름
        DOCKER_TAG = 'latest'  // 이미지 태그
    }
    stages {
        stage('Build') {
            steps {
                echo 'Building Spring Boot Application...'
                sh './gradlew clean build'  // Gradle을 사용하여 빌드 수행
            }
        }
        stage('Build Docker Image') {
            steps {
                echo 'Building Docker Image...'
                script {
                    docker.build("${DOCKER_IMAGE}:${DOCKER_TAG}", ".")
                }
            }
        }
        stage('Push to Docker Hub') {
            steps {
                echo 'Pushing Docker Image to Docker Hub...'
                script {
                    docker.withRegistry('https://index.docker.io/v1/', "${DOCKER_CREDENTIAL_ID}") {
                        docker.image("${DOCKER_IMAGE}:${DOCKER_TAG}").push()
                    }
                }
            }
        }
    }
}
