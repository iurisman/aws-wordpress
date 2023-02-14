#! /bin/bash
#
# Backup this wordpress installation to S3
#
set -x

if [[ $# -ne 4 ]]; then
  me=$(basename $0)
  echo "Usage: $me <wordpress-root-dir> <mysql-username> <mysql-password> <s3-bucket-url>"
  exit 2
fi
wp_root=$1
mysql_user=$2
mysql_passwrd=$3
s3_bucket_uri=$4

# Site backup
tempdir=$(mktemp -d)
tar -cvf $tempdir/wp_root.tar $wp_root
# MySql backup
mysqldump -u "$mysql_user" --password="$mysql_passwrd" --all-databases > $tempdir/mysqldump.sql
# Upload to S3
timestamp=$(date +%F_%T)
zip $tempdir/site-backup-$timestamp.zip $tempdir/wp_root.tar $tempdir/mysqldump.sql
aws s3 cp $tempdir/site-backup-$timestamp.zip $s3_bucket_uri/site-backup-$timestamp.zip