// Add two demo accounts
// admin:password & user:password (bcrypt(10) passwords)
INSERT INTO user(id, username, password, email) VALUES('83d4c034-4ff9-11e5-885d-feff819cdc9f', 'admin@example.org', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'admin@example.org');
INSERT INTO user(id, username, password, email) VALUES('83d4c39a-4ff9-11e5-885d-feff819cdc9f', 'user@example.org', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'user@example.org');

// Create ADMIN and USER authorities
INSERT INTO authority (id, name, authority) VALUES ('9bec2c02-4ff9-11e5-885d-feff819cdc9f', 'admin', 'ROLE_ADMIN');
INSERT INTO authority (id, name, authority) VALUES('9bec2d6a-4ff9-11e5-885d-feff819cdc9f', 'user', 'ROLE_USER');

// Set up admin authorities
INSERT INTO user_authority (user_id, authority_id) VALUES('83d4c034-4ff9-11e5-885d-feff819cdc9f', '9bec2c02-4ff9-11e5-885d-feff819cdc9f');
INSERT INTO user_authority (user_id, authority_id) VALUES('83d4c034-4ff9-11e5-885d-feff819cdc9f', '9bec2d6a-4ff9-11e5-885d-feff819cdc9f');
// Set up user authorities
INSERT INTO user_authority (user_id, authority_id) VALUES('83d4c39a-4ff9-11e5-885d-feff819cdc9f', '9bec2d6a-4ff9-11e5-885d-feff819cdc9f');

// POPULATE SURVEY DUMMY DATA
-- INSERT INTO SURVEY(survey_name) VALUES('A SURVEY');
-- INSERT INTO SURVEY(survey_name) VALUES('MORE SURVEY');
-- INSERT INTO SURVEY(survey_name) VALUES('SUCH SURVEY');
-- INSERT INTO SURVEY(survey_name) VALUES('MUCH WOW SURVEY');
-- INSERT INTO SURVEY(survey_name) VALUES('DOGE SURVEY');
-- INSERT INTO COMMENT (COMMENT, CREATED, USER_ID, SURVEY_ID) VALUES('ANOTHER COMMENT YES', '2015-02-10 17:28:22.952', 2, 1);

// POPULATE THE RESPONSE PACKAGE WITH DATA.
INSERT INTO CODE(id, category, code_value) VALUES('378949ec-65d0-11e5-9d70-feff819cdc9f', 'Dummy Category Alpha', 1);
INSERT INTO CODE(id, category, code_value) VALUES('37894d7a-65d0-11e5-9d70-feff819cdc9f', 'Dummy Category Beta', 2);
INSERT INTO CODE(id, category, code_value) VALUES('37894f32-65d0-11e5-9d70-feff819cdc9f', 'Dummy Category Charlie', 3);
INSERT INTO CODE(id, category, code_value) VALUES('378953c4-65d0-11e5-9d70-feff819cdc9f', 'Dummy Category Delta', 4);
INSERT INTO CODE(id, category, code_value) VALUES('378955cc-65d0-11e5-9d70-feff819cdc9f', 'Dummy Category Echo', 5);

INSERT INTO RESPONSE_KIND(id, code, name) VALUES('3789577a-65d0-11e5-9d70-feff819cdc9f', 1, 'RESPONSE Alpha');
INSERT INTO RESPONSE_KIND(id, code, name) VALUES('3789590a-65d0-11e5-9d70-feff819cdc9f', 2, 'RESPONSE Beta');
INSERT INTO RESPONSE_KIND(id, code, name) VALUES('37895cd4-65d0-11e5-9d70-feff819cdc9f', 3, 'RESPONSE Charli');
INSERT INTO RESPONSE_KIND(id, code, name) VALUES('37895ee6-65d0-11e5-9d70-feff819cdc9f', 4, 'RESPONSE Delta');
INSERT INTO RESPONSE_KIND(id, code, name) VALUES('378960a8-65d0-11e5-9d70-feff819cdc9f', 5, 'RESPONSE Echo');

INSERT INTO RESPONSE_DOMAIN (id, RESPONSE_KIND_ID) VALUES('d5dbaebe-65d0-11e5-9d70-feff819cdc9f', '3789577a-65d0-11e5-9d70-feff819cdc9f');
INSERT INTO RESPONSE_DOMAIN (id, RESPONSE_KIND_ID) VALUES('d5dbb12a-65d0-11e5-9d70-feff819cdc9f', '3789590a-65d0-11e5-9d70-feff819cdc9f');
INSERT INTO RESPONSE_DOMAIN (id, RESPONSE_KIND_ID) VALUES('d5dbb332-65d0-11e5-9d70-feff819cdc9f', '37895cd4-65d0-11e5-9d70-feff819cdc9f');
INSERT INTO RESPONSE_DOMAIN (id, RESPONSE_KIND_ID) VALUES('d5dbb418-65d0-11e5-9d70-feff819cdc9f', '37895ee6-65d0-11e5-9d70-feff819cdc9f');
INSERT INTO RESPONSE_DOMAIN (id, RESPONSE_KIND_ID) VALUES('d5dbb4ea-65d0-11e5-9d70-feff819cdc9f', '378960a8-65d0-11e5-9d70-feff819cdc9f');

INSERT INTO RESPONSE_DOMAIN_CODE(id, RESPONSEDOMAIN_ID, CODE_ID, RANK) VALUES('05f9a484-65d1-11e5-9d70-feff819cdc9f', '3789577a-65d0-11e5-9d70-feff819cdc9f','378949ec-65d0-11e5-9d70-feff819cdc9f', 1);
INSERT INTO RESPONSE_DOMAIN_CODE(id, RESPONSEDOMAIN_ID, CODE_ID, RANK) VALUES('05f9a89e-65d1-11e5-9d70-feff819cdc9f', '3789590a-65d0-11e5-9d70-feff819cdc9f','37894d7a-65d0-11e5-9d70-feff819cdc9f', 2);
INSERT INTO RESPONSE_DOMAIN_CODE(id, RESPONSEDOMAIN_ID, CODE_ID, RANK) VALUES('05f9a9c0-65d1-11e5-9d70-feff819cdc9f', '37895cd4-65d0-11e5-9d70-feff819cdc9f','37894f32-65d0-11e5-9d70-feff819cdc9f', 3);
INSERT INTO RESPONSE_DOMAIN_CODE(id, RESPONSEDOMAIN_ID, CODE_ID, RANK) VALUES('05f9abd2-65d1-11e5-9d70-feff819cdc9f', '37895ee6-65d0-11e5-9d70-feff819cdc9f','378953c4-65d0-11e5-9d70-feff819cdc9f', 4);
INSERT INTO RESPONSE_DOMAIN_CODE(id, RESPONSEDOMAIN_ID, CODE_ID, RANK) VALUES('05f9ad12-65d1-11e5-9d70-feff819cdc9f', '378960a8-65d0-11e5-9d70-feff819cdc9f','378955cc-65d0-11e5-9d70-feff819cdc9f', 5);