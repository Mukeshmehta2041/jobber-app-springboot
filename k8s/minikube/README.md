- kubectl create namespace production

- kubectl get namespaces

- kubectl apply -f auth-depl.yaml

- kubectl apply -f auth-service.yaml

- kubectl get pods -n production

- kubectl get services -n production

- kubectl get pods -n production -l app=service-registry

- kubectl port-forward -n production pod/service-registry-7b6fdc9974-72vds 8761:8761
