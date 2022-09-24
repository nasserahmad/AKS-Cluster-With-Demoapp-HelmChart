# Ingress - SSL

## Step-01: Introduction
- Implement SSL using Lets Encrypt

[![Image]

## Step-02: Install Cert Manager
```t
# Label the nginx-ingress namespace to disable resource validation
kubectl label namespace nginx-ingress cert-manager.io/disable-validation=true

# Add the Jetstack Helm repository
helm repo add jetstack https://charts.jetstack.io

# Update your local Helm chart repository cache
helm repo update

# Install the cert-manager Helm chart
helm install \
  cert-manager jetstack/cert-manager \
  --namespace nginx-ingress \
  --version v1.8.2 \
  --set installCRDs=true

## SAMPLE OUTPUT
$ helm install \
>   cert-manager jetstack/cert-manager \
>   --namespace nginx-ingress \
>   --version v1.8.2 \
>   --set installCRDs=true     
NAME: cert-manager
LAST DEPLOYED: Thu Sep 22 17:15:06 2022
NAMESPACE: nginx-ingress
STATUS: deployed
REVISION: 1
TEST SUITE: None
NOTES:
cert-manager v1.8.2 has been deployed successfully!

In order to begin issuing certificates, you will need to set up a ClusterIssuer
or Issuer resource (for example, by creating a 'letsencrypt-staging' issuer).

More information on the different types of issuers and how to configure them
can be found in our documentation:

https://cert-manager.io/docs/configuration/

For information on how to configure cert-manager to automatically provision
Certificates for Ingress resources, take a look at the `ingress-shim`
documentation:

https://cert-manager.io/docs/usage/ingress/


# Verify Cert Manager pods
kubectl get pods --namespace nginx-ingress

# Verify Cert Manager Services
kubectl get svc --namespace nginx-ingress
```

## Step-06: Review or Create Cluster Issuer Kubernetes Manifest
### Review Cluster Issuer Kubernetes Manifest
- Create or Review Cert Manager Cluster Issuer Kubernetes Manigest
```yaml
apiVersion: cert-manager.io/v1
kind: ClusterIssuer
metadata:
  name: letsencrypt
spec:
  acme:
    # The ACME server URL
    server: https://acme-v02.api.letsencrypt.org/directory
    # Email address used for ACME registration
    email: nase.ahmad1@gmail.com
    # Name of a secret used to store the ACME account private key
    privateKeySecretRef:
      name: letsencrypt
    solvers:
      - http01:
          ingress:
            class: nginx
```

### Deploy Cluster Issuer
```t
# Deploy Cluster Issuer
kubectl apply -f kube-manifests/01-CertManager-ClusterIssuer/cluster-issuer.yml

# List Cluster Issuer
kubectl get clusterissuer

# Describe Cluster Issuer
kubectl describe clusterissuer letsencrypt
```


## Step-07: Review Application DemoApp1,2 K8S Manifests
- 01-DemoApp1-Deployment.yml
- 02-DemoApp1-ClusterIP-Service.yml
- 01-DemoApp2-Deployment.yml
- 02-DemoApp2-ClusterIP-Service.yml

## Step-08: Create or Review Ingress SSL Kubernetes Manifest
- 01-Ingress-SSL.yml

## Step-09: Deploy All Manifests & Verify
- Certificate Request, Generation, Approal and Download and be ready might take from 1 hour to couple of days if we make any mistakes and also fail.
- For me it took, only 5 minutes to get the certificate from **https://letsencrypt.org/**
```t
# Deploy
kubectl apply -R -f kube-manifests/

# Verify Pods
kubectl get pods

# Verify Cert Manager Pod Logs
kubectl get pods -n nginx-ingress
kubectl  logs -f <cert-manager-55d65894c7-sx62f> -n nginx-ingress #Replace Pod name

# Verify SSL Certificates (It should turn to True)
kubectl get certificate
```
```log
stack@Azure:~$ kubectl get certificate
NAME                      READY   SECRET                    AGE
demoapp1-kubekon-secret   True    demoapp1-kubekon-secret   45m
demoapp2-kubekon-secret   True    demoapp2-kubekon-secret   45m
stack@Azure:~$
```

```log
# Sample Success Log
I0922 18:18:27.567910       1 pod.go:59] cert-manager/challenges/http01/selfCheck/http01/ensurePod "msg"="found onisting HTTP01 solver pod" "dnsName"="kubekon.info" "related_resource_kind"="Pod" "related_resource_name"="cm-acme--solver-k97xg" "related_resource_namespace"="dsv-app" "related_resource_version"="v1" "resource_kind"="Challenge" ource_name"="kubekon-info-secrets-rg9kl-800955403-1379115109" "resource_namespace"="dsv-app" "resource_version"="vtype"="HTTP-01"

I0922 18:18:27.568015       1 service.go:43] cert-manager/challenges/http01/selfCheck/http01/ensureService "msg"="d one existing HTTP01 solver Service for challenge resource" "dnsName"="kubekon.info" "related_resource_kind"="Ser" "related_resource_name"="cm-acme-http-solver-8l4x7" "related_resource_namespace"="dsv-app" "related_resource_ver"="v1" "resource_kind"="Challenge" "resource_name"="kubekon-info-secrets-rg9kl-800955403-1379115109" "resource_namce"="dsv-app" "resource_version"="v1" "type"="HTTP-01"

I0922 18:18:27.568112       1 ingress.go:99] cert-manager/challenges/http01/selfCheck/http01/ensureIngress "msg"="d one existing HTTP01 solver ingress" "dnsName"="kubekon.info" "related_resource_kind"="Ingress" "related_resourcee"="cm-acme-http-solver-7mrv2" "related_resource_namespace"="dsv-app" "related_resource_version"="v1" "resource_ki"Challenge" "resource_name"="kubekon-info-secrets-rg9kl-800955403-1379115109" "resource_namespace"="dsv-app" "reso_version"="v1" "type"="HTTP-01"

E0922 18:18:27.585297       1 sync.go:186] cert-manager/challenges "msg"="propagation check failed" "error"="faile perform self check GET request 'http://kubekon.info/.well-known/acme-challenge/tllPe31HzyMMEsPwr-bqBF6vyyqmVFlgLhkjono': Get \"https://kubekon.info:443/.well-known/acme-challenge/tllPe31HzyMMEsPwr-bqBF6vyyqmVFlgLhpzmtkjono\": re error: tls: unrecognized name" "dnsName"="kubekon.info" "resource_kind"="Challenge" "resource_name"="kubekon-infcrets-rg9kl-800955403-1379115109" "resource_namespace"="dsv-app" "resource_version"="v1" "type"="HTTP-01"
```

## Step-10: Access Application
- For me **cert-manager** did not work with the **nginx-ingress** and finally switch the ingress controller to **ingress-nginx** controller maintained by kubernetes and its generated and configured the certificate for my domain name.
```t
# URLs
http://demoapp1.kubekon.info/
http://demoapp1.kubekon.info/
```

## Step-11: Verify nginx-ingress logs for Client IP
```t
# List Pods
kubectl -n nginx-ingress get pods

# Check logs
kubectl -n nginx-ingress logs -f ingress-controller-xxxxxxxxx
```
## Step-12: Clean-Up
```t
# Delete Apps
kubectl delete -R -f kube-manifests/

# Delete Ingress Controller
kubectl delete namespace nginx-ingress
```

## Cert Manager
- https://cert-manager.io/docs/installation/#default-static-install
- https://cert-manager.io/docs/installation/helm/
- https://docs.cert-manager.io/
- https://cert-manager.io/docs/installation/helm/#1-add-the-jetstack-helm-repository
- https://cert-manager.io/docs/configuration/
- https://cert-manager.io/docs/tutorials/acme/ingress/#step-6---configure-a-lets-encrypt-issuer
- https://letsencrypt.org/how-it-works/
