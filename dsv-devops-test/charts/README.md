# Introduction
## Develop on Azure Kubernetes Service (AKS) with Helm

[Helm][helm] is an open-source packaging tool that helps you install and manage the lifecycle of Kubernetes applications. Similar to Linux package managers like *APT* and *Yum*, Helm manages Kubernetes charts, which are packages of pre-configured Kubernetes resources.

## Prerequisites

* An Azure subscription. If you don't have an Azure subscription, you can create a [free account](https://azure.microsoft.com/free).
* [Azure CLI][azure-cli-install] or [Azure PowerShell][azure-powershell-install] installed.
* [Helm v3 installed][helm-install].

## Create an Azure Container Registry
You'll need to store your container images in an Azure Container Registry (ACR) to run your application in your AKS cluster using Helm. Provide your own registry name unique within Azure and containing 5-50 alphanumeric characters. The *Basic* SKU is a cost-optimized entry point for development purposes that provides a balance of storage and throughput.

### [Azure CLI](#tab/azure-cli)

The below example uses [az acr create][az-acr-create] to create an ACR named *MyHelmACR* in *MyResourceGroup* with the *Basic* SKU.

```t
az group create --name acr-rg --location eastus
az acr create --resource-group acr-rg --name k8sdevopsacr --sku Basic
```

#### Install Helm3 (if not installed)
```t
# MAC installation
brew install helm

# Debian/Ubuntu Installation
curl https://baltocdn.com/helm/signing.asc | gpg --dearmor | sudo tee /usr/share/keyrings/helm.gpg > /dev/null
sudo apt-get install apt-transport-https --yes
echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/helm.gpg] https://baltocdn.com/helm/stable/debian/ all main" | sudo tee /etc/apt/sources.list.d/helm-stable-debian.list
sudo apt-get update
sudo apt-get install helm
```
### Generate your Helm chart using the `helm create` command.

```t
# To create helm chart
helm create dsv-devops-test
```
- Update *dsv-devops-test/Chart.yaml* to add a dependency if any required for the chart.
- Update the defult templates as per your requirements.


#####  Create Docker-Registry secrets
```t
# Run below command to create a namespace dsv-app
kubectl create namespace dsv-app

# Run below command to create Docker-registry secret
# kubectl create secret docker-registry myreg --docker-server=https://index.docker.io/v1/ --docker-username=<USERNAME> --docker-password=<PASSWORD> --docker-email=<EMAIL ID> -n dsv-devops
```

### Deploy Application
##### Deploy Generic Application k8s helm chart and verify

- Install your application using your Helm chart using the `helm install` command.

```t
# Install created chart with the default values in values.yaml
helm install dsv-devops-test dsv-devops-test/ \
    --namespace dsv-app --create-namespace

# Custom Parameters in Command Line -set
helm install dsv-devops-test dsv-devops-test/ \
    --namespace dsv-app --create-namespace \
    --set image.tag=latest \
    --set image.repository=nasserahmad\java-application \
    --set hosts.host=www.kubekon.info \
    --set orgPassword=secret

# Custom Parameters from a YAML file-values
helm install --values custom-values.yaml dsv-devops-test dsv-devops-test/
    --namespace dsv-pp --create-namespace

# List the installed Helm chart
helm list -n dsv-app

# To check helm chart revision history
helm history dsv-devops-test -n dsv-app

# It takes a few minutes for the service to return a public IP address. Monitor progress using the `kubectl get service` command with the `--watch` argument.

$ kubectl get svc -n dsv-app --watch

```

