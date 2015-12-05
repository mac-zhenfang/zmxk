ALTER TABLE users ADD likes int(1) DEFAULT 0;
ALTER TABLE users DELETE likes;

ALTER TABLE kids ADD likes int(1) DEFAULT 0;

ALTER TABLE teams ADD members text;
ALTER TABLE kids DELETE teamId;
ALTER TABLE teams ADD ownerId varchar(36);
ALTER TABLE users ADD maxTeamMemberSize int(1) default 12;
ALTER TABLE teams ADD minSize int(1) DEFAULT 5;
create index status_event_time on events(status, eventTime);
ALTER TABLE kids ADD age int(1) DEFAULT 0;
ALTER TABLE kids ADD gender int(1) DEFAULT 0;
ALTER TABLE kids ADD firstTimeAttendEvent timestamp;
ALTER TABLE attendees ADD videoLink varchar(64) default null;
ALTER TABLE kids add coverVideoLink varchar(64) default null;
ALTER TABLE events add seq int(3);
ALTER TABLE events_hist add seq int(3);
ALTER TABLE kids ADD kidNum int(1) unique;
ALTER TABLE sites ADD siteNum int(1) unique;
