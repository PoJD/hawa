create table if not exists roomstate (
  name varchar(100), 
  at timestamp, 
  temperature double);
  
create table if not exists outdoor (
  name varchar(100), 
  at timestamp, 
  reading double);

create table if not exists securityevents (
  type varchar(100),
  source varchar(100),
  at timestamp, 
  filepath varchar(100));
