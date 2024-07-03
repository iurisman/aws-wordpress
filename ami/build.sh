#!/bin/sh
#
# This is run by packer while building the AMI.
#
set -x
sudo apt update

echo "Installing Apache 2 web server ..."
sudo apt install -y apache2
# Apache should run as os user ubuntu in order to access wordpress files.
sudo sed -i 's/www-data/ubuntu/g' /etc/apache2/envvars

echo "Installing PHP runtime and PHP MySQL connector ..."
sudo apt install -y php libapache2-mod-php php-mysql

echo "Installing MySQL server"
sudo apt install -y mysql-server

echo "Installing AWS CLI"
sudo apt install -y awscli

echo "Installing Wordpress"
cd /tmp
wget https://wordpress.org/latest.tar.gz
tar -xvf latest.tar.gz
sudo mv wordpress/ /var/www/html
sudo sed -i 's|/var/www/html|/var/www/html/wordpress|g' /etc/apache2/sites-available/000-default.conf
sudo systemctl restart apache2

echo "Installing zip utils"
sudo apt install -y zip

echo "Installing Certbot"
sudo snap install --classic certbot
sudo apt install -y certbot python3-certbot-apache

echo "Setting up swapfile"
sudo dd if=/dev/zero of=/swapfile bs=64M count=32
sudo chmod 600 /swapfile
sudo mkswap /swapfile
sudo swapon /swapfile
echo '/swapfile none swap sw 0 0' | sudo tee -a /etc/fstab