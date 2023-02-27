#!/bin/sh
#
# This file is baked into the AMI. It is to be run once to init a new instance
# deployed from the AMI `iurisman-wp-ubuntu` built by this project.
# See README.md for details.
#

echo -n "Enter MySQL root password: "
read mysql_root_password
echo -n "Enter MySQL wordpress password: "
read mysql_wp_password

sudo mysql --verbose -u root <<EOF
-- Set password for root
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password by '$mysql_root_password';
-- Create database user to own wordpress db
CREATE USER 'wp_user'@localhost IDENTIFIED BY '$mysql_wp_password';
-- Create wordpress db
CREATE DATABASE wp;
GRANT ALL PRIVILEGES ON wp.* TO 'wp_user'@localhost;
EOF

