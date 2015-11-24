ALTER TABLE users ADD likes int(1) DEFAULT 0;
ALTER TABLE teams ADD members text;
ALTER TABLE kids DELETE teamId;