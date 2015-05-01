#!/usr/bin/env bash

set -e

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
echo "create database if not exists puresec_master_clojure" | mysql -u$1 -p$2
echo "GRANT ALL PRIVILEGES ON puresec_master_clojure.* TO 'psec_master_cl'@'localhost';" | mysql -u$1 -p$2
lein ragtime migrate

