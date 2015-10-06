// Add two demo accounts
// admin:password & user:password (bcrypt(10) passwords)
INSERT INTO user_account(id, username, password, email) VALUES('83d4c034-4ff9-11e5-885d-feff819cdc9f', 'admin', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'admin@example.org');
INSERT INTO user_account(id, username, password, email) VALUES('83d4c39a-4ff9-11e5-885d-feff819cdc9f', 'user', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'user@example.org');

// Create ADMIN and USER authorities
INSERT INTO authority (id, name, authority) VALUES ('9bec2c02-4ff9-11e5-885d-feff819cdc9f', 'admin', 'ROLE_ADMIN');
INSERT INTO authority (id, name, authority) VALUES('9bec2d6a-4ff9-11e5-885d-feff819cdc9f', 'user', 'ROLE_USER');

// Set up admin authorities
INSERT INTO user_authority (user_id, authority_id) VALUES('83d4c034-4ff9-11e5-885d-feff819cdc9f', '9bec2c02-4ff9-11e5-885d-feff819cdc9f');
INSERT INTO user_authority (user_id, authority_id) VALUES('83d4c034-4ff9-11e5-885d-feff819cdc9f', '9bec2d6a-4ff9-11e5-885d-feff819cdc9f');
// Set up user authorities
INSERT INTO user_authority (user_id, authority_id) VALUES('83d4c39a-4ff9-11e5-885d-feff819cdc9f', '9bec2d6a-4ff9-11e5-885d-feff819cdc9f');


-- INSERT INTO SURVEY(survey_name) VALUES('MORE SURVEY');
-- INSERT INTO SURVEY(survey_name) VALUES('SUCH SURVEY');
-- INSERT INTO SURVEY(survey_name) VALUES('MUCH WOW SURVEY');
-- INSERT INTO SURVEY(survey_name) VALUES('DOGE SURVEY');
-- INSERT INTO COMMENT (COMMENT, CREATED, USER_ID, SURVEY_ID) VALUES('ANOTHER COMMENT YES', '2015-02-10 17:28:22.952', 2, 1);

// POPULATE THE RESPONSE PACKAGE WITH DATA.
INSERT INTO code(id,created, category,change_reason,name) VALUES('378949ec-65d0-11e5-9d70-feff819cdc9f','01-01-2015', 'Dummy Category Alpha','CREATED','#TEST');
INSERT INTO code(id,created, category,change_reason,name) VALUES('37894d7a-65d0-11e5-9d70-feff819cdc9f','01-01-2015', 'Dummy Category Beta','CREATED','#TEST');
INSERT INTO code(id,created, category,change_reason,name) VALUES('37894f32-65d0-11e5-9d70-feff819cdc9f','01-01-2015', 'Dummy Category Charlie','CREATED','#TEST');
INSERT INTO code(id,created, category,change_reason,name) VALUES('378953c4-65d0-11e5-9d70-feff819cdc9f','01-01-2015', 'Dummy Category Delta','CREATED','#TEST');
INSERT INTO code(id,created, category,change_reason,name) VALUES('378955cc-65d0-11e5-9d70-feff819cdc9f','01-01-2015', 'Dummy Category Echo','CREATED','#TEST');

INSERT INTO responsedomain(id,created,change_reason,name,response_kind) VALUES('d5dbaebe-65d0-11e5-9d70-feff819cdc9f','01-01-2015','CREATED','TestDomianCode','Code');
INSERT INTO responsedomain(id,created,change_reason,name,response_kind) VALUES('d5dbb12a-65d0-11e5-9d70-feff819cdc9f','01-01-2015','CREATED','TestDomianCategory','Category');
INSERT INTO responsedomain(id,created,change_reason,name,response_kind) VALUES('d5dbb332-65d0-11e5-9d70-feff819cdc9f','01-01-2015','CREATED','TestDomianNumeric','Numeric');


INSERT INTO responsedomain_code(id,created,change_reason, responsedomain_id, code_id, code_idx, code_value) VALUES('05f9a484-65d1-11e5-9d70-feff819cdc9f','01-01-2015','CREATED', 'd5dbaebe-65d0-11e5-9d70-feff819cdc9f','378949ec-65d0-11e5-9d70-feff819cdc9f', 1,'11');
INSERT INTO responsedomain_code(id,created,change_reason, responsedomain_id, code_id, code_idx, code_value) VALUES('05f9a89e-65d1-11e5-9d70-feff819cdc9f','01-01-2015','CREATED', 'd5dbaebe-65d0-11e5-9d70-feff819cdc9f','37894d7a-65d0-11e5-9d70-feff819cdc9f', 2,'12');
INSERT INTO responsedomain_code(id,created,change_reason, responsedomain_id, code_id, code_idx, code_value) VALUES('05f9a9c0-65d1-11e5-9d70-feff819cdc9f','01-01-2015','CREATED', 'd5dbaebe-65d0-11e5-9d70-feff819cdc9f','37894f32-65d0-11e5-9d70-feff819cdc9f', 3,'13');
INSERT INTO responsedomain_code(id,created,change_reason, responsedomain_id, code_id, code_idx, code_value) VALUES('05f9abd2-65d1-11e5-9d70-feff819cdc9f','01-01-2015','CREATED', 'd5dbaebe-65d0-11e5-9d70-feff819cdc9f','378953c4-65d0-11e5-9d70-feff819cdc9f', 4,'14');
INSERT INTO responsedomain_code(id,created,change_reason, responsedomain_id, code_id, code_idx, code_value) VALUES('05f9ad12-65d1-11e5-9d70-feff819cdc9f','01-01-2015','CREATED', 'd5dbaebe-65d0-11e5-9d70-feff819cdc9f','378955cc-65d0-11e5-9d70-feff819cdc9f', 5,'15');


// POPULATE SURVEY DUMMY DATA
-- INSERT INTO survey_program(id,name,change_reason) VALUES('a9fe6c58-5038-1fe0-8150-382001480000', 'THE SURVEY','CREATED');
-- INSERT INTO study(id,change_reason,name,survey_id) VALUES('a9fe6c58-5038-1fe0-8150-382001480001', 'CREATED','myStudy','a9fe6c58-5038-1fe0-8150-382001480000');
-- INSERT INTO topic_group(id,change_reason,name,study_id) VALUES('a9fe6c58-5038-1fe0-8150-382001480002','CREATED','Module test','a9fe6c58-5038-1fe0-8150-382001480001');
-- INSERT INTO concept(id,change_reason,name,topicgroup_id) VALUES('a9fe6c58-5038-1fe0-8150-382001480003','CREATED','Test Concept','a9fe6c58-5038-1fe0-8150-382001480002');
-- INSERT INTO question(id,change_reason,grid_idx,name,question,responsedomain_id) VALUES('a9fe6c58-5038-1fe0-8150-382001480004','CREATED',0,'How to DDI?','How would you DDI if you could?','d5dbaebe-65d0-11e5-9d70-feff819cdc9f');
-- INSERT INTO concept_question(question_id,concept_id) VALUES('a9fe6c58-5038-1fe0-8150-382001480004','a9fe6c58-5038-1fe0-8150-382001480003')
