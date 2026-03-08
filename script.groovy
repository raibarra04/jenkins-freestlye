def testApp() {
    echo "Testing the application..."
}

def incrementVersion() {
    echo "Incrementing app version..."
    sh 'mvn build-helper:parse-version versions:set \
    -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.nextIncrementalVersion} \
    versions:commit'
    def matcher = readFile('pom.xml') =~ '<version>(.+)</version>'
    def version = matcher[0][1]
    env.IMAGE_NAME = "$version-$BUILD_NUMBER"
}

def buildJar() {
    echo "Building the application..."
    sh "mvn clean package"
}

def buildImage() {
    echo "Building the docker image..."
    withCredentials([usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh "docker build -t raibarra/java-maven-demo-app:${IMAGE_NAME} ."
        sh 'echo $PASS | docker login -u $USER --password-stdin'
        sh "docker push raibarra/java-maven-demo-app:${IMAGE_NAME}"
    }
}

def deployApp() {
    echo "Deploying the application to EC2..."
    def shellCmd = "bash ./server-cmds.sh"
    sshagent(['ec2-server-key']) {
        sh "scp server-cmds.sh ec2-user@3.17.190.15:/home/ec2-user"
        sh "scp docker-compose.yaml ec2-user@3.17.190.15:/home/ec2-user"
        sh "ssh -o StrictHostKeyChecking=no ec2-user@3.17.190.15 ${shellCmd}"
    }
}

def commitVersionUpdate() {
    echo "Committing app version update..."
    withCredentials([string(credentialsId: 'github-token', variable: 'TOKEN')]) {
        sh 'git config --global user.email "jenkins@example.com"'
        sh 'git config --global user.name "jenkins"'
        
        sh 'git status'
        sh 'git branch'
        sh 'git config --list'
        
        sh "git remote set-url origin https://${TOKEN}@github.com/raibarra04/jenkins-multibranch-pipeline.git"
        sh 'git add .'
        sh 'git commit -m "chore: no-ref: ci version update"'
        sh 'git push origin HEAD:jenkins-jobs-mine'
    }
}
return this

                