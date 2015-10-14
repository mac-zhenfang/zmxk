ALTER TABLE tags ADD level int
ALTER TABLE tags ADD tagGroup varchar(64)
ALTER TABLE eventCreditRules ADD eventGroupLevel int DEFAULT 0

insert tags (id, name, type, tagGroup, level, createdTime, lastModifiedTime)
values ("951d936c-9f11-4648-afbb-b0ee530e31a0", "01", "eventGroup", "A", 1, now(), now());
insert tags (id, name, type, tagGroup, level, createdTime, lastModifiedTime)
values ("951d936c-9f11-4648-afbb-b0ee530e31a1", "02", "eventGroup", "A", 1, now(), now());
insert tags (id, name, type, tagGroup, level, createdTime, lastModifiedTime)
values ("951d936c-9f11-4648-afbb-b0ee530e31a2", "03", "eventGroup", "A", 1, now(), now());
insert tags (id, name, type, tagGroup, level, createdTime, lastModifiedTime)
values ("951d936c-9f11-4648-afbb-b0ee530e31a3", "04", "eventGroup", "A", 1, now(), now());
insert tags (id, name, type, tagGroup, level, createdTime, lastModifiedTime)
values ("951d936c-9f11-4648-afbb-b0ee530e31a4", "05", "eventGroup", "A", 1, now(), now());

insert tags (id, name, type, tagGroup, level,  createdTime, lastModifiedTime)
values ("951d936c-9f11-4648-afbb-b0ee530e31b0", "01", "eventGroup", "B", 2, now(), now());
insert tags (id, name, type, tagGroup, level, createdTime, lastModifiedTime)
values ("951d936c-9f11-4648-afbb-b0ee530e31b1", "02", "eventGroup", "B", 2, now(), now());

insert tags (id, name, type, tagGroup, level, createdTime, lastModifiedTime)
values ("951d936c-9f11-4648-afbb-b0ee530e31c0", "01", "eventGroup", "C", 3, now(), now());

insert tags (id, name, type, tagGroup, level, createdTime, lastModifiedTime)
values ("951d936c-9f11-4648-afbb-b0ee530e31x0", "01", "eventGroup", "X", 10, now(), now());
insert tags (id, name, type, tagGroup, level, createdTime, lastModifiedTime)
values ("951d936c-9f11-4648-afbb-b0ee530e31x1", "02", "eventGroup", "X", 10, now(), now());

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



ALTER TABLE series add eventSerieDefId int COMMENT "event_def"
