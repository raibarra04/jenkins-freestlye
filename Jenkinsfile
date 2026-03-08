#!/usr/bin/env groovy 
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
        stage("increment version") {
            steps {
                script {
                    gv.incrementVersion()                    
                }
            }

        }
        stage("build jar") {
            // when {
            //     expression {
            //         BRANCH_NAME == 'main'
            //     }
            // }
            steps {
                script {
                    gv.buildJar()                    
                }
            }

        }
        stage("build image") {
            // when {
            //     expression {
            //         BRANCH_NAME == 'main'
            //     }
            // }
            steps {
                script {
                    gv.buildImage()
                }
            }

        }
        stage("deploy") {
            when {
                expression {
                    BRANCH_NAME == 'jenkins-jobs-mine'
                }
            }
            steps {
                script {
                    gv.deployApp()                    
                }
            }

        }
        stage("commit version update") {
            steps {
                script {
                    gv.commitVersionUpdate()                    
                }
            }

        }
    }
}