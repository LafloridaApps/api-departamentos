#!/bin/bash

# =========================================================
# CONFIGURACIÓN DEL MICROSERVICIO (Actualizado a GHCR)
# =========================================================
NOMBRE_APP="api-departamentos"
PUERTO="8085"
# Cambiamos la ruta de Docker Hub a GitHub Container Registry
IMAGEN_HUB="ghcr.io/lafloridaapps/api-departamentos"
# =========================================================


        echo "Compilando ($NOMBRE_APP) ---"
        ./mvnw clean package -DskipTests
        docker build -t $NOMBRE_APP:local .
        TARGET_IMAGE="$NOMBRE_APP:local"



echo "--- Limpiando contenedor anterior ---"
docker stop ${NOMBRE_APP}-container 2>/dev/null
docker rm ${NOMBRE_APP}-container 2>/dev/null

echo "--- Iniciando contenedor en puerto $PUERTO ---"
docker run \
           --restart always \
           -d -p ${PUERTO}:${PUERTO} \
           --env-file docker.env \
           --network laflorida \
           --add-host=host.docker.internal:host-gateway \
           --name ${NOMBRE_APP}-container \
           $TARGET_IMAGE

# Limpia imágenes antiguas para no llenar el disco del PC de la oficina
docker image prune -f
echo "--- Proceso Terminado ($OPCION) ---"
