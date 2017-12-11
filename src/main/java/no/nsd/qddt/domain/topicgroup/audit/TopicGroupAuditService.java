package no.nsd.qddt.domain.topicgroup.audit;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.BaseServiceAudit;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;

import java.util.Collection;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface TopicGroupAuditService extends BaseServiceAudit<TopicGroup, UUID, Long> {

    Page<Revision<Long, TopicGroup>> findRevisionByIdAndChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable);
    Page<Revision<Long, TopicGroup>> findRevisionsByChangeKindIncludeLatest(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable);


}
