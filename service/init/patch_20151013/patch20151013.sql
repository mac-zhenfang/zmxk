--ALTER TABLE tags ADD level int
--ALTER TABLE tags ADD tagGroup varchar(64)

--insert tags (id, name, type, tagGroup, level, createdTime, lastModifiedTime) values ("951d936c-9f11-4648-afbb-b0ee530e31a0", "01", "eventGroup", "A", 1, now(), now());
--insert tags (id, name, type, tagGroup, level, createdTime, lastModifiedTime) values ("951d936c-9f11-4648-afbb-b0ee530e31a1", "02", "eventGroup", "A", 1, now(), now());
--insert tags (id, name, type, tagGroup, level, createdTime, lastModifiedTime) values ("951d936c-9f11-4648-afbb-b0ee530e31a2", "03", "eventGroup", "A", 1, now(), now());
--insert tags (id, name, type, tagGroup, level, createdTime, lastModifiedTime) values ("951d936c-9f11-4648-afbb-b0ee530e31a3", "04", "eventGroup", "A", 1, now(), now());
--insert tags (id, name, type, tagGroup, level, createdTime, lastModifiedTime) values ("951d936c-9f11-4648-afbb-b0ee530e31a4", "05", "eventGroup", "A", 1, now(), now());

--insert tags (id, name, type, tagGroup, level,  createdTime, lastModifiedTime)values ("951d936c-9f11-4648-afbb-b0ee530e31b0", "01", "eventGroup", "B", 2, now(), now());
--insert tags (id, name, type, tagGroup, level, createdTime, lastModifiedTime) values ("951d936c-9f11-4648-afbb-b0ee530e31b1", "02", "eventGroup", "B", 2, now(), now());

--insert tags (id, name, type, tagGroup, level, createdTime, lastModifiedTime) values ("951d936c-9f11-4648-afbb-b0ee530e31c0", "01", "eventGroup", "C", 3, now(), now());

--insert tags (id, name, type, tagGroup, level, createdTime, lastModifiedTime) values ("951d936c-9f11-4648-afbb-b0ee530e31x0", "01", "eventGroup", "X", 10, now(), now());
--insert tags (id, name, type, tagGroup, level, createdTime, lastModifiedTime) values ("951d936c-9f11-4648-afbb-b0ee530e31x1", "02", "eventGroup", "X", 10, now(), now());


ALTER TABLE eventCreditRules ADD roundId varchar(32)

DROP TABLE IF EXISTS `rounds`;
CREATE TABLE `rounds` (
	`id` varchar(36) NOT NULL,
	`level` int,
	`levelName` varchar(36),
	`shortName` varchar(36) NOT NULL,
	`createdTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  	`lastModifiedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  	PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert rounds (id, level, levelName, shortName, createdTime, lastModifiedTime) values ("951d936c-9f11-4648-afbb-b0ee530e31a1", 1, "A", "01", now(), now());
insert rounds (id, level, levelName, shortName, createdTime, lastModifiedTime) values ("951d936c-9f11-4648-afbb-b0ee530e31a2", 1, "A", "02", now(), now());
insert rounds (id, level, levelName, shortName, createdTime, lastModifiedTime) values ("951d936c-9f11-4648-afbb-b0ee530e31a3", 1, "A", "03", now(), now());
insert rounds (id, level, levelName, shortName, createdTime, lastModifiedTime) values ("951d936c-9f11-4648-afbb-b0ee530e31a4", 1, "A", "04", now(), now());
insert rounds (id, level, levelName, shortName, createdTime, lastModifiedTime) values ("951d936c-9f11-4648-afbb-b0ee530e31a5", 1, "A", "05", now(), now());
insert rounds (id, level, levelName, shortName, createdTime, lastModifiedTime) values ("951d936c-9f11-4648-afbb-b0ee530e31a6", 1, "A", "06", now(), now());
insert rounds (id, level, levelName, shortName, createdTime, lastModifiedTime) values ("951d936c-9f11-4648-afbb-b0ee530e31a7", 1, "A", "07", now(), now());
insert rounds (id, level, levelName, shortName, createdTime, lastModifiedTime) values ("951d936c-9f11-4648-afbb-b0ee530e31b1", 2, "B", "01", now(), now());
insert rounds (id, level, levelName, shortName, createdTime, lastModifiedTime) values ("951d936c-9f11-4648-afbb-b0ee530e31b2", 2, "B", "02", now(), now());
insert rounds (id, level, levelName, shortName, createdTime, lastModifiedTime) values ("951d936c-9f11-4648-afbb-b0ee530e31b3", 2, "B", "03", now(), now());
insert rounds (id, level, levelName, shortName, createdTime, lastModifiedTime) values ("951d936c-9f11-4648-afbb-b0ee530e31c1", 3, "C", "01", now(), now());
insert rounds (id, level, levelName, shortName, createdTime, lastModifiedTime) values ("951d936c-9f11-4648-afbb-b0ee530e31c2", 3, "C", "02", now(), now());
insert rounds (id, level, levelName, shortName, createdTime, lastModifiedTime) values ("951d936c-9f11-4648-afbb-b0ee530e31x1", 10, "X", "01", now(), now());
insert rounds (id, level, levelName, shortName, createdTime, lastModifiedTime) values ("951d936c-9f11-4648-afbb-b0ee530e31x2", 10, "X", "02", now(), now());
insert rounds (id, level, levelName, shortName, createdTime, lastModifiedTime) values ("951d936c-9f11-4648-afbb-b0ee530e31x3", 10, "X", "03", now(), now());

ALTER TABLE attendees ADD roundId varchar(36);
ALTER TABLE eventCreditRules ADD roundLevel int;
DROP TABLE IF EXISTS `event_serie_def`;
CREATE TABLE `event_serie_def` (
	`id` int(4) NOT NULL AUTO_INCREMENT,
	`name` varchar(128) CHARACTER SET utf8 NOT NULL,
	`stages` int COMMENT "3",
	`createdTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  	`lastModifiedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  	PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert event_serie_def(name, stages,createdTime,lastModifiedTime) values ("三阶段淘汰赛", 3, now(), now());

DROP TABLE IF EXISTS `event_def`;
CREATE TABLE `event_def` (
	`id` int(4) NOT NULL AUTO_INCREMENT,
	`name` varchar(128) CHARACTER SET utf8 NOT NULL,
	`shortName` varchar(128) CHARACTER SET utf8 NOT NULL,
	`stage` int COMMENT "1,2,3",
	`eventSerieDefId` int(4) COMMENT 'event_serie_def'
	`createdTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  	`lastModifiedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  	PRIMARY KEY (`id`),
	UNIQUE KEY `def_stage` (`stage`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert event_def(name, shortName, stage, eventSerieDefId, createdTime,lastModifiedTime) values ("预赛","A", 1, 1, now(), now());
insert event_def(name, shortName, stage, eventSerieDefId, createdTime,lastModifiedTime) values ("复赛","B", 2, 1, now(), now());
insert event_def(name, shortName, stage, eventSerieDefId, createdTime,lastModifiedTime) values ("决赛","C", 3, 1, now(), now());



ALTER TABLE eventTypes add eventSerieDefId int COMMENT "event_def"
