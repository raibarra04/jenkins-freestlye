#!/user/bin/env groovy

library identifier: 'jenkins-shared-library@main', retriever: modernSCM(
        [$class: 'GitSCMSource',
        remote: 'https://github.com/raibarra04/jenkins-shared-library.git',
        credentialsId: 'github-login-credentials'])

def gv

pipeline {
    agent any
    tools {
        maven "maven-3.9"
    }
    stages {
        stage("init") {
            steps {
                script {
                    gv = load "script.groovy"
                }
            }

        }
        stage("test app") {
            steps {
                script {
                    gv.testApp()                    
                }
            }

        }
        stage("build jar") {
            steps {
                script {
                    buildJar()                    
                }
            }

        }
        stage("build image") {
            steps {
                script {
                    buildImage 'raibarra/java-maven-demo-app:jma-3.0'
                    dockerLogin()
                    dockerPush 'raibarra/java-maven-demo-app:jma-3.0'
                }
            }

        }
        stage("deploy") {
            steps {
                script {
                    gv.deployApp()                    
                }
            }

        }
    }
}