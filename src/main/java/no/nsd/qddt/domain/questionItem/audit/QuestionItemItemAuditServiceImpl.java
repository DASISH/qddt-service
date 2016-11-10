package no.nsd.qddt.domain.questionItem.audit;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;


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
    public Page<Revision<Integer, QuestionItem>> findRevisionByIdAndChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable) {
        int skip = pageable.getOffset();
        int limit = pageable.getPageSize();
        return new PageImpl<>(
                questionItemAuditRepository.findRevisions(id).getContent().stream()
                        .filter(f -> !changeKinds.contains(f.getEntity().getChangeKind()))
                        .skip(skip)
                        .limit(limit)
                        .collect(Collectors.toList())
        );
    }

}
