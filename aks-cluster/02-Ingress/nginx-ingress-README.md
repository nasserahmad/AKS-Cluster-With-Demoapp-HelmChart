# Ingress

## 01: Introduction
  - Accept traffic from outside the Kubernetes platform, and load balance it to pods (containers) running inside the platform
  - Can manage egress traffic within a cluster for services which need to communicate with other services outside of a cluster
  - Are configured using the Kubernetes API to deploy objects called “Ingress Resources”
  - Monitor the pods running in Kubernetes and automatically update the load‑balancing rules when pods are added or removed from a service
  
### Ingress Basic Architecture
[![Image](https://github.com/nasserahmad/AKS-Cluster-With-Demoapp-HelmChart/blob/main/aks-cluster/02-Ingress/basic-arch-png/basic-arch.PNG)

### What are we going to configure and install to implement ingress architecture?

- We are going to create a **Static Public IP** for Ingress in Azure AKS
- Associate that Public IP to **Ingress Controller** during installation.
- We are going to create a namespace `nginx-ingress` for Ingress Controller where all ingress controller related things will be placed. 
- In future, we can install **cert-manager** for SSL certificates also in same namespace. 
- **Caution Note:** This namespace is for Ingress controller stuff, ingress resource we can create in any other namespaces and not an issue.  Only condition is create ingress resource and ingress pointed application in same namespace (Example: DemoApp and Ingress resource of DemoApp should be in same namespace)
- Create / Review Ingress Manifest
- Deploy a simple Nginx DemoApp with Ingress manifest and test it
- Clean-Up or delete application after testing

## 02: Create Static Public IP
```t
# Get the resource group name of the AKS cluster 
az aks show --resource-group aks-rg1 --name mycluster --query nodeResourceGroup -o tsv

# TEMPLATE - Create a public IP address with the static allocation
az network public-ip create --resource-group <REPLACE-OUTPUT-RG-FROM-PREVIOUS-COMMAND> --name myAKSPublicIPForIngress --sku Standard --allocation-method static --query publicIp.ipAddress -o tsv

# REPLACE - Create Public IP: Replace Resource Group value
az network public-ip create --resource-group MC_aks-rg1_mycluster_westeurope --name myAKSPublicIPForIngress --sku Standard --allocation-method static --query publicIp.ipAddress -o tsv
```
az network public-ip show --resource-group MC_aks-rg1_mycluster_eastus --name myAKSPublicIPForIngress --query ipAddress --output tsv

- Make a note of Static IP which we will use in next step when installing Ingress Controller
```t
# Make a note of Public IP created for Ingress
20.241.251.83
```

## 03: Install Ingress Controller
```t
# Install Helm3 (if not installed)
# MAC installation
brew install helm

# Debian/Ubuntu Installation
curl https://baltocdn.com/helm/signing.asc | gpg --dearmor | sudo tee /usr/share/keyrings/helm.gpg > /dev/null
sudo apt-get install apt-transport-https --yes
echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/helm.gpg] https://baltocdn.com/helm/stable/debian/ all main" | sudo tee /etc/apt/sources.list.d/helm-stable-debian.list
sudo apt-get update
sudo apt-get install helm

# Create a namespace for your ingress resources
kubectl create namespace nginx-ingress

# Add the official stable repository
helm repo add nginx-stable https://helm.nginx.com/stable
helm repo update
helm repo list

#  Customizing the Chart Before Installing.
helm search repo nginx-ingress 
helm show values nginx-stable/nginx-ingress

# Use Helm to deploy an NGINX ingress controller
helm install nginx-ingress nginx-stable/nginx-ingress \
    --namespace nginx-ingress --create-namespace \
    --set replicaCount=2 \
    --set nodeSelector."beta\.kubernetes\.io/os"=linux \
    --set service.externalTrafficPolicy=Local \
    --set service.loadBalancerIP="REPLACE_STATIC_IP" 

# Replace Static IP captured in 02
helm install nginx-ingress nginx-stable/nginx-ingress \
    --namespace nginx-ingress --create-namespace \
    --set replicaCount=1 \
    --set nodeSelector."beta\.kubernetes\.io/os"=linux \
    --set service.externalTrafficPolicy=Local \
    --set service.loadBalancerIP="20.241.251.83" 

# List Services with labels
kubectl get service -l app.kubernetes.io/instance=nginx-ingress --namespace nginx-ingress

# List Pods
kubectl get pods -n nginx-ingress
kubectl get all -n nginx-ingress


# Access Public IP
http://<Public-IP-created-for-Ingress>

# Output should be
404 Not Found from Nginx

# Verify Load Balancer on Azure Mgmt Console
Primarily refer Settings -> Frontend IP Configuration
```
## 04: Review Application k8s manifests
- 01-NginxDemoApp-Deployment.yml
- 02-NginxDemoApp-ClusterIP-Service.yml
- 03-ingress.yml

## 05: Deploy Application k8s manifests and verify
```t
# Deploy
kubectl apply -f kube-manifests/

# List Pods
kubectl get pods

# List Services
kubectl get svc

# List Ingress
kubectl get ingress

# Access Application
http://<Public-IP-created-for-Ingress>/DemoApp/index.html
http://<Public-IP-created-for-Ingress>

# Verify Ingress Controller Logs
kubectl get pods -n nginx-ingress
kubectl logs -f <pod-name> -n nginx-ingress
```

## 06: Clean-Up Apps
```t
# Delete Apps
kubectl delete -f kube-manifests/
```

## Ingress Annotation Reference
- https://kubernetes.github.io/ingress-nginx/user-guide/nginx-configuration/annotations/

## Other References
- https://github.com/kubernetes/ingress-nginx
- https://github.com/kubernetes/ingress-nginx/blob/master/charts/ingress-nginx/values.yaml
- https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v0.34.1/deploy/static/provider/cloud/deploy.yaml
- https://kubernetes.github.io/ingress-nginx/deploy/#azure
- https://helm.sh/docs/intro/install/
- https://kubernetes.io/docs/reference/generated/kubernetes-api/v1.19/#ingress-v1-networking-k8s-io


