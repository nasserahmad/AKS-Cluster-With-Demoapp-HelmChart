## Introduction

In this Section, we will setup a CI/CD pipeline to deploy demo app on Azure Kubernetes cluster with Azure DevOps by using a Linux build agent, Docker and Helm. The combination of these technologies will illustrate how you can easily setup a CI/CD pipeline and accelerate our DevOps journey with containers.

Here are the technologies we’ll use.

**Azure DevOps** helps to implement your CI/CD pipelines for any platform, any languages.
**Helm** simplifies and automates more your apps deployments in Kubernetes.

## The DevOps Workflow
This is the DevOps workflow

![Image 1](https://github.com/nasserahmad/AKS-Cluster-With-Demoapp-HelmChart/blob/main/common/arch-png/arch-png.PNG) 

* Devs and Ops commit code change (apps, infrastructure-as-code, etc.) to Azure repos
* Azure build pipeline will build and push both the app as a Docker image and the Helm chart in an Azure Container Registry (ACR)
* Azure release pipeline will deploy the specific Helm chart to an Azure Kubernetes Service (AKS) cluster

## Source control
We will use the existing GitHub repo for CI/CD pipeline. 

* **Files of the app itself**, depending of the programming languages: dsv-devops-test in java returning basic response.
* **Dockerfile file** is a script leveraged by Docker, composed of various instructions and arguments listed successively to automatically perform actions on a base image in order to create a new Docker image by packaging the app.
* **charts/* folder** contains the files defining Helm Chart of the app. Helm Charts helps you define, install and upgrade your app in Kubernetes.

## Create a CI/CD  pipeline
We will now create an Azure build/deploy pipeline for the dsv-devops-test app to be able to both build/push its Docker image and package/push its Helm chart. For that we will need to create a build pipeline definition using the ci-pipeline.yml file. From the menu Pipelines > Builds, follow the steps illustrated below:


* Select Github repo - 
* Choose a template - Configuration as code - YAML
* Rename the pipeline - "dsv-devops-test-CI"
* Select Agent pool - hosted Ubuntu
* Select YAML file path - ci-pipeline.yaml

Then, you need to add some Variables for this build pipeline definition by providing your own values:

* projectName
  - Since we are setting this up for the dsv-devops-test project, just put: desv-devops-test (must be lower case)
* registryLogin
  - You can get it by running this command from az cli: ````az acr credential show -n acr-name --query username````
* registryName
  - You can get it by running this command from az cli: ````az acr show -n acr-name --query name````
* registryPassword
  - You can get it by running this command from az cli: ````az acr credential show -n acr-name --query passwords[0].value````

We could now Save & Queue a new build which will push both the Docker image and the Helm chart in your ACR.

````
List Docker images from your ACR
````
$ az acr repository list -n k8sdevopsacr
````
# List Helm charts from your ACR
````
$ az acr helm list -n k8sdevopsacr
````
# Show details of a specific Helm chart from your ACR
````
$ az acr helm show dsv-devops-test -n k8sdevopsacr
````

Now both the Docker image and the Helm chart could be used for any Kubernetes cluster from anywhere: locally, etc. You will see in the next section how to use them with a release pipeline.

