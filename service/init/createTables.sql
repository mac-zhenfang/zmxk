-- pending (0) / active (1) / deleted (2)
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` varchar(36) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 NOT NULL,
  `password` varchar(255) CHARACTER SET utf8,
  `location` varchar(36) CHARACTER SET utf8 NOT NULL,
  `mobileNum` varchar(255) NOT NULL,
  `wcId` varchar(36),
  `points` int NOT NULL DEFAULT 0,
  `status` int NOT NULL DEFAULT 0, 
  `roleId` varchar (36) NOT NULL,
  `createdTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `lastModifiedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `users_mobileNum` (`mobileNum`),
  UNIQUE KEY `users_wcId` (`wcId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles` (
	`id` varchar(36) NOT NULL,
	`name` varchar(255) NOT NULL,
	`principles` varchar(36) NOT NULL,
	`createdTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  	`lastModifiedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `kids`;
CREATE TABLE `kids` (
	`id` varchar(36) NOT NULL,
	`name` varchar(255) CHARACTER SET utf8 NOT NULL,
	`schoolType` int,
	`schoolName` varchar(255) CHARACTER SET utf8 NOT NULL,
	`userId` varchar(36) NOT NULL,
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
	`stage` int, -- Preliminary(0)/contest(1)/semi final(2)/final(3)
	`status` int,-- prepare(0) / start (1) / complete (2)
	`eventTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
	`createdTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  	`lastModifiedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  	PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `series`;
CREATE TABLE `series` (
	`id` varchar(36) NOT NULL,
	`name` varchar(36) NOT NULL,
	`series_starttime` timestamp NOT NULL,
	`series_endtime` timestamp NOT NULL,
	`stages` int,
	`rank_upgrade_qualification` int,-- which rank can upgrade to next stage
	`siteId` varchar(36) NOT NULL,
	`eventTypeId` varchar(36) NOT NULL,
	`createdTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  	`lastModifiedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  	PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `points_rules`;
CREATE TABLE `points_rules` (
	`id` varchar(36) NOT NULL,
	`action` varchar(255) CHARACTER SET utf8 NOT NULL,
	`points` int,
	`status` int NOT NULL DEFAULT 0,
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
	`eventId` varchar(36) NOT NULL,
	`score` float,
	`rank` int,
 	`seq` int,
 	`status` int NOT NULL DEFAULT 0,
	`createdTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  	`lastModifiedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  	PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `eventTypes`;
CREATE TABLE `eventTypes` (
	`id` varchar(36) NOT NULL,
	`name` varchar(255) CHARACTER SET utf8 NOT NULL,
	`siteId` varchar(36) NOT NULL,
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

SET GLOBAL event_scheduler = ON;
DROP EVENT IF EXISTS `cleanSecurityCodes`;
CREATE EVENT `cleanSecurityCodes`
ON SCHEDULE
    EVERY 1 HOUR
DO DELETE FROM `securityCodes` WHERE `lastModifiedTime` < DATE_SUB(NOW(), INTERVAL 24 HOUR);
