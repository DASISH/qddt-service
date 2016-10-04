package no.nsd.qddt.domain.topicgroup.audit;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Service("topicGroupAuditService")
class TopicGroupAuditServiceImpl implements TopicGroupAuditService {

    private TopicGroupAuditRepository topicGroupAuditRepository;

    @Autowired
    public TopicGroupAuditServiceImpl(TopicGroupAuditRepository topicGroupAuditRepository) {
        this.topicGroupAuditRepository = topicGroupAuditRepository;
    }

    @Override
    public Revision<Integer, TopicGroup> findLastChange(UUID uuid) {
        return topicGroupAuditRepository.findLastChangeRevision(uuid);
    }

    @Override
    public Revision<Integer, TopicGroup> findRevision(UUID uuid, Integer revision) {
        return topicGroupAuditRepository.findRevision(uuid, revision);
    }

    @Override
    public Page<Revision<Integer, TopicGroup>> findRevisions(UUID uuid, Pageable pageable) {
        return topicGroupAuditRepository.findRevisions(uuid, pageable);
    }

//    @Override
//    public Page<Revision<Integer, TopicGroup>> findRevisionByIdAndChangeKindNotIn(UUID uuid, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable) {
//        return topicGroupAuditRepository.findRevisionsByIdAndChangeKindNotIn(uuid,changeKinds,pageable);
//    }
}
