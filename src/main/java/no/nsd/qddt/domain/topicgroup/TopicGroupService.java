package no.nsd.qddt.domain.topicgroup;

import no.nsd.qddt.domain.classes.interfaces.BaseArchivedService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface TopicGroupService extends BaseArchivedService<TopicGroup> {

    List<TopicGroup> findByStudyId(UUID id);

    Page<TopicGroup> findAllPageable(Pageable pageable);

    Page<TopicGroup> findByNameAndDescriptionPageable(String name, String description, Pageable pageable);

    List<TopicGroup> findByQuestionItem(UUID id, Integer rev);

    TopicGroup copy(UUID id, Integer rev, UUID parentId);

}
