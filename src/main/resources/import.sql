--Add primary agency
INSERT INTO agency (id, name) VALUES('1359dede-9f18-11e5-8994-feff819cdc9f', 'NSD-qddt');

--Add two demo accounts
--admin:password & user:password (bcrypt(10) passwords)
INSERT INTO user_account(id, username, password, email, agency_id,is_enabled) VALUES('83d4c034-4ff9-11e5-885d-feff819cdc9f', 'admin', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'admin@example.org', '1359dede-9f18-11e5-8994-feff819cdc9f',true);
INSERT INTO user_account(id, username, password, email, agency_id,is_enabled) VALUES('83d4c39a-4ff9-11e5-885d-feff819cdc9f', 'user@example.org', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'user@example.org', '1359dede-9f18-11e5-8994-feff819cdc9f',true);
INSERT INTO user_account(id, username, password, email, agency_id,is_enabled) VALUES('83d4c3aa-4ff9-11e5-885d-feff819cdc9f', 'stig', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'stig.norland@nsd.no', '1359dede-9f18-11e5-8994-feff819cdc9f',true);
INSERT INTO user_account(id, username, password, email, agency_id,is_enabled) VALUES('83d4c3ba-4ff9-11e5-885d-feff819cdc9f', 'review', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'review@example.org', '1359dede-9f18-11e5-8994-feff819cdc9f',true);
-- INSERT INTO user_account(id, username, password, email) VALUES('83d4c034-4ff9-11e5-885d-feff819cdc9f', 'admin', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'admin@example.org');
-- INSERT INTO user_account(id, username, password, email) VALUES('83d4c39a-4ff9-11e5-885d-feff819cdc9f', 'user', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'user@example.org');

--Create ADMIN and USER authorities
INSERT INTO authority (id, name, authority) VALUES ('9bec2c02-4ff9-11e5-885d-feff819cdc9f', 'admin', 'ROLE_ADMIN');
INSERT INTO authority (id, name, authority) VALUES('9bec2d6a-4ff9-11e5-885d-feff819cdc9f', 'user', 'ROLE_USER');
INSERT INTO authority (id, authority, name) VALUES ('9bec2d6b-4ff9-11e5-885d-feff819cdc9f','ROLE_SUPER','AgencyAdmin');
INSERT INTO authority (id, authority, name) VALUES ('9bec2d6c-4ff9-11e5-885d-feff819cdc9f','ROLE_LIMITED','Reviewer');


--Set up admin authorities
--admin:
INSERT INTO user_authority (user_id, authority_id) VALUES('83d4c034-4ff9-11e5-885d-feff819cdc9f', '9bec2c02-4ff9-11e5-885d-feff819cdc9f');
INSERT INTO user_authority (user_id, authority_id) VALUES('83d4c034-4ff9-11e5-885d-feff819cdc9f', '9bec2d6a-4ff9-11e5-885d-feff819cdc9f');
--user:
INSERT INTO user_authority (user_id, authority_id) VALUES('83d4c39a-4ff9-11e5-885d-feff819cdc9f', '9bec2d6a-4ff9-11e5-885d-feff819cdc9f');
--superuser
INSERT INTO user_authority (user_id, authority_id) VALUES('83d4c30a-4ff9-11e5-885d-feff819cdc9f', '9bec2d6b-4ff9-11e5-885d-feff819cdc9f');
INSERT INTO user_authority (user_id, authority_id) VALUES('83d4c3aa-4ff9-11e5-885d-feff819cdc9f', '9bec2d6b-4ff9-11e5-885d-feff819cdc9f');
--commenter
INSERT INTO user_authority (user_id, authority_id) VALUES('83d4c3ba-4ff9-11e5-885d-feff819cdc9f', '9bec2d6c-4ff9-11e5-885d-feff819cdc9f');



INSERT INTO author (id,  name, picture) VALUES ('83d4c30a-4ff9-11e5-885d-feff819cdc9f',  'Prof Drøvell', 'http://coraljs.com/example/contact/john-doe.jpeg');
INSERT INTO author (id,  name, picture) VALUES ('83d4c30b-4ff9-11e5-885d-feff819cdc9f',  'Professor AI Head', 'http://www.secondpicture.com/tutorials/3d/human_head_reference_picture_front.jpg');
INSERT INTO author (id,  name, picture) VALUES ('83d4c30c-4ff9-11e5-885d-feff819cdc9f',  'Doctor Who', 'https://upload.wikimedia.org/wikipedia/commons/thumb/8/83/Matt_Smith_Cannes_2014.jpg/220px-Matt_Smith_Cannes_2014.jpg');
INSERT INTO author (id,  name, picture) VALUES ('83d4c30d-4ff9-11e5-885d-feff819cdc9f',  'Professor Veldiglangtnavn', 'http://coraljs.com/example/contact/john-doe.jpeg');
UPDATE author SET  email = 'test@example.org', homepage = 'http://www.nsd.no' WHERE id = '83d4c30a-4ff9-11e5-885d-feff819cdc9f';
UPDATE author SET  homepage = 'http://ai.uni-bremen.de/team/michael_beetz' WHERE id = '83d4c30b-4ff9-11e5-885d-feff819cdc9f';
UPDATE author SET   homepage = 'https://en.wikipedia.org/wiki/Doctor_Who' WHERE id = '83d4c30c-4ff9-11e5-885d-feff819cdc9f';
