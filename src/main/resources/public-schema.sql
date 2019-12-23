DROP SEQUENCE hibernate_sequence;
CREATE SEQUENCE hibernate_sequence INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 5  NO CYCLE;
DROP TABLE agency;
CREATE TABLE agency (id UUID NOT NULL, updated TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL, name CHARACTER VARYING(50), PRIMARY KEY (id));
DROP TABLE author;
CREATE TABLE author (id UUID NOT NULL, updated TIMESTAMP(6) WITHOUT TIME ZONE, about CHARACTER VARYING(500), authors_affiliation CHARACTER VARYING(255), email CHARACTER VARYING(255), homepage CHARACTER VARYING(255), name CHARACTER VARYING(70) NOT NULL, picture CHARACTER VARYING(255), user_id UUID NOT NULL, PRIMARY KEY (id));
DROP TABLE authority;
CREATE TABLE authority (id UUID NOT NULL, authority CHARACTER VARYING(255), name CHARACTER VARYING(255), PRIMARY KEY (id));
DROP TABLE category;
CREATE TABLE category (id UUID NOT NULL, updated TIMESTAMP(6) WITHOUT TIME ZONE, based_on_object UUID, based_on_revision INTEGER, change_comment CHARACTER VARYING(255) NOT NULL, change_kind CHARACTER VARYING(255) NOT NULL, name CHARACTER VARYING(255), major INTEGER, minor INTEGER, version_label CHARACTER VARYING(255), xml_lang CHARACTER VARYING(255), category_kind CHARACTER VARYING(255) NOT NULL, classification_level CHARACTER VARYING(255), description CHARACTER VARYING(2000), format CHARACTER VARYING(255), hierarchy_level CHARACTER VARYING(255) NOT NULL, maximum CHARACTER VARYING(255), minimum CHARACTER VARYING(255), label CHARACTER VARYING(255), user_id UUID NOT NULL, agency_id UUID NOT NULL, PRIMARY KEY (id), CONSTRAINT unq_category_name_kind UNIQUE (label, name, category_kind));
DROP TABLE category_children;
CREATE TABLE category_children (category_id UUID NOT NULL, children_id UUID NOT NULL, category_idx INTEGER NOT NULL, PRIMARY KEY (category_id, category_idx));
DROP TABLE code;
CREATE TABLE code (responsedomain_id UUID NOT NULL, alignment CHARACTER VARYING(255), code_value CHARACTER VARYING(255), responsedomain_idx INTEGER NOT NULL, PRIMARY KEY (responsedomain_id, responsedomain_idx));
DROP TABLE comment;
CREATE TABLE comment (id UUID NOT NULL, updated TIMESTAMP(6) WITHOUT TIME ZONE, comment CHARACTER VARYING(2000), is_public BOOLEAN DEFAULT true NOT NULL, owner_id UUID NOT NULL, user_id UUID NOT NULL, owner_idx INTEGER, PRIMARY KEY (id));
DROP TABLE concept;
CREATE TABLE concept (id UUID NOT NULL, updated TIMESTAMP(6) WITHOUT TIME ZONE, based_on_object UUID, based_on_revision INTEGER, change_comment CHARACTER VARYING(255) NOT NULL, change_kind CHARACTER VARYING(255) NOT NULL, name CHARACTER VARYING(255), major INTEGER, minor INTEGER, version_label CHARACTER VARYING(255), xml_lang CHARACTER VARYING(255), description CHARACTER VARYING(10000), is_archived BOOLEAN NOT NULL, label CHARACTER VARYING(255), topicgroup_id UUID, user_id UUID NOT NULL, agency_id UUID NOT NULL, concept_id UUID, PRIMARY KEY (id));
DROP TABLE concept_question_item;
CREATE TABLE concept_question_item (concept_id UUID NOT NULL, element_id UUID, element_kind CHARACTER VARYING(255), element_revision INTEGER, major INTEGER, minor INTEGER, name CHARACTER VARYING(255), version_label CHARACTER VARYING(255), concept_idx INTEGER NOT NULL, PRIMARY KEY (concept_id, concept_idx));
DROP TABLE control_construct;
CREATE TABLE control_construct (control_construct_kind CHARACTER VARYING(31) NOT NULL, id UUID NOT NULL, updated TIMESTAMP(6) WITHOUT TIME ZONE, based_on_object UUID, based_on_revision INTEGER, change_comment CHARACTER VARYING(255) NOT NULL, change_kind CHARACTER VARYING(255) NOT NULL, name CHARACTER VARYING(255), major INTEGER, minor INTEGER, version_label CHARACTER VARYING(255), xml_lang CHARACTER VARYING(255), label CHARACTER VARYING(255), description CHARACTER VARYING(255), control_construct_super_kind CHARACTER VARYING(255), questionitem_revision INTEGER, questionitem_id UUID, question_name CHARACTER VARYING(25), question_text CHARACTER VARYING(500), user_id UUID NOT NULL, agency_id UUID NOT NULL, PRIMARY KEY (id));
DROP TABLE control_construct_instruction;
CREATE TABLE control_construct_instruction (control_construct_id UUID NOT NULL, instruction_id UUID, instruction_rank CHARACTER VARYING(255), instruction_idx INTEGER NOT NULL, PRIMARY KEY (control_construct_id, instruction_idx));
DROP TABLE control_construct_other_material;
CREATE TABLE control_construct_other_material (owner_id UUID NOT NULL, description CHARACTER VARYING(255), file_name CHARACTER VARYING(255), file_type CHARACTER VARYING(255), original_name CHARACTER VARYING(255) NOT NULL, original_owner UUID, size BIGINT NOT NULL, owner_idx INTEGER NOT NULL, PRIMARY KEY (owner_id, owner_idx));
DROP TABLE control_construct_sequence;
CREATE TABLE control_construct_sequence (sequence_id UUID NOT NULL, element_id UUID, element_kind CHARACTER VARYING(255), element_revision INTEGER, major INTEGER, minor INTEGER, name CHARACTER VARYING(255), version_label CHARACTER VARYING(255), sequence_idx INTEGER NOT NULL, PRIMARY KEY (sequence_id, sequence_idx));
DROP TABLE control_construct_universe;
CREATE TABLE control_construct_universe (question_construct_id UUID NOT NULL, universe_id UUID NOT NULL, universe_idx INTEGER NOT NULL, PRIMARY KEY (question_construct_id, universe_idx));
DROP TABLE instruction;
CREATE TABLE instruction (id UUID NOT NULL, updated TIMESTAMP(6) WITHOUT TIME ZONE, based_on_object UUID, based_on_revision INTEGER, change_comment CHARACTER VARYING(255) NOT NULL, change_kind CHARACTER VARYING(255) NOT NULL, name CHARACTER VARYING(255), major INTEGER, minor INTEGER, version_label CHARACTER VARYING(255), xml_lang CHARACTER VARYING(255), description CHARACTER VARYING(255), user_id UUID NOT NULL, agency_id UUID NOT NULL, PRIMARY KEY (id), CONSTRAINT unq_instruction_name UNIQUE (name, description, agency_id));
DROP TABLE instrument;
CREATE TABLE instrument (id UUID NOT NULL, updated TIMESTAMP(6) WITHOUT TIME ZONE, based_on_object UUID, based_on_revision INTEGER, change_comment CHARACTER VARYING(255) NOT NULL, change_kind CHARACTER VARYING(255) NOT NULL, name CHARACTER VARYING(255), major INTEGER, minor INTEGER, version_label CHARACTER VARYING(255), xml_lang CHARACTER VARYING(255), description CHARACTER VARYING(255), external_instrument_location CHARACTER VARYING(255), instrument_kind CHARACTER VARYING(255), label CHARACTER VARYING(255), user_id UUID NOT NULL, agency_id UUID NOT NULL, study_id UUID, PRIMARY KEY (id));
DROP TABLE instrument_element;
CREATE TABLE instrument_element (id UUID NOT NULL, element_id UUID, element_kind CHARACTER VARYING(255), element_revision INTEGER, major INTEGER, minor INTEGER, name CHARACTER VARYING(255), version_label CHARACTER VARYING(255), _idx INTEGER, instrument_element_id UUID, instrument_id UUID, PRIMARY KEY (id));
DROP TABLE instrument_element_parameter;
CREATE TABLE instrument_element_parameter (instrument_element_id UUID NOT NULL, name CHARACTER VARYING(255), referenced_id UUID);
DROP TABLE publication;
CREATE TABLE publication (id UUID NOT NULL, updated TIMESTAMP(6) WITHOUT TIME ZONE, based_on_object UUID, based_on_revision INTEGER, change_comment CHARACTER VARYING(255) NOT NULL, change_kind CHARACTER VARYING(255) NOT NULL, name CHARACTER VARYING(255), major INTEGER, minor INTEGER, version_label CHARACTER VARYING(255), xml_lang CHARACTER VARYING(255), purpose CHARACTER VARYING(255), user_id UUID NOT NULL, agency_id UUID NOT NULL, status_id BIGINT NOT NULL, PRIMARY KEY (id));
DROP TABLE publication_element;
CREATE TABLE publication_element (publication_id UUID NOT NULL, element_id UUID, element_kind CHARACTER VARYING(255), element_revision INTEGER, major INTEGER, minor INTEGER, name CHARACTER VARYING(255), version_label CHARACTER VARYING(255), publication_idx INTEGER NOT NULL, PRIMARY KEY (publication_id, publication_idx));
DROP TABLE publication_status;
CREATE TABLE publication_status (id BIGINT NOT NULL, description CHARACTER VARYING(255), label CHARACTER VARYING(255), publication_status_idx INTEGER, published CHARACTER VARYING(255), agency_id UUID, publication_status_id BIGINT, PRIMARY KEY (id), CONSTRAINT unq_publication_status UNIQUE (agency_id, label));
DROP TABLE question_item;
CREATE TABLE question_item (id UUID NOT NULL, updated TIMESTAMP(6) WITHOUT TIME ZONE, based_on_object UUID, based_on_revision INTEGER, change_comment CHARACTER VARYING(255) NOT NULL, change_kind CHARACTER VARYING(255) NOT NULL, name CHARACTER VARYING(255), major INTEGER, minor INTEGER, version_label CHARACTER VARYING(255), xml_lang CHARACTER VARYING(255), intent CHARACTER VARYING(3000), question CHARACTER VARYING(2000), responsedomain_name CHARACTER VARYING(255), responsedomain_revision INTEGER, responsedomain_id UUID, user_id UUID NOT NULL, agency_id UUID NOT NULL, PRIMARY KEY (id));
DROP TABLE responsedomain;
CREATE TABLE responsedomain (id UUID NOT NULL, updated TIMESTAMP(6) WITHOUT TIME ZONE, based_on_object UUID, based_on_revision INTEGER, change_comment CHARACTER VARYING(255) NOT NULL, change_kind CHARACTER VARYING(255) NOT NULL, name CHARACTER VARYING(255), major INTEGER, minor INTEGER, version_label CHARACTER VARYING(255), xml_lang CHARACTER VARYING(255), description CHARACTER VARYING(2000) NOT NULL, display_layout CHARACTER VARYING(255), maximum CHARACTER VARYING(255), minimum CHARACTER VARYING(255), response_kind CHARACTER VARYING(255), schema_id UUID, user_id UUID NOT NULL, agency_id UUID NOT NULL, category_id UUID, PRIMARY KEY (id), CONSTRAINT unq_responsedomain_name UNIQUE (name, category_id, based_on_object), CONSTRAINT uk_9e2u4i28e05b8bxk0wupa6d4t UNIQUE (schema_id));
DROP TABLE revinfo;
CREATE TABLE revinfo (id INTEGER NOT NULL, timestamp BIGINT NOT NULL, modified TIMESTAMP(6) WITHOUT TIME ZONE, user_id UUID NOT NULL, PRIMARY KEY (id));
DROP TABLE study;
CREATE TABLE study (id UUID NOT NULL, updated TIMESTAMP(6) WITHOUT TIME ZONE, based_on_object UUID, based_on_revision INTEGER, change_comment CHARACTER VARYING(255) NOT NULL, change_kind CHARACTER VARYING(255) NOT NULL, name CHARACTER VARYING(255), major INTEGER, minor INTEGER, version_label CHARACTER VARYING(255), xml_lang CHARACTER VARYING(255), description CHARACTER VARYING(10000), is_archived BOOLEAN, user_id UUID NOT NULL, agency_id UUID NOT NULL, survey_id UUID, PRIMARY KEY (id));
DROP TABLE study_authors;
CREATE TABLE study_authors (study_id UUID NOT NULL, author_id UUID NOT NULL, PRIMARY KEY (study_id, author_id));
DROP TABLE survey_program;
CREATE TABLE survey_program (id UUID NOT NULL, updated TIMESTAMP(6) WITHOUT TIME ZONE, based_on_object UUID, based_on_revision INTEGER, change_comment CHARACTER VARYING(255) NOT NULL, change_kind CHARACTER VARYING(255) NOT NULL, name CHARACTER VARYING(255), major INTEGER, minor INTEGER, version_label CHARACTER VARYING(255), xml_lang CHARACTER VARYING(255), description CHARACTER VARYING(10000), is_archived BOOLEAN NOT NULL, user_id UUID NOT NULL, agency_id UUID NOT NULL, PRIMARY KEY (id));
DROP TABLE survey_program_authors;
CREATE TABLE survey_program_authors (survey_id UUID NOT NULL, author_id UUID NOT NULL, PRIMARY KEY (survey_id, author_id));
DROP TABLE topic_group;
CREATE TABLE topic_group (id UUID NOT NULL, updated TIMESTAMP(6) WITHOUT TIME ZONE, based_on_object UUID, based_on_revision INTEGER, change_comment CHARACTER VARYING(255) NOT NULL, change_kind CHARACTER VARYING(255) NOT NULL, name CHARACTER VARYING(255), major INTEGER, minor INTEGER, version_label CHARACTER VARYING(255), xml_lang CHARACTER VARYING(255), description CHARACTER VARYING(10000), is_archived BOOLEAN NOT NULL, study_id UUID, user_id UUID NOT NULL, agency_id UUID NOT NULL, PRIMARY KEY (id));
DROP TABLE topic_group_authors;
CREATE TABLE topic_group_authors (topicgroup_id UUID NOT NULL, author_id UUID NOT NULL, PRIMARY KEY (topicgroup_id, author_id));
DROP TABLE topic_group_other_material;
CREATE TABLE topic_group_other_material (owner_id UUID NOT NULL, description CHARACTER VARYING(255), file_name CHARACTER VARYING(255), file_type CHARACTER VARYING(255), original_name CHARACTER VARYING(255) NOT NULL, original_owner UUID, size BIGINT NOT NULL, owner_idx INTEGER NOT NULL, PRIMARY KEY (owner_id, owner_idx));
DROP TABLE topic_group_question_item;
CREATE TABLE topic_group_question_item (topicgroup_id UUID NOT NULL, element_id UUID, element_kind CHARACTER VARYING(255), element_revision INTEGER, major INTEGER, minor INTEGER, name CHARACTER VARYING(255), version_label CHARACTER VARYING(255), topicgroup_idx INTEGER NOT NULL, PRIMARY KEY (topicgroup_id, topicgroup_idx));
DROP TABLE universe;
CREATE TABLE universe (id UUID NOT NULL, updated TIMESTAMP(6) WITHOUT TIME ZONE, based_on_object UUID, based_on_revision INTEGER, change_comment CHARACTER VARYING(255) NOT NULL, change_kind CHARACTER VARYING(255) NOT NULL, name CHARACTER VARYING(255), major INTEGER, minor INTEGER, version_label CHARACTER VARYING(255), xml_lang CHARACTER VARYING(255), description CHARACTER VARYING(2000) NOT NULL, user_id UUID NOT NULL, agency_id UUID NOT NULL, PRIMARY KEY (id), CONSTRAINT unq_universe_name UNIQUE (name, description, agency_id));
DROP TABLE user_account;
CREATE TABLE user_account (id UUID NOT NULL, email CHARACTER VARYING(255), is_enabled BOOLEAN, updated TIMESTAMP(6) WITHOUT TIME ZONE, username CHARACTER VARYING(255), password CHARACTER VARYING(255), agency_id UUID, PRIMARY KEY (id), CONSTRAINT unq_user_email UNIQUE (email));
DROP TABLE user_authority;
CREATE TABLE user_authority (user_id UUID NOT NULL, authority_id UUID NOT NULL, PRIMARY KEY (user_id, authority_id));
ALTER TABLE "author" ADD CONSTRAINT fk72nss70nw66j9as1wx036rqnm FOREIGN KEY ("user_id") REFERENCES "user_account" ("id");
ALTER TABLE "category" ADD CONSTRAINT fkbnlig6nw8akrv8ahcejixu6kp FOREIGN KEY ("user_id") REFERENCES "user_account" ("id");
ALTER TABLE "category" ADD CONSTRAINT fk18skcl5goh3iuc1tf3jkc65s FOREIGN KEY ("agency_id") REFERENCES "agency" ("id");
ALTER TABLE "category_children" ADD CONSTRAINT fk659qdt2r98579x3b414qvc211 FOREIGN KEY ("children_id") REFERENCES "category" ("id");
ALTER TABLE "category_children" ADD CONSTRAINT fk6n2k92j6sc537ex4s3voewjn4 FOREIGN KEY ("category_id") REFERENCES "category" ("id");
ALTER TABLE "code" ADD CONSTRAINT fk3qoefqip0l2d641wsw02n4m42 FOREIGN KEY ("responsedomain_id") REFERENCES "responsedomain" ("id");
ALTER TABLE "comment" ADD CONSTRAINT fk3y3uou7na66pfn512byon549s FOREIGN KEY ("user_id") REFERENCES "user_account" ("id");
ALTER TABLE "comment" ADD CONSTRAINT fkqy57xx05jqnmfho4al644sq0l FOREIGN KEY ("owner_id") REFERENCES "instruction" ("id");
ALTER TABLE "concept" ADD CONSTRAINT fkf057337a833aq1fypxk7716sn FOREIGN KEY ("user_id") REFERENCES "user_account" ("id");
ALTER TABLE "concept" ADD CONSTRAINT fkg5xfxtnaglg4tbif0ui5p2eoi FOREIGN KEY ("agency_id") REFERENCES "agency" ("id");
ALTER TABLE "concept" ADD CONSTRAINT fkayyoxj6mfc6j6m1mq5nt7cw65 FOREIGN KEY ("concept_id") REFERENCES "concept" ("id");
ALTER TABLE "concept" ADD CONSTRAINT fk9amaem7ciowm4sir9hclpq23u FOREIGN KEY ("topicgroup_id") REFERENCES "topic_group" ("id");
ALTER TABLE "concept_question_item" ADD CONSTRAINT fke8arsk6hglardlq3fbyawolxe FOREIGN KEY ("concept_id") REFERENCES "concept" ("id");
ALTER TABLE "control_construct" ADD CONSTRAINT fk823dk0xscu6a8j6o5yp94jrhl FOREIGN KEY ("user_id") REFERENCES "user_account" ("id");
ALTER TABLE "control_construct" ADD CONSTRAINT fkivipadm0g9x8vkafvbgp7il5b FOREIGN KEY ("agency_id") REFERENCES "agency" ("id");
ALTER TABLE "control_construct_instruction" ADD CONSTRAINT fkegws5y6wv32bq8km4sm3tvxo0 FOREIGN KEY ("instruction_id") REFERENCES "instruction" ("id");
ALTER TABLE "control_construct_instruction" ADD CONSTRAINT fkrt0kg76jwks2bkum9l6ejtl08 FOREIGN KEY ("control_construct_id") REFERENCES "control_construct" ("id");
ALTER TABLE "control_construct_other_material" ADD CONSTRAINT fkrm5bmk1m2hyguvi7f01xfwicv FOREIGN KEY ("owner_id") REFERENCES "control_construct" ("id");
ALTER TABLE "control_construct_sequence" ADD CONSTRAINT fk4ki52a2v9d8t8dkfl0osih16l FOREIGN KEY ("sequence_id") REFERENCES "control_construct" ("id");
ALTER TABLE "control_construct_universe" ADD CONSTRAINT fkhhflivw3pqfljmp6t6bst6sqm FOREIGN KEY ("universe_id") REFERENCES "universe" ("id");
ALTER TABLE "control_construct_universe" ADD CONSTRAINT fkhqaalg4qmeu4tsar2p2jycc1q FOREIGN KEY ("question_construct_id") REFERENCES "control_construct" ("id");
ALTER TABLE "instruction" ADD CONSTRAINT fkbyh6foaxjav1gaqiua3a1rxbf FOREIGN KEY ("user_id") REFERENCES "user_account" ("id");
ALTER TABLE "instruction" ADD CONSTRAINT fkh6paf8tqhpm4otsttrtue8kpu FOREIGN KEY ("agency_id") REFERENCES "agency" ("id");
ALTER TABLE "instrument" ADD CONSTRAINT fkcux2jjuie39aa0yddoac1dix6 FOREIGN KEY ("user_id") REFERENCES "user_account" ("id");
ALTER TABLE "instrument" ADD CONSTRAINT fkpmhq7movl94km0086wdtfw7qu FOREIGN KEY ("agency_id") REFERENCES "agency" ("id");
ALTER TABLE "instrument" ADD CONSTRAINT fkjsym76lfsctp6ey62wrcrft3u FOREIGN KEY ("study_id") REFERENCES "study" ("id");
ALTER TABLE "instrument_element" ADD CONSTRAINT fkgp0g22o1sm2v1ll2ys8yyckry FOREIGN KEY ("instrument_element_id") REFERENCES "instrument_element" ("id");
ALTER TABLE "instrument_element" ADD CONSTRAINT fkl440ov3yge4cea17u7rau5wa7 FOREIGN KEY ("instrument_id") REFERENCES "instrument" ("id");
ALTER TABLE "instrument_element_parameter" ADD CONSTRAINT fkm7r6hhgx2ah8hvfg4wfk1xvsy FOREIGN KEY ("instrument_element_id") REFERENCES "instrument_element" ("id");
ALTER TABLE "publication" ADD CONSTRAINT fkeaeoql2u1x4t096c99ovgs2fx FOREIGN KEY ("user_id") REFERENCES "user_account" ("id");
ALTER TABLE "publication" ADD CONSTRAINT fk6lkjk95yiwaanm03yliuw1hf9 FOREIGN KEY ("agency_id") REFERENCES "agency" ("id");
ALTER TABLE "publication" ADD CONSTRAINT fkjhjlnd8e7wlduekbmhps1x71n FOREIGN KEY ("status_id") REFERENCES "publication_status" ("id");
ALTER TABLE "publication_element" ADD CONSTRAINT fklaum7f34ut10hvb41asxpaeql FOREIGN KEY ("publication_id") REFERENCES "publication" ("id");
ALTER TABLE "publication_status" ADD CONSTRAINT fk7xgyfv2nr69tv9860gw5ihmfh FOREIGN KEY ("agency_id") REFERENCES "agency" ("id");
ALTER TABLE "publication_status" ADD CONSTRAINT fkga3xbi284j3pagu855c7n2xl8 FOREIGN KEY ("publication_status_id") REFERENCES "publication_status" ("id");
ALTER TABLE "question_item" ADD CONSTRAINT fkpt2rd9pyc03ylujdb9uskta46 FOREIGN KEY ("user_id") REFERENCES "user_account" ("id");
ALTER TABLE "question_item" ADD CONSTRAINT fkqlxw0sdme1sym0v5tqf3a48hv FOREIGN KEY ("agency_id") REFERENCES "agency" ("id");
ALTER TABLE "responsedomain" ADD CONSTRAINT fk2c3ljdyvxovpj3y31e9129i0u FOREIGN KEY ("user_id") REFERENCES "user_account" ("id");
ALTER TABLE "responsedomain" ADD CONSTRAINT fkru9bfi8hwxa695r1v4rocamfp FOREIGN KEY ("agency_id") REFERENCES "agency" ("id");
ALTER TABLE "responsedomain" ADD CONSTRAINT fkd8ao4qu07wf2toj55ejkqel4c FOREIGN KEY ("category_id") REFERENCES "category" ("id");
ALTER TABLE "revinfo" ADD CONSTRAINT fk9q8k0vbdm1m4gneoxkjt9oqyq FOREIGN KEY ("user_id") REFERENCES "user_account" ("id");
ALTER TABLE "study" ADD CONSTRAINT fkfe9ceqica2qaks8r48xv6ufh5 FOREIGN KEY ("user_id") REFERENCES "user_account" ("id");
ALTER TABLE "study" ADD CONSTRAINT fk4vd3vxil6qdp9jqfye52mvlxu FOREIGN KEY ("agency_id") REFERENCES "agency" ("id");
ALTER TABLE "study" ADD CONSTRAINT fkc6s73c4ewthcwtlwd5ker06bt FOREIGN KEY ("survey_id") REFERENCES "survey_program" ("id");
ALTER TABLE "study_authors" ADD CONSTRAINT fkhrkwml3qy9umfo50c85vcxp08 FOREIGN KEY ("author_id") REFERENCES "author" ("id");
ALTER TABLE "study_authors" ADD CONSTRAINT fki8nijwwu2jlba4bh4jh55wjd5 FOREIGN KEY ("study_id") REFERENCES "study" ("id");
ALTER TABLE "survey_program" ADD CONSTRAINT fkdrsjm3c773rcm4b6esuas4v7n FOREIGN KEY ("user_id") REFERENCES "user_account" ("id");
ALTER TABLE "survey_program" ADD CONSTRAINT fkarg803hlwl5e5bixw7n5b94kd FOREIGN KEY ("agency_id") REFERENCES "agency" ("id");
ALTER TABLE "survey_program_authors" ADD CONSTRAINT fk8t025stsfksqwtbivi32pr3nh FOREIGN KEY ("author_id") REFERENCES "author" ("id");
ALTER TABLE "survey_program_authors" ADD CONSTRAINT fkss4vl4vr2d9a5gqvpkhf1y9cw FOREIGN KEY ("survey_id") REFERENCES "survey_program" ("id");
ALTER TABLE "topic_group" ADD CONSTRAINT fkiadqxywk6cvnkeppe29x4o2nh FOREIGN KEY ("user_id") REFERENCES "user_account" ("id");
ALTER TABLE "topic_group" ADD CONSTRAINT fkm5qnrgq1cknw7fgpqwiy4hme9 FOREIGN KEY ("agency_id") REFERENCES "agency" ("id");
ALTER TABLE "topic_group" ADD CONSTRAINT fkih7hytqtd5tlhsy4lrr24oqo2 FOREIGN KEY ("study_id") REFERENCES "study" ("id");
ALTER TABLE "topic_group_authors" ADD CONSTRAINT fk3owdueqn0au3vrvpwdcixw1er FOREIGN KEY ("author_id") REFERENCES "author" ("id");
ALTER TABLE "topic_group_authors" ADD CONSTRAINT fk9xlc2ormxvx2ngrer2r8y0ssg FOREIGN KEY ("topicgroup_id") REFERENCES "topic_group" ("id");
ALTER TABLE "topic_group_other_material" ADD CONSTRAINT fk2dfsslroif3labivf0g4o7509 FOREIGN KEY ("owner_id") REFERENCES "topic_group" ("id");
ALTER TABLE "topic_group_question_item" ADD CONSTRAINT fkg5kw7lp2kjy0ovg5tnfdkt6kk FOREIGN KEY ("topicgroup_id") REFERENCES "topic_group" ("id");
ALTER TABLE "universe" ADD CONSTRAINT fkcgtm3ov5pfuqphjiioed8unkn FOREIGN KEY ("user_id") REFERENCES "user_account" ("id");
ALTER TABLE "universe" ADD CONSTRAINT fk8lrj9jnijd2j81n96e7q9ebi6 FOREIGN KEY ("agency_id") REFERENCES "agency" ("id");
ALTER TABLE "user_account" ADD CONSTRAINT fkerd4b3vc09lde4wiuxk95xhnn FOREIGN KEY ("agency_id") REFERENCES "agency" ("id");
ALTER TABLE "user_authority" ADD CONSTRAINT fkgvxjs381k6f48d5d2yi11uh89 FOREIGN KEY ("authority_id") REFERENCES "authority" ("id");
ALTER TABLE "user_authority" ADD CONSTRAINT fkn48a3n0mb8d8njshjf75kgsv FOREIGN KEY ("user_id") REFERENCES "user_account" ("id");
DROP VIEW change_log;
CREATE VIEW change_log (ref_id, ref_rev, ref_kind, ref_change_kind, ref_modified, ref_modified_by, ref_action, element_id, element_revision, element_kind, name) AS  SELECT ca.id AS ref_id,
    ca.rev AS ref_rev,
    'CONCEPT'::text AS ref_kind,
    ca.change_kind AS ref_change_kind,
    COALESCE(ca.updated, ref.modified) AS ref_modified,
    COALESCE(ca.user_id, ref.user_id) AS ref_modified_by,
    ca.revtype AS ref_action,
    NULL::uuid AS element_id,
    NULL::integer AS element_revision,
    NULL::character varying AS element_kind,
    COALESCE(ca.name, 'Deleted'::character varying) AS name
   FROM (audit.concept_aud ca
     LEFT JOIN revinfo ref ON ((ref.id = ca.rev)))
UNION
 SELECT cc.id AS ref_id,
    cc.rev AS ref_rev,
    'CONTROL_CONSTRUCT'::text AS ref_kind,
    cc.change_kind AS ref_change_kind,
    COALESCE(cc.updated, ref.modified) AS ref_modified,
    COALESCE(cc.user_id, ref.user_id) AS ref_modified_by,
    cc.revtype AS ref_action,
    NULL::uuid AS element_id,
    NULL::integer AS element_revision,
    NULL::character varying AS element_kind,
    COALESCE(cc.name, 'Deleted'::character varying) AS name
   FROM (audit.control_construct_aud cc
     LEFT JOIN revinfo ref ON ((ref.id = cc.rev)))
UNION
 SELECT ai.id AS ref_id,
    ai.rev AS ref_rev,
    'INSTRUMENT'::text AS ref_kind,
    ai.change_kind AS ref_change_kind,
    COALESCE(ai.updated, ref.modified) AS ref_modified,
    COALESCE(ai.user_id, ref.user_id) AS ref_modified_by,
    ai.revtype AS ref_action,
    NULL::uuid AS element_id,
    NULL::integer AS element_revision,
    NULL::character varying AS element_kind,
    COALESCE(ai.name, 'Deleted'::character varying) AS name
   FROM (audit.instrument_aud ai
     LEFT JOIN revinfo ref ON ((ref.id = ai.rev)))
UNION
 SELECT qi.id AS ref_id,
    qi.rev AS ref_rev,
    'QUESTION_ITEM'::text AS ref_kind,
    qi.change_kind AS ref_change_kind,
    ref.modified AS ref_modified,
    ref.user_id AS ref_modified_by,
    qi.revtype AS ref_action,
    NULL::uuid AS element_id,
    NULL::integer AS element_revision,
    NULL::character varying AS element_kind,
    COALESCE(qi.name, 'Deleted'::character varying) AS name
   FROM (audit.question_item_aud qi
     LEFT JOIN revinfo ref ON ((ref.id = qi.rev)))
UNION
 SELECT rd.id AS ref_id,
    rd.rev AS ref_rev,
    'RESPONSEDOMAIN'::text AS ref_kind,
    rd.change_kind AS ref_change_kind,
    COALESCE(rd.updated, ref.modified) AS ref_modified,
    COALESCE(rd.user_id, ref.user_id) AS ref_modified_by,
    rd.revtype AS ref_action,
    NULL::uuid AS element_id,
    NULL::integer AS element_revision,
    NULL::character varying AS element_kind,
    COALESCE(rd.name, 'Deleted'::character varying) AS name
   FROM (audit.responsedomain_aud rd
     LEFT JOIN revinfo ref ON ((ref.id = rd.rev)))
UNION
 SELECT sa.id AS ref_id,
    sa.rev AS ref_rev,
    'STUDY'::text AS ref_kind,
    sa.change_kind AS ref_change_kind,
    COALESCE(sa.updated, ref.modified) AS ref_modified,
    COALESCE(sa.user_id, ref.user_id) AS ref_modified_by,
    sa.revtype AS ref_action,
    NULL::uuid AS element_id,
    NULL::integer AS element_revision,
    NULL::character varying AS element_kind,
    COALESCE(sa.name, 'Deleted'::character varying) AS name
   FROM (audit.study_aud sa
     LEFT JOIN revinfo ref ON ((ref.id = sa.rev)))
UNION
 SELECT asp.id AS ref_id,
    asp.rev AS ref_rev,
    'SURVEY_PROGRAM'::text AS ref_kind,
    asp.change_kind AS ref_change_kind,
    COALESCE(asp.updated, ref.modified) AS ref_modified,
    COALESCE(asp.user_id, ref.user_id) AS ref_modified_by,
    asp.revtype AS ref_action,
    NULL::uuid AS element_id,
    NULL::integer AS element_revision,
    NULL::character varying AS element_kind,
    COALESCE(asp.name, 'Deleted'::character varying) AS name
   FROM (audit.survey_program_aud asp
     LEFT JOIN revinfo ref ON ((ref.id = asp.rev)))
UNION
 SELECT atg.id AS ref_id,
    atg.rev AS ref_rev,
    'TOPIC_GROUP'::text AS ref_kind,
    atg.change_kind AS ref_change_kind,
    COALESCE(atg.updated, ref.modified) AS ref_modified,
    COALESCE(atg.user_id, ref.user_id) AS ref_modified_by,
    atg.revtype AS ref_action,
    NULL::uuid AS element_id,
    NULL::integer AS element_revision,
    NULL::character varying AS element_kind,
    COALESCE(atg.name, 'Deleted'::character varying) AS name
   FROM (audit.topic_group_aud atg
     LEFT JOIN revinfo ref ON ((ref.id = atg.rev)))
UNION
 SELECT ap.id AS ref_id,
    ap.rev AS ref_rev,
    'PUBLICATION'::text AS ref_kind,
    ap.change_kind AS ref_change_kind,
    COALESCE(ap.updated, ref.modified) AS ref_modified,
    COALESCE(ap.user_id, ref.user_id) AS ref_modified_by,
    ap.revtype AS ref_action,
    NULL::uuid AS element_id,
    NULL::integer AS element_revision,
    NULL::character varying AS element_kind,
    COALESCE(ap.name, 'Deleted'::character varying) AS name
   FROM (audit.publication_aud ap
     LEFT JOIN revinfo ref ON ((ref.id = ap.rev)))
UNION
 SELECT COALESCE(cqi.element_id, cqi.concept_id) AS ref_id,
    cqi.rev AS ref_rev,
    COALESCE(cqi.element_kind, ('CONCEPT'::text)::character varying) AS ref_kind,
    'UPDATED_HIERARCHY_RELATION'::text AS ref_change_kind,
    ref.modified AS ref_modified,
    ref.user_id AS ref_modified_by,
    cqi.revtype AS ref_action,
    cqi.element_id,
    cqi.element_revision,
    cqi.element_kind,
    COALESCE(cqi.name, 'Deleted'::character varying) AS name
   FROM (audit.concept_question_item_aud cqi
     LEFT JOIN revinfo ref ON ((ref.id = cqi.rev)))
UNION
 SELECT COALESCE(tgqi.element_id, tgqi.topicgroup_id) AS ref_id,
    tgqi.rev AS ref_rev,
    COALESCE(tgqi.element_kind, ('TOPIC_GROUP'::text)::character varying) AS ref_kind,
    'UPDATED_HIERARCHY_RELATION'::text AS ref_change_kind,
    ref.modified AS ref_modified,
    ref.user_id AS ref_modified_by,
    tgqi.revtype AS ref_action,
    tgqi.element_id,
    tgqi.element_revision,
    tgqi.element_kind,
    COALESCE(tgqi.name, 'Deleted'::character varying) AS name
   FROM (audit.topic_group_question_item_aud tgqi
     LEFT JOIN revinfo ref ON ((ref.id = tgqi.rev)))
UNION
 SELECT ccom.owner_id AS ref_id,
    ccom.rev AS ref_rev,
    'OTHER_MATERIAL'::text AS ref_kind,
    NULL::character varying AS ref_change_kind,
    ref.modified AS ref_modified,
    ref.user_id AS ref_modified_by,
    ccom.revtype AS ref_action,
    NULL::uuid AS element_id,
    NULL::integer AS element_revision,
    'OTHER_MATERIAL'::text AS element_kind,
    ccom.original_name AS name
   FROM (audit.control_construct_other_material_aud ccom
     LEFT JOIN revinfo ref ON ((ref.id = ccom.rev)))
UNION
 SELECT tgom.owner_id AS ref_id,
    tgom.rev AS ref_rev,
    'TOPIC_GROUP'::text AS ref_kind,
    NULL::character varying AS ref_change_kind,
    ref.modified AS ref_modified,
    ref.user_id AS ref_modified_by,
    tgom.revtype AS ref_action,
    NULL::uuid AS element_id,
    NULL::integer AS element_revision,
    'OTHER_MATERIAL'::text AS element_kind,
    tgom.original_name AS name
   FROM (audit.topic_group_other_material_aud tgom
     LEFT JOIN revinfo ref ON ((ref.id = tgom.rev)));
DROP VIEW project_archived;
CREATE VIEW project_archived (id, path, name, is_archived, parent_id) AS  SELECT concept.id,
    '/concept'::text AS path,
    concept.name,
    concept.is_archived,
    COALESCE(concept.topicgroup_id, concept.concept_id) AS parent_id
   FROM concept
UNION
 SELECT topic_group.id,
    '/module'::text AS path,
    topic_group.name,
    topic_group.is_archived,
    topic_group.study_id AS parent_id
   FROM topic_group
UNION
 SELECT study.id,
    '/study'::text AS path,
    study.name,
    study.is_archived,
    study.survey_id AS parent_id
   FROM study
UNION
 SELECT survey_program.id,
    '/survey'::text AS path,
    survey_program.name,
    survey_program.is_archived,
    NULL::uuid AS parent_id
   FROM survey_program;
DROP VIEW project_archived_hierarchy;
CREATE VIEW project_archived_hierarchy (id, path, name, is_archived, ancestors) AS  WITH RECURSIVE tree AS (
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
DROP VIEW uar;
CREATE VIEW uar (id, email, username, name, authority) AS  SELECT ua.id,
    ua.email,
    ua.username,
    a.name,
    a.authority
   FROM ((user_account ua
     LEFT JOIN user_authority au ON ((au.user_id = ua.id)))
     LEFT JOIN authority a ON ((au.authority_id = a.id)))
  WHERE ua.is_enabled;
DROP VIEW uuidpath;
CREATE VIEW uuidpath (id, path, name, user_id) AS  SELECT c.id,
    '/categories'::text AS path,
    c.name,
    c.user_id
   FROM category c
  WHERE ((c.category_kind)::text = 'CATEGORY'::text)
UNION
 SELECT c.id,
    '/missing'::text AS path,
    c.name,
    c.user_id
   FROM category c
  WHERE ((c.category_kind)::text = 'MISSING_GROUP'::text)
UNION
 SELECT ins.id,
    '/instructions'::text AS path,
    ins.name,
    ins.user_id
   FROM instruction ins
UNION
 SELECT cc.id,
    '/conditions'::text AS path,
    cc.name,
    cc.user_id
   FROM control_construct cc
  WHERE ((cc.control_construct_kind)::text = 'CONDITION_CONSTRUCT'::text)
UNION
 SELECT cc.id,
    '/statements'::text AS path,
    cc.name,
    cc.user_id
   FROM control_construct cc
  WHERE ((cc.control_construct_kind)::text = 'STATEMENT_CONSTRUCT'::text)
UNION
 SELECT cc.id,
    '/questions'::text AS path,
    cc.name,
    cc.user_id
   FROM control_construct cc
  WHERE ((cc.control_construct_kind)::text = 'QUESTION_CONSTRUCT'::text)
UNION
 SELECT cc.id,
    '/sequences'::text AS path,
    cc.name,
    cc.user_id
   FROM control_construct cc
  WHERE ((cc.control_construct_kind)::text = 'SEQUENCE_CONSTRUCT'::text)
UNION
 SELECT instrument.id,
    '/instruments'::text AS path,
    instrument.name,
    instrument.user_id
   FROM instrument
UNION
 SELECT publication.id,
    '/publications'::text AS path,
    publication.name,
    publication.user_id
   FROM publication
UNION
 SELECT question_item.id,
    '/questionitems'::text AS path,
    question_item.name,
    question_item.user_id
   FROM question_item
UNION
 SELECT responsedomain.id,
    '/responsedomains'::text AS path,
    responsedomain.name,
    responsedomain.user_id
   FROM responsedomain
UNION
 SELECT concept.id,
    '/concept'::text AS path,
    concept.name,
    concept.user_id
   FROM concept
UNION
 SELECT topic_group.id,
    '/module'::text AS path,
    topic_group.name,
    topic_group.user_id
   FROM topic_group
UNION
 SELECT study.id,
    '/study'::text AS path,
    study.name,
    study.user_id
   FROM study
UNION
 SELECT survey_program.id,
    '/survey'::text AS path,
    survey_program.name,
    survey_program.user_id
   FROM survey_program;
