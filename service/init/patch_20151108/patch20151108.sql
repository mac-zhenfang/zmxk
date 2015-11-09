ALTER TABLE rounds ADD quota int(1) DEFAULT 7;


-- Event History

DROP TABLE IF EXISTS `events_hist`;
CREATE TABLE `events_hist` (
	`id` varchar(36) NOT NULL,
	`name` varchar(255) CHARACTER SET utf8 NOT NULL,
	`eventTypeId` varchar(36),
	`siteId` varchar(36) NOT NULL,
	`seriesId` varchar(36),
	`quota` int,
	`isTeam` boolean  DEFAULT FALSE COMMENT 'check if it is the team series',
	`stage` int, -- Preliminary(0)/contest(1)/semi final(2)/final(3)
	`status` int NOT NULL DEFAULT 0,-- prepare(0) / start (1) / complete (2)
	`eventTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
	`createdTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  	`lastModifiedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  	PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


 -- pending(0) / joined(1) / complete (2)
DROP TABLE IF EXISTS `attendees_hist`;
CREATE TABLE `attendees_hist` (
	`id` varchar(36) NOT NULL,
	`kidId` varchar(36),
	`userId` varchar(36),
	`teamId` varchar(36),
	`eventId` varchar(36) NOT NULL,
	`tagId` varchar(36),
	`score` float,
	`rank` int,
 	`seq` int,
 	`status` int NOT NULL DEFAULT 0,
	`createdTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  	`lastModifiedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  	PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


ALTER TABLE attendees_hist ADD roundId varchar(36);
ALTER TABLE attendees_hist ADD nextRoundId varchar(36);
ALTER TABLE attendees_hist ADD notify_times tinyint default 0;
ALTER TABLE events_hist ADD shortName varchar(32);