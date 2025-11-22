<div align="center">

# 🚀 Jenkins Shared Library

### Reusable CI/CD Pipeline Components for Docker & Maven Projects

[![Jenkins](https://img.shields.io/badge/Jenkins-D24939?style=for-the-badge&logo=jenkins&logoColor=white)](https://www.jenkins.io/)
[![Groovy](https://img.shields.io/badge/Groovy-4298B8?style=for-the-badge&logo=apache-groovy&logoColor=white)](https://groovy-lang.org/)
[![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)
[![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)](https://maven.apache.org/)

</div>

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Project Structure](#-project-structure)
- [Available Functions](#-available-functions)
- [Installation](#-installation)
- [Usage](#-usage)
- [Examples](#-examples)
- [Contributing](#-contributing)

---

## 🎯 Overview

This Jenkins Shared Library provides a set of reusable pipeline steps for building, testing, and deploying applications using Docker and Maven. It simplifies your CI/CD workflows by encapsulating common operations into easy-to-use functions.

---

## ✨ Features

- 🏗️ **Maven Build Automation** - Build Java applications with `mvn clean package`
- 🐳 **Docker Integration** - Build, login, and push Docker images
- 🔐 **Secure Credentials** - Built-in Docker Hub authentication
- 📦 **Modular Design** - Reusable components across multiple pipelines
- 🔄 **Version Control Aware** - Git branch detection support

---

## 📁 Project Structure

```
jenkins-shared-library/
├── vars/                    # Global variables (pipeline steps)
│   ├── buildImage.groovy   # Docker image build step
│   ├── buildJar.groovy     # Maven build step
│   ├── dockerLogin.groovy  # Docker Hub login step
│   └── dockerPush.groovy   # Docker image push step
├── src/                     # Shared library classes
│   └── com/example/
│       └── Docker.groovy   # Docker operations class
└── lib/                     # External libraries
```

---

## 🛠️ Available Functions

### `buildJar()`
Builds a Maven project using `mvn clean package`.

**Parameters:** None

**Example:**
```groovy
buildJar()
```

---

### `buildImage(String imageName)`
Builds a Docker image with the specified name.

**Parameters:**
- `imageName` - Name and tag for the Docker image (e.g., `myapp:1.0.0`)

**Example:**
```groovy
buildImage('myapp:latest')
```

---

### `dockerLogin()`
Authenticates with Docker Hub using credentials stored in Jenkins.

**Parameters:** None

**Credentials Required:**
- Credential ID: `docker-hub-repo`
- Type: Username with password

**Example:**
```groovy
dockerLogin()
```

---

### `dockerPush(String imageName)`
Pushes a Docker image to Docker Hub.

**Parameters:**
- `imageName` - Name and tag of the image to push

**Example:**
```groovy
dockerPush('myapp:latest')
```

---

## 📥 Installation

### 1. Configure in Jenkins

1. Go to **Jenkins → Manage Jenkins → Configure System**
2. Scroll to **Global Pipeline Libraries**
3. Click **Add** and configure:
   - **Name:** `jenkins-shared-library`
   - **Default version:** `main` (or your branch name)
   - **Retrieval method:** Modern SCM
   - **Source Code Management:** Git
   - **Project Repository:** `<your-repo-url>`

### 2. Load Library Implicitly (Optional)

Check **Load implicitly** to make the library available to all pipelines automatically.

---

## 🚀 Usage

### Import in Jenkinsfile

```groovy
@Library('jenkins-shared-library') _

pipeline {
    agent any
    
    stages {
        stage('Build') {
            steps {
                script {
                    buildJar()
                }
            }
        }
        
        stage('Docker Build & Push') {
            steps {
                script {
                    buildImage("myusername/myapp:${env.BUILD_NUMBER}")
                    dockerLogin()
                    dockerPush("myusername/myapp:${env.BUILD_NUMBER}")
                }
            }
        }
    }
}
```

---

## 💡 Examples

### Complete CI/CD Pipeline

```groovy
@Library('jenkins-shared-library') _

pipeline {
    agent any
    
    environment {
        DOCKER_IMAGE = "myusername/myapp"
        VERSION = "${env.BUILD_NUMBER}"
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Build Application') {
            steps {
                script {
                    buildJar()
                }
            }
        }
        
        stage('Build Docker Image') {
            steps {
                script {
                    buildImage("${DOCKER_IMAGE}:${VERSION}")
                    buildImage("${DOCKER_IMAGE}:latest")
                }
            }
        }
        
        stage('Push to Docker Hub') {
            steps {
                script {
                    dockerLogin()
                    dockerPush("${DOCKER_IMAGE}:${VERSION}")
                    dockerPush("${DOCKER_IMAGE}:latest")
                }
            }
        }
    }
    
    post {
        success {
            echo "✅ Pipeline completed successfully!"
        }
        failure {
            echo "❌ Pipeline failed!"
        }
    }
}
```

### Multi-Branch Pipeline

```groovy
@Library('jenkins-shared-library') _

pipeline {
    agent any
    
    stages {
        stage('Build') {
            steps {
                script {
                    // Branch name is automatically detected
                    buildJar()
                    
                    def imageName = "myapp:${env.GIT_BRANCH}-${env.BUILD_NUMBER}"
                    buildImage(imageName)
                    
                    if (env.GIT_BRANCH == 'main') {
                        dockerLogin()
                        dockerPush(imageName)
                    }
                }
            }
        }
    }
}
```

---

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📝 Requirements

- Jenkins 2.x or higher
- Docker installed on Jenkins agent
- Maven installed on Jenkins agent
- Docker Hub credentials configured in Jenkins

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

---

<div align="center">

**Made with ❤️ for DevOps Engineers**

</div>
