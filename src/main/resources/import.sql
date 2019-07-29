DROP TABLE IF EXISTS public.project_archived;
DROP VIEW IF  EXISTS public.project_archived;

DROP TABLE IF EXISTS public.project_archived_hierarchy;
DROP VIEW IF  EXISTS public.project_archived_hierarchy;

DROP TABLE IF EXISTS public.uar;
DROP VIEW IF  EXISTS public.uar;

DROP TABLE IF EXISTS public.uuidpath;
DROP VIEW IF  EXISTS public.uuidpath;

DROP TABLE IF EXISTS public.allrev;
DROP VIEW IF  EXISTS public.allrev;

CREATE VIEW public.allrev (id, rev, revtype, revend, tablename) AS
  SELECT agency_aud.id,
    agency_aud.rev,
    agency_aud.revtype,
    agency_aud.revend,
    'agency_aud'::text AS tablename
  FROM audit.agency_aud
  UNION
  SELECT author_aud.id,
    author_aud.rev,
    author_aud.revtype,
    author_aud.revend,
    'author_aud'::text AS tablename
  FROM audit.author_aud
  UNION
  SELECT authority_aud.id,
    authority_aud.rev,
    authority_aud.revtype,
    authority_aud.revend,
    'authority_aud'::text AS tablename
  FROM audit.authority_aud
  UNION
  SELECT category_aud.id,
    category_aud.rev,
    category_aud.revtype,
    category_aud.revend,
    'category_aud'::text AS tablename
  FROM audit.category_aud
  UNION
  SELECT category_children_aud.category_id AS id,
    category_children_aud.rev,
    category_children_aud.revtype,
    category_children_aud.revend,
         'category_children_aud'::text AS tablename
  FROM audit.category_children_aud
  UNION
  SELECT code_aud.responsedomain_id AS id,
    code_aud.rev,
    code_aud.revtype,
    code_aud.revend,
         'code_aud'::text AS tablename
  FROM audit.code_aud
  UNION
  SELECT concept_aud.id,
    concept_aud.rev,
    concept_aud.revtype,
    concept_aud.revend,
    'concept_aud'::text AS tablename
  FROM audit.concept_aud
  UNION
  SELECT concept_question_item_aud.element_id,
    concept_question_item_aud.rev,
    concept_question_item_aud.revtype,
    concept_question_item_aud.revend,
    'concept_question_item_aud'::text AS tablename
  FROM audit.concept_question_item_aud
  UNION
  SELECT control_construct_aud.id,
    control_construct_aud.rev,
    control_construct_aud.revtype,
    control_construct_aud.revend,
    'control_construct_aud'::text AS tablename
  FROM audit.control_construct_aud
  UNION
  SELECT control_construct_instruction_aud.instruction_id AS id,
    control_construct_instruction_aud.rev,
    control_construct_instruction_aud.revtype,
    control_construct_instruction_aud.revend,
         'control_construct_instruction_aud'::text AS tablename
  FROM audit.control_construct_instruction_aud
  UNION
  SELECT control_construct_universe_aud.universe_id AS id,
    control_construct_universe_aud.rev,
    control_construct_universe_aud.revtype,
    control_construct_universe_aud.revend,
         'control_construct_universe_aud'::text AS tablename
  FROM audit.control_construct_universe_aud
  UNION
  SELECT instruction_aud.id,
    instruction_aud.rev,
    instruction_aud.revtype,
    instruction_aud.revend,
    'instruction_aud'::text AS tablename
  FROM audit.instruction_aud
  UNION
  SELECT instrument_aud.id,
    instrument_aud.rev,
    instrument_aud.revtype,
    instrument_aud.revend,
    'instrument_aud'::text AS tablename
  FROM audit.instrument_aud
  UNION
  SELECT instrument_element_aud.element_id AS id,
    instrument_element_aud.rev,
    instrument_element_aud.revtype,
    instrument_element_aud.revend,
         'instrument_control_construct_aud'::text AS tablename
  FROM audit.instrument_element_aud
  UNION
  SELECT control_construct_other_material_aud.owner_id as id,
         control_construct_other_material_aud.rev,
         control_construct_other_material_aud.revtype,
         control_construct_other_material_aud.revend,
         'control_construct_other_material_aud'::text AS tablename
  FROM audit.control_construct_other_material_aud
  UNION
  SELECT topic_group_other_material_aud.owner_id as id,
         topic_group_other_material_aud.rev,
         topic_group_other_material_aud.revtype,
         topic_group_other_material_aud.revend,
         'control_construct_other_material_aud'::text AS tablename
  FROM audit.topic_group_other_material_aud
  UNION
  SELECT publication_aud.id,
    publication_aud.rev,
    publication_aud.revtype,
    publication_aud.revend,
    'publication_aud'::text AS tablename
  FROM audit.publication_aud
  UNION
  SELECT publication_element_aud.element_id,
    publication_element_aud.rev,
    publication_element_aud.revtype,
    publication_element_aud.revend,
    'publication_element_aud'::text AS tablename
  FROM audit.publication_element_aud
  UNION
  SELECT question_item_aud.id,
    question_item_aud.rev,
    question_item_aud.revtype,
    question_item_aud.revend,
    'question_item_aud'::text AS tablename
  FROM audit.question_item_aud
  UNION
  SELECT responsedomain_aud.id,
    responsedomain_aud.rev,
    responsedomain_aud.revtype,
    responsedomain_aud.revend,
    'responsedomain_aud'::text AS tablename
  FROM audit.responsedomain_aud
  UNION
  SELECT study_aud.id,
    study_aud.rev,
    study_aud.revtype,
    study_aud.revend,
    'study_aud'::text AS tablename
  FROM audit.study_aud
  UNION
  SELECT study_authors_aud.author_id AS id,
    study_authors_aud.rev,
    study_authors_aud.revtype,
    study_authors_aud.revend,
         'study_authors_aud'::text AS tablename
  FROM audit.study_authors_aud
  UNION
  SELECT survey_program_aud.id,
    survey_program_aud.rev,
    survey_program_aud.revtype,
    survey_program_aud.revend,
    'survey_program_aud'::text AS tablename
  FROM audit.survey_program_aud
  UNION
  SELECT survey_program_authors_aud.author_id AS id,
    survey_program_authors_aud.rev,
    survey_program_authors_aud.revtype,
    survey_program_authors_aud.revend,
         'survey_program_authors_aud'::text AS tablename
  FROM audit.survey_program_authors_aud
  UNION
  SELECT topic_group_aud.id,
    topic_group_aud.rev,
    topic_group_aud.revtype,
    topic_group_aud.revend,
    'topic_group_aud'::text AS tablename
  FROM audit.topic_group_aud
  UNION
  SELECT topic_group_authors_aud.author_id AS id,
    topic_group_authors_aud.rev,
    topic_group_authors_aud.revtype,
    topic_group_authors_aud.revend,
         'topic_group_authors_aud'::text AS tablename
  FROM audit.topic_group_authors_aud
  UNION
  SELECT topic_group_question_item_aud.element_id,
    topic_group_question_item_aud.rev,
    topic_group_question_item_aud.revtype,
    topic_group_question_item_aud.revend,
    'topic_group_question_item_aud'::text AS tablename
  FROM audit.topic_group_question_item_aud
  UNION
  SELECT universe_aud.id,
    universe_aud.rev,
    universe_aud.revtype,
    universe_aud.revend,
    'universe_aud'::text AS tablename
  FROM audit.universe_aud
  UNION
  SELECT user_account_aud.id,
    user_account_aud.rev,
    user_account_aud.revtype,
    user_account_aud.revend,
    'user_account_aud'::text AS tablename
  FROM audit.user_account_aud
  UNION
  SELECT user_authority_aud.user_id AS id,
    user_authority_aud.rev,
    user_authority_aud.revtype,
    user_authority_aud.revend,
         'user_authority_aud'::text AS tablename
  FROM audit.user_authority_aud;

CREATE VIEW public.project_archived (id, path, name, is_archived, parent_id) AS
  SELECT concept.id,
         '/concept'::text AS path,
         concept.name,
         concept.is_archived,
         COALESCE(concept.topicgroup_id, concept.concept_id) AS parent_id
  FROM public.concept
  UNION
  SELECT topic_group.id,
         '/module'::text AS path,
         topic_group.name,
         topic_group.is_archived,
         topic_group.study_id AS parent_id
  FROM public.topic_group
  UNION
  SELECT study.id,
         '/study'::text AS path,
         study.name,
         study.is_archived,
         study.survey_id AS parent_id
  FROM public.study
  UNION
  SELECT survey_program.id,
         '/survey'::text AS path,
         survey_program.name,
         survey_program.is_archived,
         NULL::uuid AS parent_id
  FROM public.survey_program;


CREATE VIEW public.project_archived_hierarchy (id, path, name, is_archived, ancestors) AS
  WITH RECURSIVE tree AS (
    SELECT project_archived.id,
           project_archived.path,
           project_archived.name,
           project_archived.is_archived,
           ARRAY[]::uuid[] AS ancestors
    FROM project_archived
    WHERE (project_archived.parent_id IS NULL)
    UNION ALL
    SELECT project_archived.id,
           project_archived.path AS kind,
           project_archived.name,
           project_archived.is_archived,
           (tree_1.ancestors || project_archived.parent_id)
    FROM project_archived,
         tree tree_1
    WHERE (project_archived.parent_id = tree_1.id)
  )
  SELECT tree.id,
         tree.path,
         tree.name,
         tree.is_archived,
         tree.ancestors
  FROM tree;

CREATE VIEW public.uar (id, email, username, name, authority) AS
  SELECT ua.id,
        ua.email,
        ua.username,
        a.name,
        a.authority
  FROM ((user_account ua
     LEFT JOIN public.user_authority au ON ((au.user_id = ua.id)))
     LEFT JOIN public.authority a ON ((au.authority_id = a.id)))
  WHERE ua.is_enabled;

CREATE VIEW public.uuidpath (id, path, name, user_id) AS
  SELECT c.id,
    '/categories'::text AS path,
    c.name,
    c.user_id
  FROM public.category c
  WHERE ((c.category_kind)::text = 'CATEGORY'::text)
  UNION
  SELECT c.id,
    '/missing'::text AS path,
    c.name,
    c.user_id
  FROM public.category c
  WHERE ((c.category_kind)::text = 'MISSING_GROUP'::text)
  UNION
  SELECT cc.id,
    '/questions'::text AS path,
    cc.name,
    cc.user_id
  FROM public.control_construct cc
  WHERE ((cc.control_construct_kind)::text = 'QUESTION_CONSTRUCT'::text)
  UNION
  SELECT cc.id,
    '/sequences'::text AS path,
    cc.name,
    cc.user_id
  FROM public.control_construct cc
  WHERE ((cc.control_construct_kind)::text = 'SEQUENCE_CONSTRUCT'::text)
  UNION
  SELECT instrument.id,
    '/instruments'::text AS path,
    instrument.name,
    instrument.user_id
  FROM public.instrument
  UNION
  SELECT publication.id,
    '/publications'::text AS path,
    publication.name,
    publication.user_id
  FROM public.publication
  UNION
  SELECT question_item.id,
    '/questionitems'::text AS path,
    question_item.name,
    question_item.user_id
  FROM public.question_item
  UNION
  SELECT responsedomain.id,
    '/responsedomains'::text AS path,
    responsedomain.name,
    responsedomain.user_id
  FROM public.responsedomain
  UNION
  SELECT concept.id,
    '/concept'::text AS path,
    concept.name,
    concept.user_id
  FROM public.concept
  UNION
  SELECT topic_group.id,
    '/module'::text AS path,
    topic_group.name,
    topic_group.user_id
  FROM public.topic_group
  UNION
  SELECT study.id,
    '/study'::text AS path,
    study.name,
    study.user_id
  FROM public.study
  UNION
  SELECT survey_program.id,
    '/survey'::text AS path,
    survey_program.name,
    survey_program.user_id
  FROM public.survey_program;

DROP TABLE IF EXISTS public.change_log;
DROP VIEW IF  EXISTS public.change_log;

CREATE VIEW public.change_log (ref_id, ref_rev, ref_kind, ref_change_kind, ref_modified, ref_modified_by, ref_action, element_id, element_revision, element_kind, name) AS  SELECT ca.id AS ref_id,
    ca.rev AS ref_rev,
    'CONCEPT'::text AS ref_kind,
    ca.change_kind AS ref_change_kind,
    ca.updated AS ref_modified,
    ca.user_id AS ref_modified_by,
    ca.revtype AS ref_action,
    NULL::uuid AS element_id,
    NULL::integer AS element_revision,
    NULL::character varying AS element_kind,
    COALESCE(ca.name, 'Deleted'::character varying) AS name
   FROM audit.concept_aud ca
UNION
 SELECT cc.id AS ref_id,
    cc.rev AS ref_rev,
    'CONTROL_CONSTRUCT'::text AS ref_kind,
    cc.change_kind AS ref_change_kind,
    cc.updated AS ref_modified,
    cc.user_id AS ref_modified_by,
    cc.revtype AS ref_action,
    NULL::uuid AS element_id,
    NULL::integer AS element_revision,
    NULL::character varying AS element_kind,
    COALESCE(cc.name, 'Deleted'::character varying) AS name
   FROM audit.control_construct_aud cc
UNION
 SELECT ai.id AS ref_id,
    ai.rev AS ref_rev,
    'INSTRUMENT'::text AS ref_kind,
    ai.change_kind AS ref_change_kind,
    ai.updated AS ref_modified,
    ai.user_id AS ref_modified_by,
    ai.revtype AS ref_action,
    NULL::uuid AS element_id,
    NULL::integer AS element_revision,
    NULL::character varying AS element_kind,
    COALESCE(ai.name, 'Deleted'::character varying) AS name
   FROM audit.instrument_aud ai
UNION
 SELECT qi.id AS ref_id,
    qi.rev AS ref_rev,
    'QUESTION_ITEM'::text AS ref_kind,
    qi.change_kind AS ref_change_kind,
    qi.updated AS ref_modified,
    qi.user_id AS ref_modified_by,
    qi.revtype AS ref_action,
    NULL::uuid AS element_id,
    NULL::integer AS element_revision,
    NULL::character varying AS element_kind,
    COALESCE(qi.name, 'Deleted'::character varying) AS name
   FROM audit.question_item_aud qi
UNION
 SELECT rd.id AS ref_id,
    rd.rev AS ref_rev,
    'RESPONSEDOMAIN'::text AS ref_kind,
    rd.change_kind AS ref_change_kind,
    rd.updated AS ref_modified,
    rd.user_id AS ref_modified_by,
    rd.revtype AS ref_action,
    NULL::uuid AS element_id,
    NULL::integer AS element_revision,
    NULL::character varying AS element_kind,
    COALESCE(rd.name, 'Deleted'::character varying) AS name
   FROM audit.responsedomain_aud rd
UNION
 SELECT sa.id AS ref_id,
    sa.rev AS ref_rev,
    'STUDY'::text AS ref_kind,
    sa.change_kind AS ref_change_kind,
    sa.updated AS ref_modified,
    sa.user_id AS ref_modified_by,
    sa.revtype AS ref_action,
    NULL::uuid AS element_id,
    NULL::integer AS element_revision,
    NULL::character varying AS element_kind,
    COALESCE(sa.name, 'Deleted'::character varying) AS name
   FROM audit.study_aud sa
UNION
 SELECT asp.id AS ref_id,
    asp.rev AS ref_rev,
    'SURVEY_PROGRAM'::text AS ref_kind,
    asp.change_kind AS ref_change_kind,
    asp.updated AS ref_modified,
    asp.user_id AS ref_modified_by,
    asp.revtype AS ref_action,
    NULL::uuid AS element_id,
    NULL::integer AS element_revision,
    NULL::character varying AS element_kind,
    COALESCE(asp.name, 'Deleted'::character varying) AS name
   FROM audit.survey_program_aud asp
UNION
 SELECT atg.id AS ref_id,
    atg.rev AS ref_rev,
    'TOPIC_GROUP'::text AS ref_kind,
    atg.change_kind AS ref_change_kind,
    atg.updated AS ref_modified,
    atg.user_id AS ref_modified_by,
    atg.revtype AS ref_action,
    NULL::uuid AS element_id,
    NULL::integer AS element_revision,
    NULL::character varying AS element_kind,
    COALESCE(atg.name, 'Deleted'::character varying) AS name
   FROM audit.topic_group_aud atg
UNION
 SELECT ap.id AS ref_id,
    ap.rev AS ref_rev,
    'PUBLICATION'::text AS ref_kind,
    ap.change_kind AS ref_change_kind,
    ap.updated AS ref_modified,
    ap.user_id AS ref_modified_by,
    ap.revtype AS ref_action,
    NULL::uuid AS element_id,
    NULL::integer AS element_revision,
    NULL::character varying AS element_kind,
    COALESCE(ap.name, 'Deleted'::character varying) AS name
   FROM audit.publication_aud ap
UNION
 SELECT cqi.concept_id AS ref_id,
    cqi.rev AS ref_rev,
    'CONCEPT'::text AS ref_kind,
    NULL::character varying AS ref_change_kind,
    NULL::timestamp without time zone AS ref_modified,
    NULL::uuid AS ref_modified_by,
    cqi.revtype AS ref_action,
    cqi.element_id,
    cqi.element_revision,
    cqi.element_kind,
    COALESCE(cqi.name, 'Deleted'::character varying) AS name
   FROM audit.concept_question_item_aud cqi
UNION
 SELECT tgqi.topicgroup_id AS ref_id,
    tgqi.rev AS ref_rev,
    'TOPIC_GROUP'::text AS ref_kind,
    NULL::character varying AS ref_change_kind,
    NULL::timestamp without time zone AS ref_modified,
    NULL::uuid AS ref_modified_by,
    tgqi.revtype AS ref_action,
    tgqi.element_id,
    tgqi.element_revision,
    tgqi.element_kind,
    COALESCE(tgqi.name, 'Deleted'::character varying) AS name
   FROM audit.topic_group_question_item_aud tgqi
UNION
 SELECT ccom.owner_id AS ref_id,
    ccom.rev AS ref_rev,
    'CONTROL_CONSTRUCT'::text AS ref_kind,
    NULL::character varying AS ref_change_kind,
    NULL::timestamp without time zone AS ref_modified,
    NULL::uuid AS ref_modified_by,
    ccom.revtype AS ref_action,
    NULL::uuid AS element_id,
    NULL::integer AS element_revision,
    'OTHER_MATERIAL'::text AS element_kind,
    ccom.original_name AS name
   FROM audit.control_construct_other_material_aud ccom
UNION
 SELECT tgom.owner_id AS ref_id,
    tgom.rev AS ref_rev,
    'TOPIC_GROUP'::text AS ref_kind,
    NULL::character varying AS ref_change_kind,
    NULL::timestamp without time zone AS ref_modified,
    NULL::uuid AS ref_modified_by,
    tgom.revtype AS ref_action,
    NULL::uuid AS element_id,
    NULL::integer AS element_revision,
    'OTHER_MATERIAL'::text AS element_kind,
    tgom.original_name AS name
   FROM audit.topic_group_other_material_aud tgom;


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
