# DevOps AKS Demo

## Aims - This demo aim to demonstrate below components.

  - Dockerize your application
  - (AKS)Azure Kubernetes Service Cluster Installation and Configuration
  - Ingress installation on AKS cluster
  - Delegate a registerd domain from domain registrar to Azure DNS
  - External DNS installation on AKS cluster for Azure DNS integration with ingress
  - Enable SSL on ingress with LetsEncrypt using cert-manager
  - Create and Install Helm chart for Generic application
  - Azure DevOps CI/CD Pipeline

## Pre-requisites - Below are the Pre-requisites required to implement this demo.

  - Azure Subscription to use azure services
  - Registerd Domain Name
  - Azure DevOps Subscription to create CI/CD pipelines
  - Docker to build, push and run container images
  - Helm to install, list and upgrade k8s helm-charts

## 01-Dockerize your application

#### Docker best practices to ensure security and performance.

  - Dont use docker image based on a full operating system image,extra binaries,large image size & longer download,security vulnerabilities
  - Use the smallest possible base image that fits your needs. No need full OS & eliminate extra binaries to improve performance.
  - Be very specific about the images and tags, to be more precise you can use image SHA256. 
  - Use explicit and deterministic Docker base image tags.
  - Build & Install only what you need in production in the docker image
  - Don’t run containers as root create a user
  - Keeping unnecessary files out of your Java container images.
  - Find and fix security vulnerabilities in your container Docker image. Can use different tools like snyk
  - Use multi-stage dockerfile to build custom image rather than single-stage dockerfile to install binaries, and build your code and defining multiple docker layers.  
  
##### JAVA Multistage dockerfile

```t
FROM maven:3.8.3-jdk-8-slim AS java-build
RUN mkdir /javaproject
COPY ./src /javaproject
COPY pom.xml /javaproject
WORKDIR /javaproject
##cache maven remote dependencies of .m2 repository to avoid downloading in future build to improve performance.
RUN --mount=type=cache,target=/root/.m2 mvn clean package

FROM adoptopenjdk/openjdk8:x86_64-alpine-jre8u345-b01 AS java-publish
ARG ORG
ENV ORG_NAME ${ORG}
RUN apk add dumb-init
RUN mkdir /java-app
#Don’t run containers as root create a user
RUN addgroup --system javauser && adduser -S -s /bin/false -G javauser javauser
COPY --from=java-build /javaproject/target/dsv-0.0.1-SNAPSHOT.jar /java-app/application.jar
WORKDIR /java-app
RUN chown -R javauser:javauser /java-app
USER javauser
#Properly handle events to safely terminate a Java Docker web application
CMD "dumb-init" "java" "-jar" "application.jar" 
```

- Build the docker file with 'docker build' command and push to the docker registry(dockerHub, ACR, Private Registry) with 'docker push' commands.
 
##### Dockerfile Recommended
```t
FROM adoptopenjdk/openjdk8:x86_64-alpine-jre8u345-b01
RUN apk add dumb-init
RUN mkdir /java-app
RUN addgroup --system javauser && adduser -S -s /bin/false -G javauser javauser
COPY ./target/dsv-0.0.1-SNAPSHOT.jar /java-app/application.jar
WORKDIR /java-app
RUN chown -R javauser:javauser /java-app
USER javauser
CMD "dumb-init" "java" "-jar" "application.jar" 
```

## 02-(AKS)Azure Kubernetes Service Cluster Installation and Configuration

- For AKS Cluster installation and configuration PFB **Reference Documentation**.
  - [**Reference Documentation**](https://github.com/nasserahmad/AKS-Cluster-With-Demoapp-HelmChart/blob/main/aks-cluster/01-Create-AKS-Cluster/README.md)


## 03-Ingress installation on AKS cluster

- For Ingress installation on AKS cluster PFB **Reference Documentation**.
  - [**Reference Documentation ingress-nginx**](https://github.com/nasserahmad/AKS-Cluster-With-Demoapp-HelmChart/blob/main/aks-cluster/02-Ingress/ingress-nginx-README.md)
  - [**Reference Documentation nginx-ingress**](https://github.com/nasserahmad/AKS-Cluster-With-Demoapp-HelmChart/blob/main/aks-cluster/02-Ingress/nginx-ingress-README.md)

## 04-Delegate a registerd domain from domain registrar to Azure DNS

- To delegate a domain 'kubekon.info' from GoDaddy to azure DNS PFB **Reference Documentation**.
  - [**Reference Documentation**](https://github.com/nasserahmad/AKS-Cluster-With-Demoapp-HelmChart/tree/main/aks-cluster/03-Delegate-Domain-from-GoDaddy-to-Azure-DNS)


## 05-External DNS installation on AKS cluster for Azure DNS integration with ingress

- To install external DNS on AKS cluster for Azure DNS integration with ingress PFB **Reference Documentation**.
  - [**Reference Documentation**](https://github.com/nasserahmad/AKS-Cluster-With-Demoapp-HelmChart/blob/main/aks-cluster/04-ExternalDNS-for-AzureDNS-on-AKS/README.md)

## 06-Enabling SSL on ingress with LetsEncrypt using cert-manager

  - To enable SSL on ingress with LetsEncrypt Issuer and cert-manager PFB **Reference Documentation**.
  
  - [**Reference Documentation ingress-nginx**](https://github.com/nasserahmad/AKS-Cluster-With-Demoapp-HelmChart/blob/main/aks-cluster/05-Ingress-SSL-with-LetsEncrypt/ingress-nginx-README.md)
  
  - [**Reference Documentation nginx-ingress**](https://github.com/nasserahmad/AKS-Cluster-With-Demoapp-HelmChart/blob/main/aks-cluster/05-Ingress-SSL-with-LetsEncrypt/nginx-ingress-README.md)

## 07-Create and Install Helm chart for Generic application

  - To create and install Helm chart for your application PFB **Reference Documentation**.
  - [**Reference Documentation**](https://github.com/nasserahmad/AKS-Cluster-With-Demoapp-HelmChart/blob/main/dsv-devops-test/charts/README.md) 

## 08-Azure DevOps CI/CD Pipeline
  - To setup a CI/CD pipeline to deploy your apps on a Kubernetes cluster with Azure DevOps PFB **Reference Documentation**.
  - [**Reference Documentation**](https://github.com/nasserahmad/AKS-Cluster-With-Demoapp-HelmChart/blob/main/dsv-devops-test/README.md) 