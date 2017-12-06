CREATE TABLE SEProject.read_data
(
    id int(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    robotId varchar(20) NOT NULL,
    clusterId varchar(20) NOT NULL,
    zoneId varchar(20) NOT NULL,
    signals int(1) NOT NULL,
    value int(6) NOT NULL,
    timestamp datetime NOT NULL
);