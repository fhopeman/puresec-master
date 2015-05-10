#!/usr/bin/env bash

set -e

if [ $# -ne 2 ]; then
    echo "Usage:"
    echo "setupServer dbRootUser dbRootPassword"
    exit 1
fi

# install mysql
echo -e "\e[1minstall mysql-server ..\e[0m"
sudo apt-get install mysql-server

# init database
echo -e "\e[1mset up database ..\e[0m"
echo "DROP database IF EXISTS puresec_master" | mysql -u$1  -p$2
echo "CREATE database puresec_master" | mysql -u$1 -p$2
echo "GRANT USAGE ON *.* TO 'psec_master'@'localhost'" | mysql -u$1 -p$2
echo "DROP USER 'psec_master'@'localhost'" | mysql -u$1 -p$2
echo "CREATE USER 'psec_master'@'localhost' IDENTIFIED BY 'psec_master';" | mysql -u$1 -p$2
echo "GRANT ALL PRIVILEGES ON puresec_master.* TO 'psec_master'@'localhost';" | mysql -u$1 -p$2

# create tables
echo -e "\e[1mcreate tables\e[0m .."
echo "USE puresec_master; CREATE TABLE detector_registry (id INTEGER PRIMARY KEY AUTO_INCREMENT, detector_name VARCHAR(30) NOT NULL UNIQUE, detector_description VARCHAR(256) NOT NULL, url VARCHAR(256) NOT NULL);" | mysql -u$1 -p$2
echo "USE puresec_master; CREATE TABLE handler_registry (id INTEGER PRIMARY KEY AUTO_INCREMENT, handler_name VARCHAR(30) NOT NULL UNIQUE, handler_description VARCHAR(256) NOT NULL, url VARCHAR(256) NOT NULL);" | mysql -u$1 -p$2
echo "USE puresec_master; CREATE TABLE detector_handler (detector_id INTEGER, handler_id INTEGER, FOREIGN KEY (detector_id) REFERENCES detector_registry(id), FOREIGN KEY (handler_id) REFERENCES handler_registry(id), CONSTRAINT pk_detector_handler PRIMARY KEY (detector_id, handler_id));" | mysql -u$1 -p$2
