#!/bin/bash

#Update System
echo "\033[0;34mAtualizando Sistema..."
sudo apt update -y
sudo apt upgrade -y

#Create Containers
#MySQL
echo "\033[0;34mConfigurando MySQL Container"
mkdir sql
cd sql
wget "https://raw.githubusercontent.com/NetMinder-Enterprise/FireByte-Backend/main/src/main/java/entities/Script%20DB.sql"
cd ..
touch Dockerfile
wget "
docker build -t firebytedb .
docker run -d --name localDB -p 3306:3306 firebytedb
#Java
