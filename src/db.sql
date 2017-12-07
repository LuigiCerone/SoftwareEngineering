CREATE DATABASE SEProject;
USE SEProject;

CREATE TABLE cluster
(
  id     VARCHAR(30)        NOT NULL
    PRIMARY KEY,
  zoneId VARCHAR(30)        NOT NULL,
  ir     DOUBLE DEFAULT '0' NOT NULL
);

CREATE TABLE read_data
(
  id        INT AUTO_INCREMENT
    PRIMARY KEY,
  robotId   VARCHAR(20) NOT NULL,
  clusterId VARCHAR(20) NOT NULL,
  zoneId    VARCHAR(20) NOT NULL,
  signals   INT(1)      NOT NULL,
  value     INT(6)      NOT NULL,
  timestamp VARCHAR(50) NOT NULL
);

CREATE TABLE robot
(
  id        VARCHAR(30)        NOT NULL
    PRIMARY KEY,
  clusterId VARCHAR(30)        NOT NULL,
  ir        DOUBLE DEFAULT '0' NOT NULL,
  count     INT DEFAULT '0'    NOT NULL,
  downTime  INT DEFAULT '0'    NULL
);

CREATE TRIGGER signalsinit
AFTER INSERT ON robot
FOR EACH ROW
  BEGIN
    DECLARE now VARCHAR(40);
    SET now = NOW();
    INSERT INTO signals (id, number, value, timestamp, robotId) VALUES (NULL, 1, 1, now, NEW.id);
    INSERT INTO signals (id, number, value, timestamp, robotId) VALUES (NULL, 2, 1, now, NEW.id);
    INSERT INTO signals (id, number, value, timestamp, robotId) VALUES (NULL, 3, 1, now, NEW.id);
    INSERT INTO signals (id, number, value, timestamp, robotId) VALUES (NULL, 4, 1, now, NEW.id);
    INSERT INTO signals (id, number, value, timestamp, robotId) VALUES (NULL, 5, 1, now, NEW.id);
    INSERT INTO signals (id, number, value, timestamp, robotId) VALUES (NULL, 6, 1, now, NEW.id);
    INSERT INTO signals (id, number, value, timestamp, robotId) VALUES (NULL, 7, 1, now, NEW.id);
  END;

CREATE TABLE signals
(
  id        INT AUTO_INCREMENT
    PRIMARY KEY,
  number    INT                    NOT NULL,
  value     TINYINT(1) DEFAULT '1' NOT NULL,
  timestamp VARCHAR(40)            NOT NULL,
  robotId   VARCHAR(30)            NOT NULL
);

L