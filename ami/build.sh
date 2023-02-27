#!/bin/sh
#
# This is run by packer while bulding the AMI.
#
echo "Installing Apache 2 web server ..."
sudo apt-get update
sudo apt install -y apache2

echo "Installing PHP runtime and PHP MySQL connector ..."
sudo apt install -y php libapache2-mod-php php-mysql

echo "Installing MySQL server"
sudo apt install -y mysql-server

echo "Installing Wordpress"
cd /tmp
wget https://wordpress.org/latest.tar.gz
tar -xvf latest.tar.gz
sudo mv wordpress/ /var/www/html
sudo systemctl restart apache2

echo "Installing Certbot"
sudo add-apt-repository ppa:certbot/certbot
sudo apt install -y certbot python3-certbot-apache

