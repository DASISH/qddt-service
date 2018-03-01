package no.nsd.qddt.domain.topicgroup;

import no.nsd.qddt.domain.ArchivableService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface TopicGroupService extends ArchivableService<TopicGroup> {

    List<TopicGroup> findByStudyId(UUID id);

    Page<TopicGroup> findAllPageable(Pageable pageable);

    Page<TopicGroup> findByNameAndDescriptionPageable(String name, String description, Pageable pageable);

    TopicGroup copy(UUID id, Long rev, UUID parentId);

}
