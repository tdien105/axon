

For extracting human names from a remote json file.
- Human names will be replace with this char: ___
- After replace all human names, it will return a json file which added a new key: "AXON":"true"

# Prerequisite:
- k8s cluster with at least two nodes
- installed gitops (fluxcd) into cluster

# Deploy:
- apply all yaml files inside deploy/k8s folder at this repo
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
- Basically, when there's new commit to master branch. A github action will be called to rebuild the docker image and push to docker registry. The fluxcd (already inside cluster) will monitor that registry, when it saw new tag, it will auto update the axon-api pod accordingly.

# No-downtime:
- To achieving no-downtime deployment, i also apply these rules to our k8s cluster:
  - HPA: auto scaling our pod (min is 2)
  - PDB: make sure there will be at least one pod running
  - Update strategy: Rolling update. So k8s will deploy one by one, so in case there's problem with the code, it wont break the service.
  - PodAntiAffinity: Make sure two pods will not be placed at the same node.

# Limitations:
- In some cases, the lib (spaCy) couldnt extract the names. 
