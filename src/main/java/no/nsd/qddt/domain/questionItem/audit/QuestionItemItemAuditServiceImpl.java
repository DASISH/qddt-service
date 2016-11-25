package no.nsd.qddt.domain.questionItem.audit;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.responsedomain.web.ResponseDomainAuditController;
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
    private ResponseDomainAuditController rdAuditController;

    @Autowired
    public QuestionItemItemAuditServiceImpl(QuestionItemAuditRepository questionItemAuditRepository,ResponseDomainAuditController rdAuditController) {
        this.questionItemAuditRepository = questionItemAuditRepository;
        this.rdAuditController = rdAuditController;
    }

    @Override
    public Revision<Integer, QuestionItem> findLastChange(UUID uuid) {
        Revision<Integer, QuestionItem> rev =questionItemAuditRepository.findLastChangeRevision(uuid);
        return populateResponseDomain(rev);
    }

    @Override
    public Revision<Integer, QuestionItem> findRevision(UUID uuid, Integer revision) {
        Revision<Integer, QuestionItem> rev = questionItemAuditRepository.findRevision(uuid, revision);
        return populateResponseDomain(rev);
    }

    @Override
    public Page<Revision<Integer, QuestionItem>> findRevisions(UUID uuid, Pageable pageable) {
        return new PageImpl<>(questionItemAuditRepository.findRevisions(uuid,pageable).getContent().stream()
                .map(rev -> populateResponseDomain(rev))
                .collect(Collectors.toList())
        );
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
                        .map(rev -> populateResponseDomain(rev))
                        .collect(Collectors.toList())
        );
    }

    private Revision<Integer, QuestionItem> populateResponseDomain(Revision<Integer, QuestionItem> rev){
        if (rev.getEntity().getResponseDomainUUID() != null) {
            rev.getEntity().setResponseDomain(
                rdAuditController.getByRevision(
                    rev.getEntity().getResponseDomainUUID(),
                    rev.getEntity().getResponseDomainRevision()).getEntity());
        }
        return rev;
    }


}
