create table if not exists roomstate (
  name varchar(100), 
  at timestamp, 
  temperature double);
  
create table if not exists outdoor (
  name varchar(100), 
  at timestamp, 
  reading double);
