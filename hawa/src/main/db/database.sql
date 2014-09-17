create database hawa;
grant usage on *.* to 'superuser'@'%' identified by 'superStrong123Password';
grant usage on *.* to 'superuser'@'rpi' identified by 'superStrong123Password';
grant all privileges on hawa.* to 'superuser'@'%';
