// Jenkinsfile
pipeline {
    agent any

    stages {
        stage('Clone Repository') {
            steps {
                // 소스 코드 클론
                git 'https://github.com/your-repository/muiu-back.git'
            }
        }
        
        stage('Build') {
            steps {
                // 빌드 및 JAR 생성
                sh './gradlew clean build'
            }
        }

        stage('Docker Build') {
            steps {
                // Docker 이미지 생성
                script {
                    docker.build("soojeongmin/muiu-back:latest", "-f Dockerfile .")
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withDockerRegistry(credentialsId: 'dockerhub-credentials', url: 'https://index.docker.io/v1/') {
                    script {
                        docker.image("soojeongmin/muiu-back:latest").push()
                    }
                }
            }
        }
    }
}
