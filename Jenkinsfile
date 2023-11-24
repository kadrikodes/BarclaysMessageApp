pipeline {
    agent {
        docker {
            image 'maven:3.9.0'
            args '-v /root/.m2:/root/.m2'
        }
    }
    options {
        skipStagesAfterUnstable()
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests clean package'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        stage('SonarQube Analysis') {
                            steps {
                                script {
                                    withSonarQubeEnv('sonarqube') {
                //                        sh "${tool('sonar-scanner')}/bin/sonar-scanner -Dsonar.projectKey=myProjectKey -Dsonar.projectName=myProjectName"
                                        sh 'mvn clean package sonar:sonar'
                                }
                            }
                }
        }
        stage('Deliver') {
            steps {
                //sh './jenkins/scripts/deliver.sh'
                echo 'delivery successful'
            }
        }

    }
}
