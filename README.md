# axon

For extracting human names from a remote json file

# Prerequisite:
- k8s cluster with at least two nodes
- installed gitops (fluxcd) into cluster

# Deploy:
- apply all yaml files inside deploy/ folder at this repo
- wait untill all pods up

# Test:
- get the current public domain for this service
```
kubectl get svc/axon-api
---
NAME       TYPE           CLUSTER-IP      EXTERNAL-IP                                                                    PORT(S)        AGE
axon-api   LoadBalancer   172.20.52.248   aef0c8dc6ba724b039cea938cc25397e-1749958889.ap-southeast-1.elb.amazonaws.com   80:31340/TCP   154m
```
- now we can call our api with the following syntax:
  - type: application/json
  - key: json_link
  - value: your json link that you want to transform

```
curl -d '{"json_link": "http://therecord.co/feed.json"}' -H "Content-Type: application/json" -X POST aef0c8dc6ba724b039cea938cc25397e-1749958889.ap-southeast-1.elb.amazonaws.com
```

# Tools:
- https://github.com/jgontrum/spacy-api-docker
  - This lib is for extracting human names from a text

# CI/CD flow:
- I used GitOps flow using FluxCD (https://fluxcd.io).
![gitops](flux.png)
