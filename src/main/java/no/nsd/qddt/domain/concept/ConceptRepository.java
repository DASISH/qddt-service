package no.nsd.qddt.domain.concept;

import no.nsd.qddt.domain.classes.interfaces.BaseArchivedRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        + "ORDER BY ?#{#pageable}"
        ,nativeQuery = true)
    Page<Concept> findByQuery(@Param("name")String name, @Param("description")String description,Pageable pageable);
    

    List<Concept> findByConceptQuestionItemsElementId(UUID id);


//    @Query(value = "SELECT c.* FROM concept c " +
//        "WHERE (  ) "
//        ,countQuery = "SELECT count(c.*) FROM concept c " +
//        "WHERE (  c.change_kind !='BASED_ON' and (c.name ILIKE :name or c.description ILIKE :description) ) "
//        ,nativeQuery = true)
//    List<ElementRefEmbedded>findListbyTopicGroup(UUID topicGroupId);
}
