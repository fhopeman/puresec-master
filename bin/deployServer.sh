#!/usr/bin/env bash

set -e

if [ $# -ne 3 ]; then
    echo "Usage:"
    echo "deployServer serverUserName serverAddress serverDestination"
    exit 1
fi

echo -e "\e[1mbuild uberjar ..\e[0m"
lein do clean
lein uberjar

echo -e "\e[1mtransfer jar to server ..\e[0m"
scp target/puresec-master.jar $1@$2:$3

echo -e "\e[1mstart server ..\e[0m"
echo "run application with:"
echo "java -Xms64m -Xmx64m -Dprod=true -jar puresec-master.jar"
