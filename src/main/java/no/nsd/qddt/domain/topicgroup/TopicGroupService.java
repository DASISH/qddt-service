package no.nsd.qddt.domain.topicgroup;

import no.nsd.qddt.domain.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface TopicGroupService extends BaseService<TopicGroup, UUID> {

    List<TopicGroup> findByStudyId(UUID id);

    Page<TopicGroup> findAllPageable(Pageable pageable);

    Page<TopicGroup> findByNameAndDescriptionPageable(String name, String description, Pageable pageable);
}
