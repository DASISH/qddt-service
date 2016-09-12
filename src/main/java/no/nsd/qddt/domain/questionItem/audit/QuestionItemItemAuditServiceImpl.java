package no.nsd.qddt.domain.questionItem.audit;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.questionItem.QuestionItem;
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
@Service("questionItemAuditService")
class QuestionItemItemAuditServiceImpl implements QuestionItemAuditService {

    private QuestionItemAuditRepository questionItemAuditRepository;

    @Autowired
    public QuestionItemItemAuditServiceImpl(QuestionItemAuditRepository questionItemAuditRepository) {
        this.questionItemAuditRepository = questionItemAuditRepository;
    }

    @Override
    public Revision<Integer, QuestionItem> findLastChange(UUID uuid) {
        return questionItemAuditRepository.findLastChangeRevision(uuid);
    }

    @Override
    public Revision<Integer, QuestionItem> findRevision(UUID uuid, Integer revision) {
        return questionItemAuditRepository.findRevision(uuid, revision);
    }

    @Override
    public Page<Revision<Integer, QuestionItem>> findRevisions(UUID uuid, Pageable pageable) {
        return questionItemAuditRepository.findRevisions(uuid,pageable);
    }

    @Override
    public Page<Revision<Integer, QuestionItem>> findRevisionsByChangeKindNotIn(UUID uuid, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable) {
        return questionItemAuditRepository.findRevisionsByChangeKindNotIn(uuid, changeKinds,pageable);
    }
}
