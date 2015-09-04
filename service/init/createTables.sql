-- pending (0) / active (1) / deleted (2)
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
	`id` varchar(36) NOT NULL,
	`name` varchar(255) CHARACTER SET utf8 NOT NULL,
	`password` varchar(255) CHARACTER SET utf8,
	`location` varchar(36) CHARACTER SET utf8 NOT NULL,
	`mobileNum` varchar(255) NOT NULL,
	`wcId` varchar(36),
	`credit` int NOT NULL DEFAULT 0,
  	`goldenMedal` int NOT NULL DEFAULT 0,
  	`silverMedal` int NOT NULL DEFAULT 0,
  	`bronzeMedal` int NOT NULL DEFAULT 0,
	`status` int NOT NULL DEFAULT 0, 
	`roleId` varchar (36) NOT NULL,
	`siteId`  varchar (36),
	`createdTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
	`lastModifiedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`),
	UNIQUE KEY `users_mobileNum` (`mobileNum`),
	UNIQUE KEY `users_wcId` (`wcId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `kids`;
CREATE TABLE `kids` (
	`id` varchar(36) NOT NULL,
	`name` varchar(255) CHARACTER SET utf8 NOT NULL,
	`schoolType` int,
	`schoolName` varchar(255) CHARACTER SET utf8,
	`userId` varchar(36) NOT NULL,
	`teamId` varchar(36),
	`createdTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  	`lastModifiedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  	PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `sites`;
CREATE TABLE `sites` (
	`id` varchar(36) NOT NULL,
	`name` varchar(255) CHARACTER SET utf8 NOT NULL,
	`location` varchar(1024) CHARACTER SET utf8 NOT NULL,
	`createdTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  	`lastModifiedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  	PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `events`;
CREATE TABLE `events` (
	`id` varchar(36) NOT NULL,
	`name` varchar(255) CHARACTER SET utf8 NOT NULL,
	`eventTypeId` varchar(36) NOT NULL,
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

DROP TABLE IF EXISTS `tags`;
CREATE TABLE `tags` (
	`id` varchar(36) NOT NULL,
	`name` varchar(255) CHARACTER SET utf8 NOT NULL,
	`type` varchar(255) CHARACTER SET utf8 NOT NULL,
	`createdTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  	`lastModifiedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  	PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `series`;
CREATE TABLE `series` (
	`id` varchar(36) NOT NULL,
	`name` varchar(36) NOT NULL,
	`startTime` timestamp NOT NULL,
	`endTime` timestamp NOT NULL,
	`stages` int,
	`isTeam` boolean DEFAULT FALSE COMMENT 'check if it is the team series',
	`rankUpgradeQualification` int,-- which rank can upgrade to next stage
	`eventTypeId` varchar(36) NOT NULL,
	`createdTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  	`lastModifiedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  	PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `creditRules`;
CREATE TABLE `creditRules` (
	`id` varchar(36) NOT NULL,
	`name` varchar(255) CHARACTER SET utf8 NOT NULL,
	`credit` int,
	`tp` tinyint(1) DEFAULT 0 COMMENT 'single: 0, team: 1',
  	`goldenMedal` int NOT NULL DEFAULT 0,
  	`silverMedal` int NOT NULL DEFAULT 0,
  	`bronzeMedal` int NOT NULL DEFAULT 0,
	`createdTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  	`lastModifiedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  	PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `eventCreditRules`;
CREATE TABLE `eventCreditRules` (
	`id` varchar(36) NOT NULL,
	`name` varchar(255) CHARACTER SET utf8 NOT NULL,
	`eventTypeId` varchar(36) NOT NULL,
	`stage` int,
	`upperRank` int,
	`lowerRank` int,
	`credit` int,
  	`goldenMedal` int NOT NULL DEFAULT 0,
  	`silverMedal` int NOT NULL DEFAULT 0,
  	`bronzeMedal` int NOT NULL DEFAULT 0,
	`createdTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  	`lastModifiedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  	PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

 -- pending(0) / joined(1) / complete (2)
DROP TABLE IF EXISTS `attendees`;
CREATE TABLE `attendees` (
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

DROP TABLE IF EXISTS `teams`;
CREATE TABLE `teams` (
	`id` varchar(36),
	`name` varchar(36),
	`tp` int COMMENT "team type",
	`pid` varchar(36) COMMENT "connect to other object, like club",
	`size` int,
	`createdTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  	`lastModifiedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  	 PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `eventTypes`;
CREATE TABLE `eventTypes` (
	`id` varchar(36) NOT NULL,
	`name` varchar(255) CHARACTER SET utf8 NOT NULL,
	`siteId` varchar(36),
	`createdTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  	`lastModifiedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  	PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `securityCodes`;
CREATE TABLE `securityCodes` (
	`id` int(20) NOT NULL AUTO_INCREMENT,
	`securityCode` varchar(36) CHARACTER SET utf8 NOT NULL,
	`mobileNumber` varchar(36) NOT NULL,
	`remoteAddr` varchar(255) NOT NULL,
	`createdTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  	`lastModifiedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  	PRIMARY KEY (`id`),
  	UNIQUE KEY `securityCode_mobileNumber` (`mobileNumber`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `creditRecords`;
CREATE TABLE `creditRecords` (
	`id` varchar(36) CHARACTER SET utf8 NOT NULL,
	`userId` varchar(36) NOT NULL,
	`attendeeId` varchar(36),
	`eventId` varchar(36),
	`creditRuleType` varchar(255) NOT NULL,
	`creditRuleId` varchar(36) NOT NULL,
    `displayName` varchar(255) NOT NULL,
  	`rank` int,
  	`status` int NOT NULL DEFAULT 0,
	`score` float,
  	`credit` int NOT NULL DEFAULT 0,
  	`goldenMedal` int NOT NULL DEFAULT 0,
  	`silverMedal` int NOT NULL DEFAULT 0,
  	`bronzeMedal` int NOT NULL DEFAULT 0,
	`createdTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  	`lastModifiedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  	PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET GLOBAL event_scheduler = ON;
DROP EVENT IF EXISTS `cleanSecurityCodes`;
CREATE EVENT `cleanSecurityCodes`
ON SCHEDULE
    EVERY 1 HOUR
DO DELETE FROM `securityCodes` WHERE `lastModifiedTime` < DATE_SUB(NOW(), INTERVAL 24 HOUR);

COMMIT;
