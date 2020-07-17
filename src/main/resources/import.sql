
--Add primary agency
INSERT INTO public.agency (id, updated, name) VALUES('1359ded1-9f18-11e5-8994-feff819cdc9f','2018-01-01', 'Admin-qddt');
INSERT INTO public.agency (id, updated, name) VALUES('1359ded2-9f18-11e5-8994-feff819cdc9f','2018-01-01', 'int.esseric');
INSERT INTO public.agency (id, updated, name) VALUES('1359ded3-9f18-11e5-8994-feff819cdc9f','2018-01-01', 'Guest');

--Add two demo accounts
--admin:password & user:password (bcrypt(10) passwords)
INSERT INTO public.user_account(id, username, password, email, agency_id,is_enabled) VALUES('83d4c311-4ff9-11e5-885d-feff819cdc9f', 'admin', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'admin@example.org',  '1359ded1-9f18-11e5-8994-feff819cdc9f',true);
INSERT INTO public.user_account(id, username, password, email, agency_id,is_enabled) VALUES('83d4c312-4ff9-11e5-885d-feff819cdc9f', 'stig', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'stig.norland@nsd.no', '1359ded2-9f18-11e5-8994-feff819cdc9f',true);
INSERT INTO public.user_account(id, username, password, email, agency_id,is_enabled) VALUES('83d4c313-4ff9-11e5-885d-feff819cdc9f', 'editor', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'editor@example.org', '1359ded2-9f18-11e5-8994-feff819cdc9f',true);
INSERT INTO public.user_account(id, username, password, email, agency_id,is_enabled) VALUES('83d4c314-4ff9-11e5-885d-feff819cdc9f', 'Leah', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'leah.watson@city.ac.uk', '1359ded2-9f18-11e5-8994-feff819cdc9f',true);
INSERT INTO public.user_account(id, username, password, email, agency_id,is_enabled) VALUES('83d4c315-4ff9-11e5-885d-feff819cdc9f', 'Sarah', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'sarah.butt.1@city.ac.uk', '1359ded2-9f18-11e5-8994-feff819cdc9f',true);
INSERT INTO public.user_account(id, username, password, email, agency_id,is_enabled) VALUES('83d4c316-4ff9-11e5-885d-feff819cdc9f', 'Hilde', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'hilde.orten@nsd.no', '1359ded2-9f18-11e5-8994-feff819cdc9f',true);
INSERT INTO public.user_account(id, username, password, email, agency_id,is_enabled) VALUES('83d4c317-4ff9-11e5-885d-feff819cdc9f', 'Benjamin', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'benjamin.beuster@nsd.no', '1359ded2-9f18-11e5-8994-feff819cdc9f',true);
INSERT INTO public.user_account(id, username, password, email, agency_id,is_enabled) VALUES('83d4c318-4ff9-11e5-885d-feff819cdc9f', 'user', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'user@example.org',    '1359ded2-9f18-11e5-8994-feff819cdc9f',true);
INSERT INTO public.user_account(id, username, password, email, agency_id,is_enabled) VALUES('83d4c319-4ff9-11e5-885d-feff819cdc9f', 'review', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'review@example.org','1359ded3-9f18-11e5-8994-feff819cdc9f',true);
INSERT INTO public.user_account(id, username, password, email, agency_id,is_enabled) VALUES('83d4c31a-4ff9-11e5-885d-feff819cdc9f', 'guest', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'guestw@example.org', '1359ded3-9f18-11e5-8994-feff819cdc9f',true);




--Create ADMIN and USER authorities
INSERT INTO public.authority (id, name, authority) VALUES('9bec2d6a-4ff9-11e5-885d-feff819cdc9f', 'ToolAdmin'       , 'ROLE_ADMIN'  );
INSERT INTO public.authority (id, name, authority) VALUES('9bec2d6b-4ff9-11e5-885d-feff819cdc9f', 'Editor'          , 'ROLE_EDITOR' );
INSERT INTO public.authority (id, name, authority) VALUES('9bec2d6c-4ff9-11e5-885d-feff819cdc9f', 'ConceptualEditor', 'ROLE_CONCEPT');
INSERT INTO public.authority (id, name, authority) VALUES('9bec2d6d-4ff9-11e5-885d-feff819cdc9f', 'Viewer'          , 'ROLE_VIEW'   );
INSERT INTO public.authority (id, name, authority) VALUES('9bec2d6e-4ff9-11e5-885d-feff819cdc9f', 'Guest'           , 'ROLE_GUEST'  );


--Set up admin authorities
--ToolAdmin
INSERT INTO public.user_authority (user_id, authority_id) VALUES('83d4c311-4ff9-11e5-885d-feff819cdc9f', '9bec2d6a-4ff9-11e5-885d-feff819cdc9f');
INSERT INTO public.user_authority (user_id, authority_id) VALUES('83d4c312-4ff9-11e5-885d-feff819cdc9f', '9bec2d6a-4ff9-11e5-885d-feff819cdc9f');
--Editor
INSERT INTO public.user_authority (user_id, authority_id) VALUES('83d4c313-4ff9-11e5-885d-feff819cdc9f', '9bec2d6b-4ff9-11e5-885d-feff819cdc9f');
INSERT INTO public.user_authority (user_id, authority_id) VALUES('83d4c314-4ff9-11e5-885d-feff819cdc9f', '9bec2d6b-4ff9-11e5-885d-feff819cdc9f');
INSERT INTO public.user_authority (user_id, authority_id) VALUES('83d4c315-4ff9-11e5-885d-feff819cdc9f', '9bec2d6b-4ff9-11e5-885d-feff819cdc9f');
INSERT INTO public.user_authority (user_id, authority_id) VALUES('83d4c316-4ff9-11e5-885d-feff819cdc9f', '9bec2d6b-4ff9-11e5-885d-feff819cdc9f');
INSERT INTO public.user_authority (user_id, authority_id) VALUES('83d4c317-4ff9-11e5-885d-feff819cdc9f', '9bec2d6b-4ff9-11e5-885d-feff819cdc9f');
--Conceptua
INSERT INTO public.user_authority (user_id, authority_id) VALUES('83d4c318-4ff9-11e5-885d-feff819cdc9f', '9bec2d6c-4ff9-11e5-885d-feff819cdc9f');
--Viewer
INSERT INTO public.user_authority (user_id, authority_id) VALUES('83d4c319-4ff9-11e5-885d-feff819cdc9f', '9bec2d6d-4ff9-11e5-885d-feff819cdc9f');
--Guest
INSERT INTO public.user_authority (user_id, authority_id) VALUES('83d4c31a-4ff9-11e5-885d-feff819cdc9f', '9bec2d6e-4ff9-11e5-885d-feff819cdc9f');

insert into public.revinfo (timestamp, id,user_id) values(1498471892683,1,'83d4c311-4ff9-11e5-885d-feff819cdc9f');

--Add primary agency
INSERT INTO audit.agency_aud (id, updated, name, rev, revtype) VALUES('1359ded1-9f18-11e5-8994-feff819cdc9f','2018-01-01', 'Admin-qddt',1, 0);
INSERT INTO audit.agency_aud (id, updated, name, rev, revtype) VALUES('1359ded2-9f18-11e5-8994-feff819cdc9f','2018-01-01', 'int.esseric',1, 0);
INSERT INTO audit.agency_aud (id, updated, name, rev, revtype) VALUES('1359ded3-9f18-11e5-8994-feff819cdc9f','2018-01-01', 'Guest',1,0);

--Add two deaudit.mo accounts
--admin:passaudit.word & user:password (bcrypt(10) passwords)
INSERT INTO audit.user_account_aud(id, username, password, email, agency_id,is_enabled, rev, revtype) VALUES('83d4c311-4ff9-11e5-885d-feff819cdc9f', 'admin', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'admin@example.org',  '1359ded1-9f18-11e5-8994-feff819cdc9f',true,1, 0);
INSERT INTO audit.user_account_aud(id, username, password, email, agency_id,is_enabled, rev, revtype) VALUES('83d4c312-4ff9-11e5-885d-feff819cdc9f', 'stig', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'stig.norland@nsd.no', '1359ded2-9f18-11e5-8994-feff819cdc9f',true,1, 0);
INSERT INTO audit.user_account_aud(id, username, password, email, agency_id,is_enabled, rev, revtype) VALUES('83d4c313-4ff9-11e5-885d-feff819cdc9f', 'editor', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'editor@example.org', '1359ded2-9f18-11e5-8994-feff819cdc9f',true,1, 0);
INSERT INTO audit.user_account_aud(id, username, password, email, agency_id,is_enabled, rev, revtype) VALUES('83d4c314-4ff9-11e5-885d-feff819cdc9f', 'Leah', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'leah.watson@city.ac.uk', '1359ded2-9f18-11e5-8994-feff819cdc9f',true,1, 0);
INSERT INTO audit.user_account_aud(id, username, password, email, agency_id,is_enabled, rev, revtype) VALUES('83d4c315-4ff9-11e5-885d-feff819cdc9f', 'Sarah', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'sarah.butt.1@city.ac.uk', '1359ded2-9f18-11e5-8994-feff819cdc9f',true,1, 0);
INSERT INTO audit.user_account_aud(id, username, password, email, agency_id,is_enabled, rev, revtype) VALUES('83d4c316-4ff9-11e5-885d-feff819cdc9f', 'Hilde', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'hilde.orten@nsd.no', '1359ded2-9f18-11e5-8994-feff819cdc9f',true,1, 0);
INSERT INTO audit.user_account_aud(id, username, password, email, agency_id,is_enabled, rev, revtype) VALUES('83d4c317-4ff9-11e5-885d-feff819cdc9f', 'Benjamin', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'benjamin.beuster@nsd.no', '1359ded2-9f18-11e5-8994-feff819cdc9f',true,1, 0);
INSERT INTO audit.user_account_aud(id, username, password, email, agency_id,is_enabled, rev, revtype) VALUES('83d4c318-4ff9-11e5-885d-feff819cdc9f', 'user', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'user@example.org',    '1359ded2-9f18-11e5-8994-feff819cdc9f',true,1, 0);
INSERT INTO audit.user_account_aud(id, username, password, email, agency_id,is_enabled, rev, revtype) VALUES('83d4c319-4ff9-11e5-885d-feff819cdc9f', 'review', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'review@example.org','1359ded2-9f18-11e5-8994-feff819cdc9f',true,1, 0);
INSERT INTO audit.user_account_aud(id, username, password, email, agency_id,is_enabled, rev, revtype) VALUES('83d4c31a-4ff9-11e5-885d-feff819cdc9f', 'guest', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'guestw@example.org', '1359ded3-9f18-11e5-8994-feff819cdc9f',true,1, 0);
INSERT INTO audit.authority_aud (id, name, authority, rev, revtype) VALUES('9bec2d6a-4ff9-11e5-885d-feff819cdc9f', 'ToolAdmin'       , 'ROLE_ADMIN'  ,1, 0);
INSERT INTO audit.authority_aud (id, name, authority, rev, revtype) VALUES('9bec2d6b-4ff9-11e5-885d-feff819cdc9f', 'Editor'          , 'ROLE_EDITOR' ,1, 0);
INSERT INTO audit.authority_aud (id, name, authority, rev, revtype) VALUES('9bec2d6c-4ff9-11e5-885d-feff819cdc9f', 'ConceptualEditor', 'ROLE_CONCEPT',1, 0);
INSERT INTO audit.authority_aud (id, name, authority, rev, revtype) VALUES('9bec2d6d-4ff9-11e5-885d-feff819cdc9f', 'Viewer'          , 'ROLE_VIEW'   ,1, 0);
INSERT INTO audit.authority_aud (id, name, authority, rev, revtype) VALUES('9bec2d6e-4ff9-11e5-885d-feff819cdc9f', 'Guest'           , 'ROLE_GUEST'  ,1, 0);
INSERT INTO audit.user_authority_aud (user_id, authority_id, rev, revtype) VALUES('83d4c311-4ff9-11e5-885d-feff819cdc9f', '9bec2d6a-4ff9-11e5-885d-feff819cdc9f',1, 0);
INSERT INTO audit.user_authority_aud (user_id, authority_id, rev, revtype) VALUES('83d4c312-4ff9-11e5-885d-feff819cdc9f', '9bec2d6a-4ff9-11e5-885d-feff819cdc9f',1, 0);
INSERT INTO audit.user_authority_aud (user_id, authority_id, rev, revtype) VALUES('83d4c313-4ff9-11e5-885d-feff819cdc9f', '9bec2d6b-4ff9-11e5-885d-feff819cdc9f',1, 0);
INSERT INTO audit.user_authority_aud (user_id, authority_id, rev, revtype) VALUES('83d4c314-4ff9-11e5-885d-feff819cdc9f', '9bec2d6b-4ff9-11e5-885d-feff819cdc9f',1, 0);
INSERT INTO audit.user_authority_aud (user_id, authority_id, rev, revtype) VALUES('83d4c315-4ff9-11e5-885d-feff819cdc9f', '9bec2d6b-4ff9-11e5-885d-feff819cdc9f',1, 0);
INSERT INTO audit.user_authority_aud (user_id, authority_id, rev, revtype) VALUES('83d4c316-4ff9-11e5-885d-feff819cdc9f', '9bec2d6b-4ff9-11e5-885d-feff819cdc9f',1, 0);
INSERT INTO audit.user_authority_aud (user_id, authority_id, rev, revtype) VALUES('83d4c317-4ff9-11e5-885d-feff819cdc9f', '9bec2d6b-4ff9-11e5-885d-feff819cdc9f',1, 0);
INSERT INTO audit.user_authority_aud (user_id, authority_id, rev, revtype) VALUES('83d4c318-4ff9-11e5-885d-feff819cdc9f', '9bec2d6c-4ff9-11e5-885d-feff819cdc9f',1, 0);
INSERT INTO audit.user_authority_aud (user_id, authority_id, rev, revtype) VALUES('83d4c319-4ff9-11e5-885d-feff819cdc9f', '9bec2d6d-4ff9-11e5-885d-feff819cdc9f',1, 0);
INSERT INTO audit.user_authority_aud (user_id, authority_id, rev, revtype) VALUES('83d4c31a-4ff9-11e5-885d-feff819cdc9f', '9bec2d6e-4ff9-11e5-885d-feff819cdc9f',1, 0);


--- merge scripts...
drop sequence hibernate_sequence;
CREATE SEQUENCE hibernate_sequence INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 5 NO CYCLE;


INSERT INTO public.publication_status (id, publication_status_id, publication_status_idx, agency_id, published, label, description) VALUES (0, null, 0, '1359ded2-9f18-11e5-8994-feff819cdc9f', 'NOT_PUBLISHED', 'No publication', null);
INSERT INTO public.publication_status (id, publication_status_id, publication_status_idx, agency_id, published, label, description) VALUES (1, null, 1, '1359ded2-9f18-11e5-8994-feff819cdc9f', 'INTERNAL_PUBLICATION', 'Internal publication', null);
INSERT INTO public.publication_status (id, publication_status_id, publication_status_idx, agency_id, published, label, description) VALUES (2, null, 2, '1359ded2-9f18-11e5-8994-feff819cdc9f', 'EXTERNAL_PUBLICATION', 'External publication', null);
INSERT INTO public.publication_status (id, publication_status_id, publication_status_idx, agency_id, published, label, description) VALUES (3, 1, 0, '1359ded2-9f18-11e5-8994-feff819cdc9f', 'INTERNAL_PUBLICATION', 'Designmeeting1', 'Elements shared after first meeting to discuss questionnaire.');
INSERT INTO public.publication_status (id, publication_status_id, publication_status_idx, agency_id, published, label, description) VALUES (5, 1, 2, '1359ded2-9f18-11e5-8994-feff819cdc9f', 'INTERNAL_PUBLICATION', 'Designmeeting3', 'Elements shared  after third meeting to discuss questionnaire.');
INSERT INTO public.publication_status (id, publication_status_id, publication_status_idx, agency_id, published, label, description) VALUES (4, 1, 1, '1359ded2-9f18-11e5-8994-feff819cdc9f', 'INTERNAL_PUBLICATION', 'Designmeeting2', 'Elements shared after second meeting to discuss questionnaire.');
INSERT INTO public.publication_status (id, publication_status_id, publication_status_idx, agency_id, published, label, description) VALUES (11, 1, 8, '1359ded2-9f18-11e5-8994-feff819cdc9f', 'INTERNAL_PUBLICATION', 'No Milestone', 'Use for publication of elements between key milestones.');
INSERT INTO public.publication_status (id, publication_status_id, publication_status_idx, agency_id, published, label, description) VALUES (10, 1, 7, '1359ded2-9f18-11e5-8994-feff819cdc9f', 'INTERNAL_PUBLICATION', 'FinalSource – SQP/TMT', 'Elements agreed as going into the final source questionnaire.');
INSERT INTO public.publication_status (id, publication_status_id, publication_status_idx, agency_id, published, label, description) VALUES (9, 1, 6, '1359ded2-9f18-11e5-8994-feff819cdc9f', 'INTERNAL_PUBLICATION', 'PostPilot', 'Elements reviewed on basis of the results from the pilot.');
INSERT INTO public.publication_status (id, publication_status_id, publication_status_idx, agency_id, published, label, description) VALUES (8, 1, 5, '1359ded2-9f18-11e5-8994-feff819cdc9f', 'INTERNAL_PUBLICATION', 'Pilot – SQP/TMT', 'Elements agreed for pilot, export to SQP and translation');
INSERT INTO public.publication_status (id, publication_status_id, publication_status_idx, agency_id, published, label, description) VALUES (7, 1, 4, '1359ded2-9f18-11e5-8994-feff819cdc9f', 'INTERNAL_PUBLICATION', 'PostEarlyTesting', 'Elements reviewed on basis of the results from the early testing.');
INSERT INTO public.publication_status (id, publication_status_id, publication_status_idx, agency_id, published, label, description) VALUES (6, 1, 3, '1359ded2-9f18-11e5-8994-feff819cdc9f', 'INTERNAL_PUBLICATION', 'Earlytesting - SQP/TMT', 'Elements agreed for early pre-testing, export to SQP and translation.');
INSERT INTO public.publication_status (id, publication_status_id, publication_status_idx, agency_id, published, label, description) VALUES (12, 2, 0, '1359ded2-9f18-11e5-8994-feff819cdc9f', 'EXTERNAL_PUBLICATION', 'Export to Public History', 'In addition to the final elements, the development history will be made available to the public.');
INSERT INTO public.publication_status (id, publication_status_id, publication_status_idx, agency_id, published, label, description) VALUES (14, 2, 2, '1359ded2-9f18-11e5-8994-feff819cdc9f', 'EXTERNAL_PUBLICATION', 'Export to QVD', 'Once finalized, elements will be exported to the QVDB to be made publically available');
INSERT INTO public.publication_status (id, publication_status_id, publication_status_idx, agency_id, published, label, description) VALUES (13, 2, 1, '1359ded2-9f18-11e5-8994-feff819cdc9f', 'EXTERNAL_PUBLICATION', 'Export to Public', 'Elements agreed as going into the final source questionnaire.');
INSERT INTO public.publication_status (id, publication_status_id, publication_status_idx, agency_id, published, label, description) VALUES (15, 0, 0, '1359ded2-9f18-11e5-8994-feff819cdc9f', 'NOT_PUBLISHED', 'Not Published', 'Elements and discussion made available for key members of a questionnaire design sub group, but not designed to be published internally ');

