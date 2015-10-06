package no.nsd.qddt.domain.topicgroup;

import no.nsd.qddt.domain.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Stig Norland
 */
@Repository
interface TopicGroupRepository extends BaseRepository<TopicGroup,UUID> {

}
