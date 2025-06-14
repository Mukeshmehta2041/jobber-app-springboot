
# 🚀 Minikube Setup with Docker 

This guide will help you set up **Minikube** using **Docker** as the driver on a system that supports Docker.

## 📋 Prerequisites

Make sure you have the following installed:

### 1. **Docker**

Install Docker from the official site: [https://www.docker.com/products/docker-desktop](https://www.docker.com/products/docker-desktop)

Verify installation:

```bash
docker --version
```

### 2. **kubectl (Kubernetes CLI)**

```bash
# On macOS (with Homebrew)
brew install kubectl

# On Ubuntu/Debian
sudo apt update && sudo apt install -y kubectl

# Verify
kubectl version --client
```

### 3. **Minikube**

```bash
# On macOS (with Homebrew)
brew install minikube

# On Linux
curl -LO https://storage.googleapis.com/minikube/releases/latest/minikube-linux-amd64
sudo install minikube-linux-amd64 /usr/local/bin/minikube

# Verify
minikube version
```

## 🚦 Start Minikube with Docker Driver

```bash
minikube start --driver=docker
```

> 💡 If Docker is running correctly, Minikube will use it to create a local Kubernetes cluster.

To check if the cluster is up and running:

```bash
kubectl get nodes
```

## 🔄 Common Commands

### 📦 Deploy a Sample App

```bash
kubectl create deployment hello-minikube --image=kicbase/echo-server:1.0
kubectl expose deployment hello-minikube --type=NodePort --port=8080
minikube service hello-minikube --url
```

### 📜 View Logs

```bash
kubectl logs deployment/hello-minikube
```

### 🔧 Minikube Dashboard (optional GUI)

```bash
minikube dashboard
```

## 🧹 Stop & Delete Minikube Cluster

```bash
minikube stop
minikube delete
```

---

## 🛠️ Troubleshooting Tips

* Make sure Docker is running before starting Minikube.
* If you see `docker: permission denied`, try running:

  ```bash
  sudo usermod -aG docker $USER
  newgrp docker
  ```

---

## 📁 Sample `README.md` Directory Structure

```
project-root/
├── README.md
└── k8s/
    └── hello-minikube.yaml
```

---


