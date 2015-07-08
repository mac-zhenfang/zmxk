DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` varchar(36) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 NOT NULL,
  `password` varchar(255) CHARACTER SET utf8,
  `location` varchar(36) CHARACTER SET utf8 NOT NULL,
  `mobileNum` varchar(255) NOT NULL,
  `wcId` varchar(36),
  `status` int,
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
	`quota` int,
	`eventTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
	`createdTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  	`lastModifiedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  	PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `attendees`;
CREATE TABLE `attendees` (
	`id` varchar(36) NOT NULL,
	`kidId` varchar(36) NOT NULL,
	`userId` varchar(36) NOT NULL,
	`eventId` varchar(36) NOT NULL,
	`score` float,
	`rank` int,
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
