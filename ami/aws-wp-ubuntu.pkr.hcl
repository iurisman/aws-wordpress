/*****************************************************************************
Create an Amazon Machine Image with the latest Wordpress and all supporting
software. The AWS credentials must be provided in the environment the standard
way expected by the AWS clients.
*****************************************************************************/

packer {
  required_plugins {
    amazon = {
      version = ">= 0.0.2"
      source  = "github.com/hashicorp/amazon"
    }
  }
}

source "amazon-ebs" "ubuntu" {
  instance_type = "t2.micro"
  // Change this if you want a different name for your AMI
  ami_name      = "wordpress-ubuntu"
  ami_description = "https://github.com/iurisman/aws-wordpress"
  // change this if you want the AMI to be created in a different region.
  region        = "us-east-1"
  source_ami_filter {
    filters = {
      name                = "ubuntu/images/hvm-ssd/ubuntu-jammy-22.04-amd64-server-*"
      root-device-type    = "ebs"
      virtualization-type = "hvm"
    }
    most_recent = true
    owners      = ["099720109477"]
  }
  ssh_username = "ubuntu"
}

build {
  //name = "learn-packer"
  sources = [
    "source.amazon-ebs.ubuntu"
  ]
  provisioner "shell" {
    script = "build.sh"
  }
  provisioner "file" {
    source = "bin"
    destination = "~/bin"
  }
  provisioner "shell" {
    inline = [
    "chmod 700 bin/*"
    ]
  }
}
