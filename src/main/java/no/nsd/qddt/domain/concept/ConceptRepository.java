package no.nsd.qddt.domain.concept;

import no.nsd.qddt.domain.BaseArchivedRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Repository
interface ConceptRepository extends BaseArchivedRepository<Concept,UUID> {

    Page<Concept> findAll(Pageable pageable);

    Page<Concept> findByTopicGroupIdAndNameIsNotNull(UUID id, Pageable pageable);

    @Query(value = "SELECT c.* FROM concept c " +
        "WHERE (  c.change_kind !='BASED_ON' and (c.name ILIKE :name or c.description ILIKE :description) ) "
        + "ORDER BY ?#{#pageable}"
        ,countQuery = "SELECT count(c.*) FROM concept c " +
        "WHERE (  c.change_kind !='BASED_ON' and (c.name ILIKE :name or c.description ILIKE :description) ) "
        ,nativeQuery = true)
    Page<Concept> findByQuery(@Param("name")String name, @Param("description")String description, Pageable pageable);

    List<Concept> findByConceptQuestionItemsElementId(UUID id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value =
        "UPDATE concept SET _idx = crow.idx-1 FROM " +
            "(SELECT cc.id, row_number() over (partition by COALESCE (topicgroup_id, concept_id) order by _idx, updated ) as idx " +
            "FROM concept cc where COALESCE (topicgroup_id, concept_id) = CAST(:parentId AS uuid)) as crow " +
            "WHERE concept.id  = crow.id;"
        , nativeQuery = true)
    int indexChildren(@Param("parentId")String parentId);

}
