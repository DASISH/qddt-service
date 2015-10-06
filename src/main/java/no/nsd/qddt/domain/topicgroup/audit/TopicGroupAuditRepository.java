package no.nsd.qddt.domain.topicgroup.audit;

import no.nsd.qddt.domain.topicgroup.TopicGroup;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface TopicGroupAuditRepository extends EnversRevisionRepository<TopicGroup, UUID, Integer> {

}
