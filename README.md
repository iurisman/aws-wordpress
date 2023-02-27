# Run Your Own Instance of Wordpress on AWS.

#### This repository contains all components required to deploy and run a production-ready instance of Wordpress on AWS

## 1. Building the Amazon Machine Image (AMI).
* [Install](https://developer.hashicorp.com/packer/tutorials/aws-get-started/get-started-install-cli) Hashicorp Packer.



## 2. Initializing a New Wordpress Server
* Launch an EC2 instance from AMI `iurisman-wp-ubuntu` built in step 1. Be sure to use an SSH keypair and to choose 
a security group that allows SSH, HTTP, and HTTPS inbound traffic and all outbound traffic. 

* SSH onto the instance with
```shell
$ ssh -i /path/to/private/key ubuntu@<public-ip-address>
```

* Setup the database schema by running
```shell
$ bin/schema.sh
```
Since this is a new MySQL instance, you will be prompted for the database's root password. The script will also 
create a separate database user `wp_user` who will own the `wp` schema containibng the Wordpress related tables. 
You will be prompted for the password for `wp_user` as well.

* Update the Apache configuration file `/etc/apache2/sites-available/000-default.conf` to set server root to point
to the Wordpress intallation:
```text
    ...
    DocumentRoot /var/www/html/wordpress
    ...
```

* Reload Apache to enable the new config:
```shell
$ sudo systemctl restart apache2
```

* Point your browser to the EC2 instance at `http://<public-ip-address>`. This will bring the Wordpress setup dialog:
![Wordpress Setup](images/wp-setup.png)

## 3. Optional Post-Initialization Steps
### 3.1. Periodic Status Check
### 3.2. Periodic Backups
