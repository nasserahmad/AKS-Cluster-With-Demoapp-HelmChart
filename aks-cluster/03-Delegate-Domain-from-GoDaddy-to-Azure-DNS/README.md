# Delegate Domain to Azure DNS

## Step-01: Introduction
- Understand about
  - Domain Registrar i.e GoDaddy in our case
  - DNS Zones
- Learn to delegate a domain from GoDaddy to Azure DNS by creating DNS Zones in Azure Cloud 

## Step-02: DNS Zones - Create DNS Zone
- Go to Service -> **DNS Zones**
- **Subscription:** (You need to have a paid subscription for this)
- **Resource Group:** dns-zones
- **Name:** kubekon.info
- **Resource Group Location:** East US
- Click on **Review + Create**

## Step-03: Make a note of Azure Nameservers
- Go to Services -> **DNS Zones** -> **kubekon.info**
- Make a note of Nameservers
```t
ns1-34.azure-dns.com.
ns2-34.azure-dns.net.
ns3-34.azure-dns.org.
ns4-34.azure-dns.info.
```

## Step-04: Update Nameservers at your Domain provider (Mine is GoDaddy)
- **Verify before updation**
```t
nslookup -type=SOA kubekon.info
nslookup -type=NS kubekon.info
```
- Go to GoDaddy (This is my Domain Provider)
- Go to DNS -> Manage Zones -> Registered Domains -> kubekon.info
- Click on **Add or edit name servers**
- Update Azure Name servers here and click on **Update**
- Click on **Hosted Zones**
- Delete the hosted zone with name **kubekon.info**
- **Verify after updation**
```t
nslookup -type=SOA kubekon.info 8.8.8.8
nslookup -type=NS kubekon.info 8.8.8.8
```
