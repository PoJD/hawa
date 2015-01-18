create table if not exists roomstate (
  name varchar(100) not null, 
  at timestamp not null, 
  temperature double);

create table if not exists outdoor (
  name varchar(100) not null, 
  at timestamp not null, 
  reading double);

create table if not exists securityevents (
  type varchar(100) not null,
  source varchar(100) not null,
  at timestamp not null, 
  filepath varchar(100),
  detail varchar(100));
