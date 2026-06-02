#!/bin/bash

# Arrancar MongoDB en background (--fork requiere --logpath)
mongod --fork --logpath /var/log/mongod.log

# Esperar a que MongoDB esté listo para aceptar conexiones
until mongosh --eval "db.runCommand({ ping: 1 })" &>/dev/null; do sleep 1; done

# Arrancar el microservicio Spring Boot
java -jar /app/msProofSkyWatch-0.0.1-SNAPSHOT.jar