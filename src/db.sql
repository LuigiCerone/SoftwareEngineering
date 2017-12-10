create table cluster
(
  id varchar(30) not null
    primary key,
  zoneId varchar(30) not null,
  ir double default '0' not null,
  count int default '0' not null,
  downTime bigint null,
  startUpTime datetime not null,
  startDownTime datetime null
)
;

create table read_data
(
  id int auto_increment
    primary key,
  robotId varchar(20) not null,
  clusterId varchar(20) not null,
  zoneId varchar(20) not null,
  signals int(1) not null,
  value int(6) not null,
  timestamp varchar(50) not null
)
;

create table robot
(
  id varchar(30) not null
    primary key,
  clusterId varchar(30) not null,
  ir double default '0' not null,
  count int default '0' not null,
  downTime bigint default '0' null,
  startUpTime datetime not null,
  startDownTime datetime null
)
;

create trigger signalsinit
after INSERT on robot
for each row
  BEGIN
    DECLARE now VARCHAR(40);
    SET now = NOW();
    INSERT INTO signals (number, value, timestamp, robotId) VALUES (1, 1, now, NEW.id);
    INSERT INTO signals (number, value, timestamp, robotId) VALUES (2, 1, now, NEW.id);
    INSERT INTO signals (number, value, timestamp, robotId) VALUES (3, 1, now, NEW.id);
    INSERT INTO signals (number, value, timestamp, robotId) VALUES (4, 1, now, NEW.id);
    INSERT INTO signals (number, value, timestamp, robotId) VALUES (5, 1, now, NEW.id);
    INSERT INTO signals (number, value, timestamp, robotId) VALUES (6, 1, now, NEW.id);
    INSERT INTO signals (number, value, timestamp, robotId) VALUES (7, 1, now, NEW.id);
  END;

create table signals
(
  number int not null,
  value tinyint(1) default '1' not null,
  timestamp varchar(40) not null,
  robotId varchar(30) not null,
  primary key (robotId, number)
)
;

