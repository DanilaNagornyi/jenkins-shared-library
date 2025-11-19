#!/user/bin/env groovy

def call() {
//    echo "building the application for branch $GIT_BRANCH"
    echo "building the application ..."
    sh 'mvn clean package'
}