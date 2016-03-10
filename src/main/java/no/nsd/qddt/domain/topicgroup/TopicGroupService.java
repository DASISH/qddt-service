package no.nsd.qddt.domain.topicgroup;

import no.nsd.qddt.domain.BaseService;

import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface TopicGroupService extends BaseService<TopicGroup, UUID> {

    List<TopicGroup> findByStudyId(UUID id);

}
