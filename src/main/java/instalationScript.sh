#!/bin/bash

#Update System
sudo apt update
sudo apt upgrade

#Java
java -version
if [ $? != 0 ];
	then sudo apt install openjdk-17-jre
fi

#Repository
git clone https://github.com/NetMinder-Enterprise/FireByte-backend
cd FireByte-backend
sudo mvn install #Dependencies
sudo mvn assembly:single #Build
cd target
sudo java -jar login-java-netminder-1.1-jar-with-dependencies.jar