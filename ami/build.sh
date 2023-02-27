#!/bin/sh

#
mysql_root_password=changeme
#
mysql_wp_password=changeme

echo "Installing Apache 2 web server ..."
sudo apt-get update
sudo apt install -y apache2

echo "Installing php runtime and php mysql connector ..."
sudo apt install -y php libapache2-mod-php php-mysql

echo "Installing MySQL server"
sudo apt install -y mysql-server

echo "Setting up MySQL database"
sudo mysql -u root <<EOF
-- Set password for root
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password by '$mysql_root_password';
-- Create database user to own wordpress db
CREATE USER 'wp_user'@localhost IDENTIFIED BY '$mysql_wp_password';
-- Create wordpress db
CREATE DATABASE wp;
GRANT ALL PRIVILEGES ON wp.* TO 'wp_user'@localhost;
EOF

echo "Installing Download wordpress"
cd /tmp
wget https://wordpress.org/latest.tar.gz
tar -xvf latest.tar.gz
sudo mv wordpress/ /var/www/html
sudo systemctl restart apache2

echo "Installing Certbot"
sudo add-apt-repository ppa:certbot/certbot
sudo apt install -y certbot python3-certbot-apache

echo "*** Remember to run `sudo certbot --apache` to install SSL certificate to Apache ***"
# sudo certbot --apache
