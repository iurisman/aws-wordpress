#! /bin/bash
#
# Backup this wordpress installation to S3
#
set -x

if [[ $# -ne 4 ]]; then
  me=$(basename $0)
  echo "Usage: $me <wordpress-root-dir> <mysql-username> <mysql-password> <s3-bucket-uri>"
  exit 2
fi
wp_root=$1nn
mysql_user=$2
mysql_passwrd=$3
s3_bucket_uri=$4

# Error trap handler. As an example, sends an email notification.
# $1 == the line number that sent the ERR signal.
on_error() {
  aws ses send-email --from <your-email> --to <your-email> --subject "Backup failed for $s3_bucket_uri" --text "Error at line $1"
  exit 7
}
# Catch any error by trapping the ERR signal.
trap 'on_error $LINENO' ERR

# Site backup
tempdir=$(mktemp -d)
tar -cvf $tempdir/wp_root.tar $wp_root
# MySql backup
mysqldump -u "$mysql_user" --password="$mysql_passwrd" --all-databases > $tempdir/mysqldump.sql
# Upload to S3
timestamp=$(date +%F_%T)
zip -j $tempdir/site-backup-$timestamp.zip $tempdir/wp_root.tar $tempdir/mysqldump.sql
aws s3 cp $tempdir/site-backup-$timestamp.zip $s3_bucket_uri/site-backup-$timestamp.zip
rm -rf $tempdir