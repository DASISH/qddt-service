DROP VIEW allrev;
CREATE VIEW allrev (id, rev, revtype, revend, tablename) AS
  SELECT agency_aud.id,
    agency_aud.rev,
    agency_aud.revtype,
    agency_aud.revend,
    'agency_aud'::text AS tablename
  FROM agency_aud
  UNION
  SELECT author_aud.id,
    author_aud.rev,
    author_aud.revtype,
    author_aud.revend,
    'author_aud'::text AS tablename
  FROM author_aud
  UNION
  SELECT authority_aud.id,
    authority_aud.rev,
    authority_aud.revtype,
    authority_aud.revend,
    'authority_aud'::text AS tablename
  FROM authority_aud
  UNION
  SELECT category_aud.id,
    category_aud.rev,
    category_aud.revtype,
    category_aud.revend,
    'category_aud'::text AS tablename
  FROM category_aud
  UNION
  SELECT category_children_aud.category_id AS id,
    category_children_aud.rev,
    category_children_aud.revtype,
    category_children_aud.revend,
         'category_children_aud'::text AS tablename
  FROM category_children_aud
  UNION
  SELECT code_aud.responsedomain_id AS id,
    code_aud.rev,
    code_aud.revtype,
    code_aud.revend,
         'code_aud'::text AS tablename
  FROM code_aud
  UNION
  SELECT concept_aud.id,
    concept_aud.rev,
    concept_aud.revtype,
    concept_aud.revend,
    'concept_aud'::text AS tablename
  FROM concept_aud
  UNION
  SELECT concept_question_item_aud.element_id,
    concept_question_item_aud.rev,
    concept_question_item_aud.revtype,
    concept_question_item_aud.revend,
    'concept_question_item_aud'::text AS tablename
  FROM concept_question_item_aud
  UNION
  SELECT control_construct_aud.id,
    control_construct_aud.rev,
    control_construct_aud.revtype,
    control_construct_aud.revend,
    'control_construct_aud'::text AS tablename
  FROM control_construct_aud
  UNION
  SELECT control_construct_instruction_aud.instruction_id AS id,
    control_construct_instruction_aud.rev,
    control_construct_instruction_aud.revtype,
    control_construct_instruction_aud.revend,
         'control_construct_instruction_aud'::text AS tablename
  FROM control_construct_instruction_aud
  UNION
  SELECT control_construct_universe_aud.universe_id AS id,
    control_construct_universe_aud.rev,
    control_construct_universe_aud.revtype,
    control_construct_universe_aud.revend,
         'control_construct_universe_aud'::text AS tablename
  FROM control_construct_universe_aud
  UNION
  SELECT instruction_aud.id,
    instruction_aud.rev,
    instruction_aud.revtype,
    instruction_aud.revend,
    'instruction_aud'::text AS tablename
  FROM instruction_aud
  UNION
  SELECT instrument_aud.id,
    instrument_aud.rev,
    instrument_aud.revtype,
    instrument_aud.revend,
    'instrument_aud'::text AS tablename
  FROM instrument_aud
  UNION
  SELECT instrument_element_aud.element_id AS id,
    instrument_element_aud.rev,
    instrument_element_aud.revtype,
    instrument_element_aud.revend,
         'instrument_control_construct_aud'::text AS tablename
  FROM instrument_element_aud
  UNION
  SELECT other_material_aud.id,
    other_material_aud.rev,
    other_material_aud.revtype,
    other_material_aud.revend,
    'other_material_aud'::text AS tablename
  FROM other_material_aud
  UNION
  SELECT other_material_other_material_aud.id,
    other_material_other_material_aud.rev,
    other_material_other_material_aud.revtype,
    other_material_other_material_aud.revend,
    'other_material_other_material_aud'::text AS tablename
  FROM other_material_other_material_aud
  UNION
  SELECT publication_aud.id,
    publication_aud.rev,
    publication_aud.revtype,
    publication_aud.revend,
    'publication_aud'::text AS tablename
  FROM publication_aud
  UNION
  SELECT publication_element_aud.element_id,
    publication_element_aud.rev,
    publication_element_aud.revtype,
    publication_element_aud.revend,
    'publication_element_aud'::text AS tablename
  FROM publication_element_aud
  UNION
  SELECT question_item_aud.id,
    question_item_aud.rev,
    question_item_aud.revtype,
    question_item_aud.revend,
    'question_item_aud'::text AS tablename
  FROM question_item_aud
  UNION
  SELECT responsedomain_aud.id,
    responsedomain_aud.rev,
    responsedomain_aud.revtype,
    responsedomain_aud.revend,
    'responsedomain_aud'::text AS tablename
  FROM responsedomain_aud
  UNION
  SELECT study_aud.id,
    study_aud.rev,
    study_aud.revtype,
    study_aud.revend,
    'study_aud'::text AS tablename
  FROM study_aud
  UNION
  SELECT study_authors_aud.author_id AS id,
    study_authors_aud.rev,
    study_authors_aud.revtype,
    study_authors_aud.revend,
         'study_authors_aud'::text AS tablename
  FROM study_authors_aud
  UNION
  SELECT survey_program_aud.id,
    survey_program_aud.rev,
    survey_program_aud.revtype,
    survey_program_aud.revend,
    'survey_program_aud'::text AS tablename
  FROM survey_program_aud
  UNION
  SELECT survey_program_authors_aud.author_id AS id,
    survey_program_authors_aud.rev,
    survey_program_authors_aud.revtype,
    survey_program_authors_aud.revend,
         'survey_program_authors_aud'::text AS tablename
  FROM survey_program_authors_aud
  UNION
  SELECT topic_group_aud.id,
    topic_group_aud.rev,
    topic_group_aud.revtype,
    topic_group_aud.revend,
    'topic_group_aud'::text AS tablename
  FROM topic_group_aud
  UNION
  SELECT topic_group_authors_aud.author_id AS id,
    topic_group_authors_aud.rev,
    topic_group_authors_aud.revtype,
    topic_group_authors_aud.revend,
         'topic_group_authors_aud'::text AS tablename
  FROM topic_group_authors_aud
  UNION
  SELECT topic_group_question_item_aud.element_id,
    topic_group_question_item_aud.rev,
    topic_group_question_item_aud.revtype,
    topic_group_question_item_aud.revend,
    'topic_group_question_item_aud'::text AS tablename
  FROM topic_group_question_item_aud
  UNION
  SELECT universe_aud.id,
    universe_aud.rev,
    universe_aud.revtype,
    universe_aud.revend,
    'universe_aud'::text AS tablename
  FROM universe_aud
  UNION
  SELECT user_account_aud.id,
    user_account_aud.rev,
    user_account_aud.revtype,
    user_account_aud.revend,
    'user_account_aud'::text AS tablename
  FROM user_account_aud
  UNION
  SELECT user_authority_aud.user_id AS id,
    user_authority_aud.rev,
    user_authority_aud.revtype,
    user_authority_aud.revend,
         'user_authority_aud'::text AS tablename
  FROM user_authority_aud;

CREATE VIEW uuidpath (id, path) AS
  SELECT c.id,
    '/cateories'::text AS path
  FROM category c
  WHERE ((c.category_kind)::text = 'CATEGORY'::text)
  UNION
  SELECT c.id,
    '/missing'::text AS path
  FROM category c
  WHERE ((c.category_kind)::text = 'MISSING_GROUP'::text)
  UNION
  SELECT cc.id,
    '/questions'::text AS path
  FROM control_construct cc
  WHERE ((cc.control_construct_kind)::text = 'QUESTION_CONSTRUCT'::text)
  UNION
  SELECT cc.id,
    '/sequences'::text AS path
  FROM control_construct cc
  WHERE ((cc.control_construct_kind)::text = 'SEQUENCE_CONSTRUCT'::text)
  UNION
  SELECT instrument.id,
    '/instruments'::text AS path
  FROM instrument
  UNION
  SELECT publication.id,
    '/publications'::text AS path
  FROM publication
  UNION
  SELECT question_item.id,
    '/questionitems'::text AS path
  FROM question_item
  UNION
  SELECT responsedomain.id,
    '/responsedomains'::text AS path
  FROM responsedomain
  UNION
  SELECT concept.id,
    '/concept'::text AS path
  FROM concept
  UNION
  SELECT topic_group.id,
    '/module'::text AS path
  FROM topic_group
  UNION
  SELECT study.id,
    '/study'::text AS path
  FROM study
  UNION
  SELECT survey_program.id,
    '/survey'::text AS path
  FROM survey_program;



create view UAR as
  SELECT ua.id, ua.email, ua.username, a.name, a.authority
  FROM user_account ua
    left join user_authority au on au.user_id = ua.id
    left join authority a on au.authority_id = a.id
  where ua.is_enabled;

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

insert into audit.revinfo (revtstmp, rev) values(1498471892683,1);

--Add primary agency
INSERT INTO audit.agency_aud (id, updated, name, rev, revtype) VALUES('1359ded1-9f18-11e5-8994-feff819cdc9f','2018-01-01', 'Admin-qddt',1, 0);
INSERT INTO audit.agency_aud (id, updated, name, rev, revtype) VALUES('1359ded2-9f18-11e5-8994-feff819cdc9f','2018-01-01', 'int.esseric',1, 0);
INSERT INTO audit.agency_aud (id, updated, name, rev, revtype) VALUES('1359ded3-9f18-11e5-8994-feff819cdc9f','2018-01-01', 'Guest',1,0);

--Add two deaudit.mo accounts
--admin:passaudit.word & user:password (bcrypt(10) passwords)
INSERT INTO audit.user_account_aud(id, username, password, email, agency_id,is_enabled, rev, revtype) VALUES('83d4c311-4ff9-11e5-885d-feff819cdc9f', 'admin', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'admin@example.org',  '1359ded1-9f18-11e5-8994-feff819cdc9f',true,1, 0);
INSERT INTO audit.user_account_aud(id, username, password, email, agency_id,is_enabled, rev, revtype) VALUES('83d4c312-4ff9-11e5-885d-feff819cdc9f', 'stig', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'stig.norland@nsd.no', '1359ded2-9f18-11e5-8994-feff819cdc9f',true,1, 0);
INSERT INTO audit.user_account_aud(id, username, password, email, agency_id,is_enabled, rev, revtype) VALUES('83d4c313-4ff9-11e5-885d-feff819cdc9f', 'editor', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'editor@example.org', '1359ded2-9f18-11e5-8994-feff819cdc9f',true,1, 0);
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
INSERT INTO audit.user_authority_aud (user_id, authority_id, rev, revtype) VALUES('83d4c315-4ff9-11e5-885d-feff819cdc9f', '9bec2d6b-4ff9-11e5-885d-feff819cdc9f',1, 0);
INSERT INTO audit.user_authority_aud (user_id, authority_id, rev, revtype) VALUES('83d4c316-4ff9-11e5-885d-feff819cdc9f', '9bec2d6b-4ff9-11e5-885d-feff819cdc9f',1, 0);
INSERT INTO audit.user_authority_aud (user_id, authority_id, rev, revtype) VALUES('83d4c317-4ff9-11e5-885d-feff819cdc9f', '9bec2d6b-4ff9-11e5-885d-feff819cdc9f',1, 0);
INSERT INTO audit.user_authority_aud (user_id, authority_id, rev, revtype) VALUES('83d4c318-4ff9-11e5-885d-feff819cdc9f', '9bec2d6c-4ff9-11e5-885d-feff819cdc9f',1, 0);
INSERT INTO audit.user_authority_aud (user_id, authority_id, rev, revtype) VALUES('83d4c319-4ff9-11e5-885d-feff819cdc9f', '9bec2d6d-4ff9-11e5-885d-feff819cdc9f',1, 0);
INSERT INTO audit.user_authority_aud (user_id, authority_id, rev, revtype) VALUES('83d4c31a-4ff9-11e5-885d-feff819cdc9f', '9bec2d6e-4ff9-11e5-885d-feff819cdc9f',1, 0);


--- merge scripts...
drop sequence hibernate_sequence;
CREATE SEQUENCE hibernate_sequence INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 5 NO CYCLE;


INSERT INTO publication_status (id, publication_status_id, publication_status_idx, agency_id, published, label, description) VALUES (0, null, 0, '1359ded2-9f18-11e5-8994-feff819cdc9f', 'NOT_PUBLISHED', 'No publication', null);
INSERT INTO publication_status (id, publication_status_id, publication_status_idx, agency_id, published, label, description) VALUES (1, null, 1, '1359ded2-9f18-11e5-8994-feff819cdc9f', 'INTERNAL_PUBLICATION', 'Internal publication', null);
INSERT INTO publication_status (id, publication_status_id, publication_status_idx, agency_id, published, label, description) VALUES (2, null, 2, '1359ded2-9f18-11e5-8994-feff819cdc9f', 'EXTERNAL_PUBLICATION', 'External publication', null);
INSERT INTO publication_status (id, publication_status_id, publication_status_idx, agency_id, published, label, description) VALUES (3, 1, 0, '1359ded2-9f18-11e5-8994-feff819cdc9f', 'INTERNAL_PUBLICATION', 'Designmeeting1', 'Elements shared after first meeting to discuss questionnaire.');
INSERT INTO publication_status (id, publication_status_id, publication_status_idx, agency_id, published, label, description) VALUES (5, 1, 2, '1359ded2-9f18-11e5-8994-feff819cdc9f', 'INTERNAL_PUBLICATION', 'Designmeeting3', 'Elements shared  after third meeting to discuss questionnaire.');
INSERT INTO publication_status (id, publication_status_id, publication_status_idx, agency_id, published, label, description) VALUES (4, 1, 1, '1359ded2-9f18-11e5-8994-feff819cdc9f', 'INTERNAL_PUBLICATION', 'Designmeeting2', 'Elements shared after second meeting to discuss questionnaire.');
INSERT INTO publication_status (id, publication_status_id, publication_status_idx, agency_id, published, label, description) VALUES (11, 1, 8, '1359ded2-9f18-11e5-8994-feff819cdc9f', 'INTERNAL_PUBLICATION', 'No Milestone', 'Use for publication of elements between key milestones.');
INSERT INTO publication_status (id, publication_status_id, publication_status_idx, agency_id, published, label, description) VALUES (10, 1, 7, '1359ded2-9f18-11e5-8994-feff819cdc9f', 'INTERNAL_PUBLICATION', 'FinalSource – SQP/TMT', 'Elements agreed as going into the final source questionnaire.');
INSERT INTO publication_status (id, publication_status_id, publication_status_idx, agency_id, published, label, description) VALUES (9, 1, 6, '1359ded2-9f18-11e5-8994-feff819cdc9f', 'INTERNAL_PUBLICATION', 'PostPilot', 'Elements reviewed on basis of the results from the pilot.');
INSERT INTO publication_status (id, publication_status_id, publication_status_idx, agency_id, published, label, description) VALUES (8, 1, 5, '1359ded2-9f18-11e5-8994-feff819cdc9f', 'INTERNAL_PUBLICATION', 'Pilot – SQP/TMT', 'Elements agreed for pilot, export to SQP and translation');
INSERT INTO publication_status (id, publication_status_id, publication_status_idx, agency_id, published, label, description) VALUES (7, 1, 4, '1359ded2-9f18-11e5-8994-feff819cdc9f', 'INTERNAL_PUBLICATION', 'PostEarlyTesting', 'Elements reviewed on basis of the results from the early testing.');
INSERT INTO publication_status (id, publication_status_id, publication_status_idx, agency_id, published, label, description) VALUES (6, 1, 3, '1359ded2-9f18-11e5-8994-feff819cdc9f', 'INTERNAL_PUBLICATION', 'Earlytesting - SQP/TMT', 'Elements agreed for early pre-testing, export to SQP and translation.');
INSERT INTO publication_status (id, publication_status_id, publication_status_idx, agency_id, published, label, description) VALUES (12, 2, 0, '1359ded2-9f18-11e5-8994-feff819cdc9f', 'EXTERNAL_PUBLICATION', 'Export to Public History', 'In addition to the final elements, the development history will be made available to the public.');
INSERT INTO publication_status (id, publication_status_id, publication_status_idx, agency_id, published, label, description) VALUES (14, 2, 2, '1359ded2-9f18-11e5-8994-feff819cdc9f', 'EXTERNAL_PUBLICATION', 'Export to QVD', 'Once finalized, elements will be exported to the QVDB to be made publically available');
INSERT INTO publication_status (id, publication_status_id, publication_status_idx, agency_id, published, label, description) VALUES (13, 2, 1, '1359ded2-9f18-11e5-8994-feff819cdc9f', 'EXTERNAL_PUBLICATION', 'Export to Public', 'Elements agreed as going into the final source questionnaire.');
INSERT INTO publication_status (id, publication_status_id, publication_status_idx, agency_id, published, label, description) VALUES (15, 0, 0, '1359ded2-9f18-11e5-8994-feff819cdc9f', 'NOT_PUBLISHED', 'Not Published', 'Elements and discussion made available for key members of a questionnaire design sub group, but not designed to be published internally ');
