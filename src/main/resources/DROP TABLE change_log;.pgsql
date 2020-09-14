DROP TABLE change_log;
DROP VIEW change_log;
CREATE VIEW change_log (ref_id, ref_rev, ref_kind, ref_change_kind, ref_modified, ref_modified_by, ref_action, element_id, element_revision, element_kind, name) AS  
SELECT ca.id AS ref_id,
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
    COALESCE(cqi.element_name, 'Deleted'::character varying) AS name
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
    COALESCE(tgqi.element_name, 'Deleted'::character varying) AS name
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


DROP TABLE project_archived;
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

DROP TABLE project_archived_hierarchy;
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

DROP TABLE referenced;
DROP VIEW referenced;
CREATE VIEW referenced (entity, kind, modified, antall, refs) AS  WITH total(parent, entity, modified, kind) AS (
    SELECT category_children.category_id,
           category_children.children_id,
           c.updated,
           'CATEGORY'::text AS text
    FROM (category_children
             LEFT JOIN category c ON ((c.id = category_children.children_id)))
    UNION
    SELECT COALESCE(concept.concept_id, concept.topicgroup_id) AS "coalesce",
           concept.id,
           concept.updated,
           'CONCEPT'::text AS text
    FROM concept
    UNION
    SELECT concept_question_item.concept_id,
           concept_question_item.element_id,
           qi.updated,
           'QUESTION_ITEM'::text AS text
    FROM (concept_question_item
             LEFT JOIN question_item qi ON ((qi.id = concept_question_item.element_id)))
    UNION
    SELECT cc.id,
           cc.questionitem_id,
           qi.updated,
           'QUESTION_ITEM'::text AS text
    FROM (control_construct cc
             LEFT JOIN question_item qi ON ((qi.id = cc.questionitem_id)))
    WHERE (cc.questionitem_id IS NOT NULL)
    UNION
    SELECT control_construct_instruction.control_construct_id,
           control_construct_instruction.instruction_id,
           ins.updated,
           'INSTRUCTION'::text AS text
    FROM (control_construct_instruction
             LEFT JOIN instruction ins ON ((ins.id = control_construct_instruction.instruction_id)))
    UNION
    SELECT control_construct_sequence.sequence_id,
           control_construct_sequence.element_id,
           cc.updated,
           'CONSTRUCT'::text AS text
    FROM (control_construct_sequence
             LEFT JOIN control_construct cc ON ((cc.id = control_construct_sequence.element_id)))
    UNION
    SELECT control_construct_universe.question_construct_id,
           control_construct_universe.universe_id,
           u.updated,
           'UNIVERSE'::text AS text
    FROM (control_construct_universe
             LEFT JOIN universe u ON ((u.id = control_construct_universe.universe_id)))
    UNION
    SELECT instrument.study_id,
           instrument.id,
           instrument.updated,
           'INSTRUMENT'::text AS text
    FROM instrument
    WHERE (instrument.study_id IS NOT NULL)
    UNION
    SELECT COALESCE(instrument_element.instrument_id, instrument_element.instrument_element_id) AS "coalesce",
           instrument_element.element_id,
           cc.updated,
           'CONSTRUCT'::text AS text
    FROM (instrument_element
             LEFT JOIN control_construct cc ON ((cc.id = instrument_element.element_id)))
    UNION
    SELECT r.id,
           r.category_id,
           c.updated,
           'CATEGORY'::text AS text
    FROM (responsedomain r
             LEFT JOIN category c ON ((c.id = r.category_id)))
    WHERE (r.category_id IS NOT NULL)
    UNION
    SELECT topic_group.study_id,
           topic_group.id,
           topic_group.updated,
           'TOPIC_GROUP'::text AS text
    FROM topic_group
    WHERE (topic_group.study_id IS NOT NULL)
    UNION
    SELECT topic_group_question_item.topicgroup_id,
           topic_group_question_item.element_id,
           qi.updated,
           'QUESTION_ITEM'::text AS text
    FROM (topic_group_question_item
             LEFT JOIN question_item qi ON ((qi.id = topic_group_question_item.element_id)))
)
                                                                  SELECT total.entity,
                                                                         total.kind,
                                                                         total.modified,
                                                                         count(total.parent) AS antall,
                                                                         array_agg(total.parent) AS refs
                                                                  FROM total
                                                                  GROUP BY total.entity, total.kind, total.modified;

DROP TABLE uar;
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

DROP TABLE uuidpath;
DROP VIEW uuidpath;

CREATE VIEW
    uuidpath
            (
             id,
             path,
             elementkind,
             name,
             user_id,
             parent_id
                ) AS
SELECT
    c.id,
    '/categories'::text AS path,
    'CATEGORY'::text    AS elementkind,
    c.name,
    c.user_id,
    cc.category_id
FROM
    category c
        left join category_children cc ON c.id = cc.children_id
WHERE
    ((c.category_kind)::text = 'CATEGORY'::text)
UNION
SELECT
    c.id,
    '/missing'::text AS path,
    'MISSING'::text  AS elementkind,
    c.name,
    c.user_id,
    cc.category_id
FROM
    category c
        left join category_children cc ON c.id = cc.children_id
WHERE
    ((c.category_kind)::text = 'MISSING_GROUP'::text)
UNION
SELECT
    ins.id,
    '/instructions'::text AS path,
    'INSTRUCTION'::text   AS elementkind,
    ins.name,
    ins.user_id,
    null
FROM
    instruction ins
UNION
SELECT
    cc.id,
    '/conditions'::text         AS path,
    'CONDITION_CONSTRUCT'::text AS elementkind,
    cc.name,
    cc.user_id,
    null
FROM
    control_construct cc
WHERE
    ((
         cc.control_construct_kind)::text = 'CONDITION_CONSTRUCT'::text)
UNION
SELECT
    cc.id,
    '/statements'::text         AS path,
    'STATEMENT_CONSTRUCT'::text AS elementkind,
    cc.name,
    cc.user_id,
    null
FROM
    control_construct cc
WHERE
    ((
         cc.control_construct_kind)::text = 'STATEMENT_CONSTRUCT'::text)
UNION
SELECT
    cc.id,
    '/questions'::text         AS path,
    'QUESTION_CONSTRUCT'::text AS elementkind,
    cc.name,
    cc.user_id,
    null
FROM
    control_construct cc
WHERE
    ((
         cc.control_construct_kind)::text = 'QUESTION_CONSTRUCT'::text)
UNION
SELECT
    cc.id,
    '/sequences'::text         AS path,
    'SEQUENCE_CONSTRUCT'::text AS elementkind,
    cc.name,
    cc.user_id,
    null
FROM
    control_construct cc
WHERE
    ((
         cc.control_construct_kind)::text = 'SEQUENCE_CONSTRUCT'::text)
UNION
SELECT
    instrument.id,
    '/instruments'::text AS path,
    'INSTRUMENT'::text   AS elementkind,
    instrument.name,
    instrument.user_id,
    instrument.study_id
FROM
    instrument
UNION
SELECT
    publication.id,
    '/publications'::text AS path,
    'PUBLICATION'::text   AS elementkind,
    publication.name,
    publication.user_id,
    null
FROM
    publication
UNION
SELECT
    question_item.id,
    '/questionitems'::text AS path,
    'QUESTION_ITEM'::text  AS elementkind,
    question_item.name,
    question_item.user_id,
    null
FROM
    question_item
UNION
SELECT
    responsedomain.id,
    '/responsedomains'::text AS path,
    'RESPONSEDOMAIN'::text   AS elementkind,
    responsedomain.name,
    responsedomain.user_id,
    null
FROM
    responsedomain
UNION
SELECT
    concept.id,
    '/concept'::text AS path,
    'CONCEPT'::text  AS elementkind,
    concept.name,
    concept.user_id,
    COALESCE(concept.topicgroup_id, concept.concept_id)
FROM
    concept
UNION
SELECT
    topic_group.id,
    '/module'::text     AS path,
    'TOPIC_GROUP'::text AS elementkind,
    topic_group.name,
    topic_group.user_id,
    topic_group.study_id
FROM
    topic_group
UNION
SELECT
    study.id,
    '/study'::text AS path,
    'STUDY'::text  AS elementkind,
    study.name,
    study.user_id,
    study.survey_id
FROM
    study
UNION
SELECT
    survey_program.id,
    '/survey'::text        AS path,
    'SURVEY_PROGRAM'::text AS elementkind,
    survey_program.name,
    survey_program.user_id,
    null
FROM
    survey_program;
