def call(Map config = [:]) {

    def registry = config.registry
    def image = config.image

    pipeline {
        agent any

        tools {
            nodejs "my-nodejs"
        }

        stages {

            stage('Install deps & test') {
                steps {
                    dir("app") {
                        sh "npm install"
                        sh "npm test"
                    }
                }
            }

            stage('Increment version') {
                steps {
                    dir("app") {
                        sh "npm version minor --no-git-tag-version"
                    }
                }
            }

            stage('Read version') {
                steps {
                    script {
                        VERSION = sh(
                                script: "node -p \"require('./app/package.json').version\"",
                                returnStdout: true
                        ).trim()
                    }
                }
            }

            stage('Docker build') {
                steps {
                    dir("app") {
                        sh "docker build -t ${registry}/${image}:${VERSION} ."
                    }
                }
            }

            stage('Docker push') {
                steps {
                    withCredentials([usernamePassword(credentialsId: 'gitlab-pass',
                            usernameVariable: 'USER', passwordVariable: 'PASS')]) {

                        sh "echo ${PASS} | docker login -u ${USER} --password-stdin registry.gitlab.com"
                        sh "docker push ${registry}/${image}:${VERSION}"

                    }
                }
            }

            stage('Git commit') {
                steps {
                    withCredentials([usernamePassword(credentialsId: 'gitlab-pass',
                            usernameVariable: 'USER', passwordVariable: 'PASS')]) {

                        sh 'git config --global user.email "jenkins@example.com"'
                        sh 'git config --global user.name "jenkins"'
                        sh "git add ."
                        sh "git commit -m \"ci: version bump to ${VERSION}\" || true"
                        sh "git push https://${USER}:${PASS}@gitlab.com/DanilaNagornyi/jenkins-exercises.git HEAD:${env.GIT_BRANCH}"
                    }
                }
            }
        }
    }
}
