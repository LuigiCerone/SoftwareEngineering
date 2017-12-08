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
  id            VARCHAR(30)        NOT NULL
    PRIMARY KEY,
  clusterId     VARCHAR(30)        NOT NULL,
  ir            DOUBLE DEFAULT '0' NOT NULL,
  count         INT DEFAULT '0'    NOT NULL,
  downTime      INT DEFAULT '0'    NULL,
  startUpTime   DATETIME           NOT NULL,
  startDownTime DATETIME           NULL
);

CREATE TRIGGER signalsinit
AFTER INSERT ON robot
FOR EACH ROW
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

CREATE TABLE signals
(
  number    INT                    NOT NULL,
  value     TINYINT(1) DEFAULT '1' NOT NULL,
  timestamp VARCHAR(40)            NOT NULL,
  robotId   VARCHAR(30)            NOT NULL,
  PRIMARY KEY (robotId, number)
);

