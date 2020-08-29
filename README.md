# axon

prerequisite:
- k8s cluster with at least two nodes
- installed gitops (fluxcd) into cluster

deploy:
- apply all yaml files inside deploy/ folder at this repo

test:
- get the current public domain for this service
```
kubectl get svc/axon-api
---
NAME       TYPE           CLUSTER-IP      EXTERNAL-IP                                                                    PORT(S)        AGE
axon-api   LoadBalancer   172.20.52.248   aef0c8dc6ba724b039cea938cc25397e-1749958889.ap-southeast-1.elb.amazonaws.com   80:31340/TCP   154m

