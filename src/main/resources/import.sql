--Add primary agency
INSERT INTO agency (id, name) VALUES('1359dede-9f18-11e5-8994-feff819cdc9f', 'NSD-qddt');

--Add two demo accounts
--admin:password & user:password (bcrypt(10) passwords)
INSERT INTO user_account(id, username, password, email, agency_id) VALUES('83d4c034-4ff9-11e5-885d-feff819cdc9f', 'admin', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'admin@example.org', '1359dede-9f18-11e5-8994-feff819cdc9f');
INSERT INTO user_account(id, username, password, email, agency_id) VALUES('83d4c39a-4ff9-11e5-885d-feff819cdc9f', 'user@example.org', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'user@example.org', '1359dede-9f18-11e5-8994-feff819cdc9f');
INSERT INTO user_account(id, username, password, email, agency_id) VALUES('83d4c30a-4ff9-11e5-885d-feff819cdc9f', 'yong', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'yong@nsd.no', '1359dede-9f18-11e5-8994-feff819cdc9f');
INSERT INTO user_account(id, username, password, email, agency_id) VALUES('83d4c3aa-4ff9-11e5-885d-feff819cdc9f', 'stig', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'stig.norland@nsd.no', '1359dede-9f18-11e5-8994-feff819cdc9f');
INSERT INTO user_account(id, username, password, email, agency_id) VALUES('83d4c3ba-4ff9-11e5-885d-feff819cdc9f', 'review', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'review@example.org', '1359dede-9f18-11e5-8994-feff819cdc9f');
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
INSERT INTO author (id,  name, picture) VALUES ('83d4c30b-4ff9-11e5-885d-feff819cdc9f',  'Prof AI Head', 'http://www.secondpicture.com/tutorials/3d/human_head_reference_picture_front.jpg');
INSERT INTO author (id,  name, picture) VALUES ('83d4c30c-4ff9-11e5-885d-feff819cdc9f',  'Dr Who', 'https://upload.wikimedia.org/wikipedia/commons/thumb/8/83/Matt_Smith_Cannes_2014.jpg/220px-Matt_Smith_Cannes_2014.jpg');
INSERT INTO author (id,  name, picture) VALUES ('83d4c30d-4ff9-11e5-885d-feff819cdc9f',  'Professor Veldiglangnavn', 'http://coraljs.com/example/contact/john-doe.jpeg');
UPDATE author SET  email = 'test@example.org', homepage = 'http://www.nsd.no' WHERE id = '83d4c30a-4ff9-11e5-885d-feff819cdc9f';
UPDATE author SET  homepage = 'http://ai.uni-bremen.de/team/michael_beetz' WHERE id = '83d4c30b-4ff9-11e5-885d-feff819cdc9f';
UPDATE author SET   homepage = 'https://en.wikipedia.org/wiki/Doctor_Who' WHERE id = '83d4c30c-4ff9-11e5-885d-feff819cdc9f';

INSERT INTO question (id, updated,  change_kind, name, major, minor,   intent, question, user_id, agency_id, parent_id) VALUES ('a9fe6c58-53ea-1875-8153-ea78ddcb0000','2016-04-06 09:28:22','CREATED','Q1',0,1,'test','What is your age?','83d4c034-4ff9-11e5-885d-feff819cdc9f','1359dede-9f18-11e5-8994-feff819cdc9f',(null));
INSERT INTO question (id, updated,  change_kind, name, major, minor,   intent, question, user_id, agency_id, parent_id) VALUES ('a9fe6c58-53ea-1875-8153-ea78eb4f0001','2016-04-06 09:28:26','CREATED','Q2',0,1,'test','What is your gender?','83d4c034-4ff9-11e5-885d-feff819cdc9f','1359dede-9f18-11e5-8994-feff819cdc9f',(null));
INSERT INTO question (id, updated,  change_kind, name, major, minor,   intent, question, user_id, agency_id, parent_id) VALUES ('a9fe6c58-53ea-1875-8153-ea78f8810002','2016-04-06 09:28:29','CREATED','Q3',0,1,'test','Where are you from?','83d4c034-4ff9-11e5-885d-feff819cdc9f','1359dede-9f18-11e5-8994-feff819cdc9f',(null));
INSERT INTO question (id, updated,  change_kind, name, major, minor,   intent, question, user_id, agency_id, parent_id) VALUES ('a9fe6c58-53ea-1875-8153-ea790a700003','2016-04-06 09:28:34','CREATED','Q4',0,1,'test','Where do you live now?','83d4c034-4ff9-11e5-885d-feff819cdc9f','1359dede-9f18-11e5-8994-feff819cdc9f',(null)); --'a9fe6c58-53ea-1875-8153-ea78f8810002');
INSERT INTO question (id, updated,  change_kind, name, major, minor,   intent, question, user_id, agency_id, parent_id) VALUES ('a9fe6c58-53ea-1875-8153-ea79192a0004','2016-04-06 09:28:38','CREATED','Q5',0,1,'test','Who do you belive?','83d4c034-4ff9-11e5-885d-feff819cdc9f','1359dede-9f18-11e5-8994-feff819cdc9f',(null));--'a9fe6c58-53ea-1875-8153-ea78f8810002');


--POPULATE THE RESPONSE PACKAGE WITH DATA.
INSERT INTO category(id,user_id, agency_id, updated,label,name,major, minor,hierarchy_level, category_kind,CHANGE_KIND) VALUES('378949ec-65d0-11e5-9d70-feff819cdc9f','83d4c034-4ff9-11e5-885d-feff819cdc9f','1359dede-9f18-11e5-8994-feff819cdc9f','01-01-2015 12:00:01.000', 'Start','START',0,1,'ENTITY','CODE','CREATED');
INSERT INTO category(id,user_id, agency_id, updated,label,name,major, minor,hierarchy_level, category_kind,CHANGE_KIND) VALUES('37894d7a-65d0-11e5-9d70-feff819cdc9f','83d4c034-4ff9-11e5-885d-feff819cdc9f','1359dede-9f18-11e5-8994-feff819cdc9f','01-01-2015 12:00:01.000', 'Slutt','SLUTT',0,1,'ENTITY','CODE','CREATED');
INSERT INTO category(id,user_id, agency_id, updated,label,name,major, minor,hierarchy_level, category_kind,CHANGE_KIND) VALUES('37894f32-65d0-11e5-9d70-feff819cdc9f','83d4c034-4ff9-11e5-885d-feff819cdc9f','1359dede-9f18-11e5-8994-feff819cdc9f','01-01-2015 12:00:01.000', 'Range','RANGE-START-SLUTT',0,1,'GROUP_ENTITY','RANGE','CREATED');
INSERT INTO category(id,user_id, agency_id, updated,label,name,major, minor,hierarchy_level, category_kind,CHANGE_KIND) VALUES('378953c4-65d0-11e5-9d70-feff819cdc9f','83d4c034-4ff9-11e5-885d-feff819cdc9f','1359dede-9f18-11e5-8994-feff819cdc9f','01-01-2015 12:00:01.000', 'NA','NA svar',0,1,'ENTITY','CODE','CREATED');
INSERT INTO category(id,user_id, agency_id, updated,label,name,major, minor,hierarchy_level, category_kind,CHANGE_KIND) VALUES('378955cc-65d0-11e5-9d70-feff819cdc9f','83d4c034-4ff9-11e5-885d-feff819cdc9f','1359dede-9f18-11e5-8994-feff819cdc9f','01-01-2015 12:00:01.000', 'Vet ikke','NA svar',0,1,'ENTITY','CODE','CREATED');
INSERT INTO category(id,user_id, agency_id, updated,label,name,major, minor,hierarchy_level, category_kind,CHANGE_KIND) VALUES('378955cd-65d0-11e5-9d70-feff819cdc9f','83d4c034-4ff9-11e5-885d-feff819cdc9f','1359dede-9f18-11e5-8994-feff819cdc9f','01-01-2015 12:00:01.000', 'Ikke svar','NA',0,1,'GROUP_ENTITY','MISSING_GROUP','CREATED');
INSERT INTO category(id,user_id, agency_id, updated,label,name,major, minor,hierarchy_level, category_kind,CHANGE_KIND) VALUES('378955ce-65d0-11e5-9d70-feff819cdc9f','83d4c034-4ff9-11e5-885d-feff819cdc9f','1359dede-9f18-11e5-8994-feff819cdc9f','01-01-2015 12:00:01.000', 'Responsdomain Range','NA',0,1,'GROUP_ENTITY','MIXED','CREATED');

ALTER TABLE category_category drop CONSTRAINT uk_qef5u1tm9s8i1hhpoqeg0dq9e;

INSERT INTO category_category (id, parent_id) VALUES ('378949ec-65d0-11e5-9d70-feff819cdc9f', '37894f32-65d0-11e5-9d70-feff819cdc9f');
INSERT INTO category_category (id, parent_id) VALUES ('37894d7a-65d0-11e5-9d70-feff819cdc9f', '37894f32-65d0-11e5-9d70-feff819cdc9f');
INSERT INTO category_category (id, parent_id) VALUES ('378953c4-65d0-11e5-9d70-feff819cdc9f', '378955cd-65d0-11e5-9d70-feff819cdc9f');
INSERT INTO category_category (id, parent_id) VALUES ('378955cc-65d0-11e5-9d70-feff819cdc9f', '378955cd-65d0-11e5-9d70-feff819cdc9f');
INSERT INTO category_category (id, parent_id) VALUES ('378955cc-65d0-11e5-9d70-feff819cdc9f', '378955ce-65d0-11e5-9d70-feff819cdc9f');
INSERT INTO category_category (id, parent_id) VALUES ('37894f32-65d0-11e5-9d70-feff819cdc9f', '378955ce-65d0-11e5-9d70-feff819cdc9f');


INSERT INTO responsedomain(id,updated,CHANGE_KIND,name,response_kind,category_id, user_id,agency_id) VALUES('d5dbaebb-65d0-11e5-9d70-feff819cdc9f','01-01-2015','CREATED','Responsdomain Range','Scale','378955ce-65d0-11e5-9d70-feff819cdc9f','83d4c39a-4ff9-11e5-885d-feff819cdc9f','1359dede-9f18-11e5-8994-feff819cdc9f');
INSERT INTO responsedomain(id,updated,CHANGE_KIND,name,response_kind,category_id, user_id,agency_id) VALUES('d5dbb12a-65d0-11e5-9d70-feff819cdc9f','01-01-2015','CREATED','TestDomianCategory','Category',,'83d4c39a-4ff9-11e5-885d-feff819cdc9f','1359dede-9f18-11e5-8994-feff819cdc9f');

INSERT INTO code (id, updated,  change_kind, name,   code_value,  category_id, question_id, user_id,agency_id) VALUES ('05f9a484-65d1-11e5-9d70-feff819cdc9f', '01-01-2015','CREATED', 'name', '1', '378949ec-65d0-11e5-9d70-feff819cdc9f', 'a9fe6c58-53ea-1875-8153-ea78ddcb0000','83d4c39a-4ff9-11e5-885d-feff819cdc9f','1359dede-9f18-11e5-8994-feff819cdc9f');
INSERT INTO code (id, updated,  change_kind, name,   code_value,  category_id, question_id, user_id,agency_id) VALUES ('05f9a89e-65d1-11e5-9d70-feff819cdc9f', '01-01-2015','CREATED', 'name', '9', '37894d7a-65d0-11e5-9d70-feff819cdc9f', 'a9fe6c58-53ea-1875-8153-ea78ddcb0000','83d4c39a-4ff9-11e5-885d-feff819cdc9f','1359dede-9f18-11e5-8994-feff819cdc9f');
INSERT INTO code (id, updated,  change_kind, name,   code_value,  category_id, question_id, user_id,agency_id) VALUES ('05f9a9c0-65d1-11e5-9d70-feff819cdc9f', '01-01-2015','CREATED', 'name', '66', '378953c4-65d0-11e5-9d70-feff819cdc9f','a9fe6c58-53ea-1875-8153-ea78ddcb0000','83d4c39a-4ff9-11e5-885d-feff819cdc9f','1359dede-9f18-11e5-8994-feff819cdc9f');
INSERT INTO code (id, updated,  change_kind, name,   code_value,  category_id, question_id, user_id,agency_id) VALUES ('05f9abd2-65d1-11e5-9d70-feff819cdc9f', '01-01-2015','CREATED', 'name', '67', '378955cc-65d0-11e5-9d70-feff819cdc9f','a9fe6c58-53ea-1875-8153-ea78ddcb0000','83d4c39a-4ff9-11e5-885d-feff819cdc9f','1359dede-9f18-11e5-8994-feff819cdc9f');


INSERT INTO code(id,updated,CHANGE_KIND, responsedomain_id, category_id,  code_value, user_id,agency_id) VALUES('05f9ad12-65d1-11e5-9d70-feff819cdc9f','01-01-2015','CREATED', 'd5dbaebe-65d0-11e5-9d70-feff819cdc9f','378955cc-65d0-11e5-9d70-feff819cdc9f', '15','83d4c39a-4ff9-11e5-885d-feff819cdc9f','1359dede-9f18-11e5-8994-feff819cdc9f');


-- INSERT INTO SURVEY_PROGRAM(id,name,CREATED,CHANGE_KIND,USER_ID) VALUES('a9fe6c00-51af-1ced-8151-af0dd96a0000','MORE SURVEY','01-12-2015 07:00:01','CREATED','83d4c034-4ff9-11e5-885d-feff819cdc9f');
-- INSERT INTO SURVEY_PROGRAM(id,name,CREATED,CHANGE_KIND,USER_ID) VALUES('a9fe6c01-51af-1ced-8151-af0dd96a0000','SUCH SURVEY','01-12-2015 09:00:02','CREATED','83d4c034-4ff9-11e5-885d-feff819cdc9f');
-- INSERT INTO SURVEY_PROGRAM(id,name,CREATED,CHANGE_KIND,USER_ID) VALUES('a9fe6c02-51af-1ced-8151-af0dd96a0000','MUCH WOW SURVEY','01-12-2015 11:00:03','CREATED','83d4c034-4ff9-11e5-885d-feff819cdc9f');
-- INSERT INTO SURVEY_PROGRAM(id,name,CREATED,CHANGE_KIND,USER_ID) VALUES('a9fe6c03-51af-1ced-8151-af0dd96a0000','DOGE SURVEY','01-12-2015 13:00:04','CREATED','83d4c034-4ff9-11e5-885d-feff819cdc9f');
-- INSERT INTO COMMENT (id, owner_uuid,COMMENT, CREATED, USER_ID) VALUES('a9fe6d00-51af-1ced-8151-af0dd96a0000','a9fe6c00-51af-1ced-8151-af0dd96a0000','ANOTHER COMMENT YES', '2015-02-10 07:00:01', '83d4c034-4ff9-11e5-885d-feff819cdc9f');
-- INSERT INTO COMMENT (id, owner_uuid,COMMENT, CREATED, USER_ID) VALUES('a9fe6d01-51af-1ced-8151-af0dd96a0000','a9fe6c01-51af-1ced-8151-af0dd96a0000','ANOTHER COMMENT YES', '2015-02-10 09:00:01', '83d4c034-4ff9-11e5-885d-feff819cdc9f');
-- INSERT INTO COMMENT (id, owner_uuid,COMMENT, CREATED, USER_ID) VALUES('a9fe6d02-51af-1ced-8151-af0dd96a0000','a9fe6c02-51af-1ced-8151-af0dd96a0000','ANOTHER COMMENT YES', '2015-02-10 11:00:01', '83d4c034-4ff9-11e5-885d-feff819cdc9f');
-- INSERT INTO COMMENT (id, owner_uuid,COMMENT, CREATED, USER_ID) VALUES('a9fe6d03-51af-1ced-8151-af0dd96a0000','a9fe6c03-51af-1ced-8151-af0dd96a0000','ANOTHER COMMENT YES', '2015-02-10 13:00:01', '83d4c034-4ff9-11e5-885d-feff819cdc9f');
-- INSERT INTO COMMENT (id, owner_uuid,COMMENT, CREATED, USER_ID) VALUES('a9fe6d04-51af-1ced-8151-af0dd96a0000','a9fe6c04-51af-1ced-8151-af0dd96a0000','ANOTHER COMMENT YES', '2015-02-10 15:00:01', '83d4c034-4ff9-11e5-885d-feff819cdc9f');
--
-- INSERT INTO COMMENT (id, owner_uuid,COMMENT, CREATED, USER_ID) VALUES('a9fe6d05-51af-1ced-8151-af0dd96a0000','a9fe6c58-51af-12ae-8151-af82ea6a0000','ANOTHER COMMENT YES', '2015-02-10 07:00:00', '83d4c034-4ff9-11e5-885d-feff819cdc9f');
--
-- update comment set created = '2015-12-10 07:01:01' where created < '2015-12-11'
--
--
-- --POPULATE THE RESPONSE PACKAGE WITH DATA.
-- INSERT INTO category(id,created,label,name,hierarchy_level, category_kind,CHANGE_KIND) VALUES('378949ec-65d0-11e5-9d70-feff819cdc9f','01-01-2015 12:00:01.000', 'Start','START','ENTITY','CODE','CREATED');
-- INSERT INTO category(id,created,label,name,hierarchy_level, category_kind,CHANGE_KIND) VALUES('37894d7a-65d0-11e5-9d70-feff819cdc9f','01-01-2015 12:00:01.000', 'Slutt','SLUTT','ENTITY','CODE','CREATED');
-- INSERT INTO category(id,created,label,name,hierarchy_level, category_kind,CHANGE_KIND) VALUES('37894f32-65d0-11e5-9d70-feff819cdc9f','01-01-2015 12:00:01.000', 'Range','RANGE-START-SLUTT','GROUP_ENTITY','RANGE','CREATED');
-- INSERT INTO category(id,created,label,name,hierarchy_level, category_kind,CHANGE_KIND) VALUES('378953c4-65d0-11e5-9d70-feff819cdc9f','01-01-2015 12:00:01.000', 'NA','NA svar','ENTITY','CODE','CREATED');
-- INSERT INTO category(id,created,label,name,hierarchy_level, category_kind,CHANGE_KIND) VALUES('378955cc-65d0-11e5-9d70-feff819cdc9f','01-01-2015 12:00:01.000', 'Vet ikke','NA svar','ENTITY','CODE','CREATED');
-- INSERT INTO category(id,created,label,name,hierarchy_level, category_kind,CHANGE_KIND) VALUES('378955cd-65d0-11e5-9d70-feff819cdc9f','01-01-2015 12:00:01.000', 'Ikke svar','NA','GROUP_ENTITY','MULTIPLE_SINGLE','CREATED');
-- INSERT INTO category(id,created,label,name,hierarchy_level, category_kind,CHANGE_KIND) VALUES('378955ce-65d0-11e5-9d70-feff819cdc9f','01-01-2015 12:00:01.000', 'Responsdomain Range','NA','ROOT_ENTITY','MIXED','CREATED');
--
-- ALTER TABLE category_category drop CONSTRAINT uk_qef5u1tm9s8i1hhpoqeg0dq9e;
--
-- INSERT INTO category_category (id, parent_id) VALUES ('378949ec-65d0-11e5-9d70-feff819cdc9f', '37894f32-65d0-11e5-9d70-feff819cdc9f');
-- INSERT INTO category_category (id, parent_id) VALUES ('37894d7a-65d0-11e5-9d70-feff819cdc9f', '37894f32-65d0-11e5-9d70-feff819cdc9f');
-- INSERT INTO category_category (id, parent_id) VALUES ('378953c4-65d0-11e5-9d70-feff819cdc9f', '378955cd-65d0-11e5-9d70-feff819cdc9f');
-- INSERT INTO category_category (id, parent_id) VALUES ('378955cc-65d0-11e5-9d70-feff819cdc9f', '378955cd-65d0-11e5-9d70-feff819cdc9f');
-- INSERT INTO category_category (id, parent_id) VALUES ('378955cc-65d0-11e5-9d70-feff819cdc9f', '378955ce-65d0-11e5-9d70-feff819cdc9f');
-- INSERT INTO category_category (id, parent_id) VALUES ('37894f32-65d0-11e5-9d70-feff819cdc9f', '378955ce-65d0-11e5-9d70-feff819cdc9f');
--
--
-- INSERT INTO responsedomain(id,created,CHANGE_KIND,name,response_kind,category_id) VALUES('d5dbaebb-65d0-11e5-9d70-feff819cdc9f','01-01-2015','CREATED','Responsdomain Range','Scale','378955ce-65d0-11e5-9d70-feff819cdc9f');
-- INSERT INTO responsedomain(id,created,CHANGE_KIND,name,response_kind) VALUES('d5dbb12a-65d0-11e5-9d70-feff819cdc9f','01-01-2015','CREATED','TestDomianCategory','Category');
--
-- INSERT INTO responsedomain_code (id, created,  change_kind, name,  category_idx, code_value,  category_id, responsedomain_id) VALUES ('05f9a484-65d1-11e5-9d70-feff819cdc9f', '01-01-2015','CREATED', 'name', 1, '1', '378949ec-65d0-11e5-9d70-feff819cdc9f', 'd5dbaebb-65d0-11e5-9d70-feff819cdc9f');
-- INSERT INTO responsedomain_code (id, created,  change_kind, name,  category_idx, code_value,  category_id, responsedomain_id) VALUES ('05f9a89e-65d1-11e5-9d70-feff819cdc9f', '01-01-2015','CREATED', 'name', 2, '9', '37894d7a-65d0-11e5-9d70-feff819cdc9f', 'd5dbaebb-65d0-11e5-9d70-feff819cdc9f');
-- INSERT INTO responsedomain_code (id, created,  change_kind, name,  category_idx, code_value,  category_id, responsedomain_id) VALUES ('05f9a9c0-65d1-11e5-9d70-feff819cdc9f', '01-01-2015','CREATED', 'name', 3, '66', '378953c4-65d0-11e5-9d70-feff819cdc9f','d5dbaebb-65d0-11e5-9d70-feff819cdc9f');
-- INSERT INTO responsedomain_code (id, created,  change_kind, name,  category_idx, code_value,  category_id, responsedomain_id) VALUES ('05f9abd2-65d1-11e5-9d70-feff819cdc9f', '01-01-2015','CREATED', 'name', 4, '67', '378955cc-65d0-11e5-9d70-feff819cdc9f','d5dbaebb-65d0-11e5-9d70-feff819cdc9f');
--
--
-- INSERT INTO responsedomain_code(id,created,CHANGE_KIND, responsedomain_id, category_id, category_idx, code_value) VALUES('05f9ad12-65d1-11e5-9d70-feff819cdc9f','01-01-2015','CREATED', 'd5dbaebe-65d0-11e5-9d70-feff819cdc9f','378955cc-65d0-11e5-9d70-feff819cdc9f', 5,'15');
--
--
-- --POPULATE SURVEY DUMMY DATA
-- INSERT INTO survey_program(id,created,CHANGE_KIND,name)               VALUES('a9fe6c58-5038-1fe0-8150-382001480000','01-01-2015','CREATED', 'THE SURVEY');
-- INSERT INTO study         (id,created,CHANGE_KIND,name,survey_id)     VALUES('a9fe6c58-5038-1fe0-8150-382001480001','01-01-2015','CREATED','myStudy','a9fe6c58-5038-1fe0-8150-382001480000');
-- INSERT INTO topic_group   (id,created,CHANGE_KIND,name,study_id)      VALUES('a9fe6c58-5038-1fe0-8150-382001480002','01-01-2015','CREATED','Module test','a9fe6c58-5038-1fe0-8150-382001480001');
-- INSERT INTO concept       (id,created,CHANGE_KIND,name,topicgroup_id) VALUES('a9fe6c58-5038-1fe0-8150-382001480003','01-01-2015','CREATED','Test Concept','a9fe6c58-5038-1fe0-8150-382001480002');
-- INSERT INTO question      (id,created,CHANGE_KIND,grid_idx,name,question,responsedomain_id)  VALUES('a9fe6c58-5038-1fe0-8150-382001480004','01-01-2015','CREATED',0,'How to DDI?','How would you DDI if you could?','d5dbaebe-65d0-11e5-9d70-feff819cdc9f');
-- INSERT INTO concept_question (question_id,concept_id) VALUES('a9fe6c58-5038-1fe0-8150-382001480004','a9fe6c58-5038-1fe0-8150-382001480003')