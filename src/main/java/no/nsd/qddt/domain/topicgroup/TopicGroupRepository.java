package no.nsd.qddt.domain.topicgroup;

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
interface TopicGroupRepository extends BaseArchivedRepository<TopicGroup,UUID> {

    List<TopicGroup> findByStudyId(UUID id);

    Page<TopicGroup> findAll(Pageable pageable);

    @Query(value = "SELECT tg.* FROM topic_group tg " +
        "WHERE (  tg.change_kind !='BASED_ON' and (tg.name ILIKE :name or tg.description ILIKE :description) ) "
        + "ORDER BY ?#{#pageable}"
        ,countQuery = "SELECT count(tg.*) FROM topic_group tg " +
        "WHERE (  tg.change_kind !='BASED_ON' and (tg.name ILIKE :name or tg.description ILIKE :description) ) "
        + "ORDER BY ?#{#pageable}"
        ,nativeQuery = true)
    Page<TopicGroup> findByQuery(@Param("name")String name, @Param("description")String description, Pageable pageable);


    List<TopicGroup> findByTopicQuestionItemsElementId(UUID id);


}
