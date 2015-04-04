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
