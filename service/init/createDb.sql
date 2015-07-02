Create database zmxk CHARACTER SET utf8;
grant all on zmxk.* to zmxkuser@localhost identified by 'zmxkpass';
grant all on zmxk.* to zmxkuser@'%' identified by 'zmxkpass';