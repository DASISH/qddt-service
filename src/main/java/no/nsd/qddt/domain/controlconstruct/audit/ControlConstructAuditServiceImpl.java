package no.nsd.qddt.domain.controlconstruct.audit;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.controlconstruct.ControlConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Service("instrumentAuditQuestionService")
class ControlConstructAuditServiceImpl implements ControlConstructAuditService {

    private ControlConstructAuditRepository controlConstructAuditRepository;

    @Autowired
    public ControlConstructAuditServiceImpl(ControlConstructAuditRepository controlConstructAuditRepository) {
        this.controlConstructAuditRepository = controlConstructAuditRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, ControlConstruct> findLastChange(UUID id) {
        return controlConstructAuditRepository.findLastChangeRevision(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, ControlConstruct> findRevision(UUID id, Integer revision) {
        return controlConstructAuditRepository.findRevision(id, revision);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Revision<Integer, ControlConstruct>> findRevisions(UUID id, Pageable pageable) {
        return controlConstructAuditRepository.findRevisions(id,pageable);
    }

//    @Override
//    public Page<Revision<Integer, ControlConstruct>> findRevisionByIdAndChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable) {
//        return controlConstructAuditRepository.findRevisionsByIdAndChangeKindNotIn(id, changeKinds,pageable);
//    }


}
