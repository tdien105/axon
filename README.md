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


