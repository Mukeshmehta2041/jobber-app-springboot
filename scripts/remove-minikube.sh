#!/bin/bash

echo "🧹 Stopping Minikube cluster..."
minikube stop

echo "🗑️ Deleting Minikube cluster..."
minikube delete --all --purge

echo "🧽 Removing Minikube directories..."
rm -rf ~/.minikube
rm -rf ~/.kube

echo "🧼 Removing Minikube binary (if installed manually)..."
sudo rm -f /usr/local/bin/minikube

echo "🧼 Removing kubectl binary (if installed manually)..."
sudo rm -f /usr/local/bin/kubectl

echo "✅ Minikube and related files have been removed."
