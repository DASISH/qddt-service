package no.nsd.qddt.domain.topicgroup;

import no.nsd.qddt.domain.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Repository
interface TopicGroupRepository extends BaseRepository<TopicGroup,UUID> {

    List<TopicGroup> findByStudyId(UUID id);

    Page<TopicGroup> findAll(Pageable pageable);

    Page<TopicGroup> findByNameLikeIgnoreCaseOrAbstractDescriptionLikeIgnoreCase(String name, String description, Pageable pageable);

    List<TopicGroup> findByTopicQuestionItemsElementId(UUID id);
}
