az acr login --name prporegistry
docker build -t prporegistry.azurecr.io/account-service:latest .
docker push prporegistry.azurecr.io/account-service:latest