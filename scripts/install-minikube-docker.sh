#!/bin/bash

echo "🔄 Updating packages..."
sudo apt-get update -y

echo "📦 Installing dependencies..."
sudo apt-get install -y curl apt-transport-https ca-certificates conntrack

echo "🐳 Checking Docker..."
if ! command -v docker &> /dev/null; then
  echo "❌ Docker is not installed. Please install Docker first."
  exit 1
fi

echo "📥 Installing kubectl..."
curl -LO "https://storage.googleapis.com/kubernetes-release/release/$(curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt)/bin/linux/amd64/kubectl"
chmod +x kubectl
sudo mv kubectl /usr/local/bin/

echo "📥 Installing Minikube..."
curl -LO https://storage.googleapis.com/minikube/releases/latest/minikube-linux-amd64
chmod +x minikube-linux-amd64
sudo install minikube-linux-amd64 /usr/local/bin/minikube

echo "🚀 Starting Minikube with Docker driver..."
minikube start --driver=docker

echo "✅ Minikube is set up and running!"
kubectl get nodes
