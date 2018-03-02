--Add primary agency
INSERT INTO agency (id, updated, name) VALUES('1359dede-9f18-11e5-8994-feff819cdc9f','2018-01-01', 'NSD-qddt');

--Add two demo accounts
--admin:password & user:password (bcrypt(10) passwords)
INSERT INTO user_account(id, username, password, email, agency_id,is_enabled) VALUES('83d4c3aa-4ff9-11e5-885d-feff819cdc9f', 'admin', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'admin@example.org',  '1359dede-9f18-11e5-8994-feff819cdc9f',true);
INSERT INTO user_account(id, username, password, email, agency_id,is_enabled) VALUES('83d4c3ba-4ff9-11e5-885d-feff819cdc9f', 'stig', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'stig.norland@nsd.no', '1359dede-9f18-11e5-8994-feff819cdc9f',true);
INSERT INTO user_account(id, username, password, email, agency_id,is_enabled) VALUES('83d4c3ca-4ff9-11e5-885d-feff819cdc9f', 'user', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'user@example.org',    '1359dede-9f18-11e5-8994-feff819cdc9f',true);
INSERT INTO user_account(id, username, password, email, agency_id,is_enabled) VALUES('83d4c3da-4ff9-11e5-885d-feff819cdc9f', 'review', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'review@example.org','1359dede-9f18-11e5-8994-feff819cdc9f',true);
INSERT INTO user_account(id, username, password, email, agency_id,is_enabled) VALUES('83d4c3ea-4ff9-11e5-885d-feff819cdc9f', 'guest', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'guestw@example.org', '1359dede-9f18-11e5-8994-feff819cdc9f',true);
INSERT INTO user_account(id, username, password, email, agency_id,is_enabled) VALUES('83d4c3fa-4ff9-11e5-885d-feff819cdc9f', 'editor', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'editor@example.org', '1359dede-9f18-11e5-8994-feff819cdc9f',true);

--Create ADMIN and USER authorities
INSERT INTO authority (id, name, authority) VALUES('9bec2d6a-4ff9-11e5-885d-feff819cdc9f', 'ToolAdmin'       , 'ROLE_ADMIN'  );
INSERT INTO authority (id, name, authority) VALUES('9bec2d6b-4ff9-11e5-885d-feff819cdc9f', 'Editor'          , 'ROLE_EDITOR' );
INSERT INTO authority (id, name, authority) VALUES('9bec2d6c-4ff9-11e5-885d-feff819cdc9f', 'ConceptualEditor', 'ROLE_CONCEPT');
INSERT INTO authority (id, name, authority) VALUES('9bec2d6d-4ff9-11e5-885d-feff819cdc9f', 'Viewer'          , 'ROLE_VIEW'   );
INSERT INTO authority (id, name, authority) VALUES('9bec2d6e-4ff9-11e5-885d-feff819cdc9f', 'Guest'           , 'ROLE_GUEST'  );


--Set up admin authorities
--ToolAdmin
INSERT INTO user_authority (user_id, authority_id) VALUES('83d4c3aa-4ff9-11e5-885d-feff819cdc9f', '9bec2d6a-4ff9-11e5-885d-feff819cdc9f');
INSERT INTO user_authority (user_id, authority_id) VALUES('83d4c3ba-4ff9-11e5-885d-feff819cdc9f', '9bec2d6a-4ff9-11e5-885d-feff819cdc9f');
--Editor
INSERT INTO user_authority (user_id, authority_id) VALUES('83d4c3ca-4ff9-11e5-885d-feff819cdc9f', '9bec2d6b-4ff9-11e5-885d-feff819cdc9f');
INSERT INTO user_authority (user_id, authority_id) VALUES('83d4c3ba-4ff9-11e5-885d-feff819cdc9f', '9bec2d6b-4ff9-11e5-885d-feff819cdc9f');
--Conceptua
INSERT INTO user_authority (user_id, authority_id) VALUES('83d4c3fa-4ff9-11e5-885d-feff819cdc9f', '9bec2d6c-4ff9-11e5-885d-feff819cdc9f');
--Viewer
INSERT INTO user_authority (user_id, authority_id) VALUES('83d4c3da-4ff9-11e5-885d-feff819cdc9f', '9bec2d6d-4ff9-11e5-885d-feff819cdc9f');
--Guest
INSERT INTO user_authority (user_id, authority_id) VALUES('83d4c3ea-4ff9-11e5-885d-feff819cdc9f', '9bec2d6e-4ff9-11e5-885d-feff819cdc9f');



insert into audit.revinfo (revtstmp, rev) values(1498471892683,1);

INSERT INTO audit.agency_aud (id, rev, revtype, name) VALUES('1359dede-9f18-11e5-8994-feff819cdc9f',1, 0, 'NSD-qddt');

INSERT INTO audit.user_account_aud(id, username, password, email, agency_id,is_enabled,rev, revtype) VALUES('83d4c034-4ff9-11e5-885d-feff819cdc9f', 'admin', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'admin@example.org', '1359dede-9f18-11e5-8994-feff819cdc9f',true,1,0);
INSERT INTO audit.user_account_aud(id, username, password, email, agency_id,is_enabled,rev, revtype) VALUES('83d4c39a-4ff9-11e5-885d-feff819cdc9f', 'user@example.org', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'user@example.org', '1359dede-9f18-11e5-8994-feff819cdc9f',true,1,0);
INSERT INTO audit.user_account_aud(id, username, password, email, agency_id,is_enabled,rev, revtype) VALUES('83d4c3aa-4ff9-11e5-885d-feff819cdc9f', 'stig', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'stig.norland@nsd.no', '1359dede-9f18-11e5-8994-feff819cdc9f',true,1,0);
INSERT INTO audit.user_account_aud(id, username, password, email, agency_id,is_enabled,rev, revtype) VALUES('83d4c3ba-4ff9-11e5-885d-feff819cdc9f', 'review', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'review@example.org', '1359dede-9f18-11e5-8994-feff819cdc9f',true,1,0);

INSERT INTO audit.user_authority_aud (user_id, authority_id,rev, revtype) VALUES('83d4c034-4ff9-11e5-885d-feff819cdc9f', '9bec2c02-4ff9-11e5-885d-feff819cdc9f',1,0);
INSERT INTO audit.user_authority_aud (user_id, authority_id,rev, revtype) VALUES('83d4c034-4ff9-11e5-885d-feff819cdc9f', '9bec2d6a-4ff9-11e5-885d-feff819cdc9f',1,0);
INSERT INTO audit.user_authority_aud (user_id, authority_id,rev, revtype) VALUES('83d4c39a-4ff9-11e5-885d-feff819cdc9f', '9bec2d6a-4ff9-11e5-885d-feff819cdc9f',1,0);
INSERT INTO audit.user_authority_aud (user_id, authority_id,rev, revtype) VALUES('83d4c30a-4ff9-11e5-885d-feff819cdc9f', '9bec2d6b-4ff9-11e5-885d-feff819cdc9f',1,0);
INSERT INTO audit.user_authority_aud (user_id, authority_id,rev, revtype) VALUES('83d4c3aa-4ff9-11e5-885d-feff819cdc9f', '9bec2d6b-4ff9-11e5-885d-feff819cdc9f',1,0);
INSERT INTO audit.user_authority_aud (user_id, authority_id,rev, revtype) VALUES('83d4c3ba-4ff9-11e5-885d-feff819cdc9f', '9bec2d6c-4ff9-11e5-885d-feff819cdc9f',1,0);


INSERT INTO audit.authority_aud (id, name, authority,rev, revtype) VALUES ('9bec2c02-4ff9-11e5-885d-feff819cdc9f', 'admin', 'ROLE_ADMIN',1,0);
INSERT INTO audit.authority_aud (id, name, authority,rev, revtype) VALUES('9bec2d6a-4ff9-11e5-885d-feff819cdc9f', 'user', 'ROLE_USER',1,0);
INSERT INTO audit.authority_aud (id, authority, name,rev, revtype) VALUES ('9bec2d6b-4ff9-11e5-885d-feff819cdc9f','ROLE_SUPER','AgencyAdmin',1,0);
INSERT INTO audit.authority_aud (id, authority, name,rev, revtype) VALUES ('9bec2d6c-4ff9-11e5-885d-feff819cdc9f','ROLE_LIMITED','Reviewer',1,0);