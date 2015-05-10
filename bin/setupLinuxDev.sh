#!/usr/bin/env bash

set -e

if [ $# -ne 2 ]; then
    echo "Usage:"
    echo "setupLinuxDev dbRootUser dbRootPassword"
    exit 1
fi

# install leiningen
echo -e "\e[1minstall latest version of leiningen\e[0m"
mkdir -p ~/tools
wget -O ~/tools/lein.sh https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein
chmod +x ~/tools/lein.sh
if [ ! -f /bin/lein ]
then
    sudo ln -s ~/tools/lein.sh /bin/lein
fi

# install mysql
echo -e "\e[1minstall mysql-server\e[0m"
sudo apt-get install mysql-server

# init database
echo -e "\e[1mset up database\e[0m"
echo "DROP database IF EXISTS puresec_master" | mysql -u$1  -p$2
echo "CREATE database puresec_master" | mysql -u$1 -p$2
echo "GRANT USAGE ON *.* TO 'psec_master'@'localhost'" | mysql -u$1 -p$2
echo "DROP USER 'psec_master'@'localhost'" | mysql -u$1 -p$2
echo "CREATE USER 'psec_master'@'localhost' IDENTIFIED BY 'psec_master';" | mysql -u$1 -p$2
echo "GRANT ALL PRIVILEGES ON puresec_master.* TO 'psec_master'@'localhost';" | mysql -u$1 -p$2
lein ragtime migrate
