package no.nsd.qddt.domain.question.audit;

import no.nsd.qddt.domain.question.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;

import java.util.UUID;


/**
 * @author Dag Østgulen Heradstveit
 */
@Service("questionAuditService")
class QuestionAuditServiceImpl implements QuestionAuditService {

    private QuestionAuditRepository questionAuditRepository;

    @Autowired
    public QuestionAuditServiceImpl(QuestionAuditRepository questionAuditRepository) {
        this.questionAuditRepository = questionAuditRepository;
    }

    @Override
    public Revision<Integer, Question> findLastChange(UUID uuid) {
        return questionAuditRepository.findLastChangeRevision(uuid);
    }

    @Override
    public Revision<Integer, Question> findRevision(UUID uuid, Integer revision) {
        return questionAuditRepository.findRevision(uuid, revision);
    }

    @Override
    public Page<Revision<Integer, Question>> findRevisions(UUID uuid, Pageable pageable) {
        return questionAuditRepository.findRevisions(uuid,pageable);
    }

    @Override
    public Revision<Integer, Question> findFirstChange(UUID uuid) {
        return questionAuditRepository.findRevisions(uuid).
                getContent().stream().
                min((i,o)->i.getRevisionNumber()).get();
    }

//    @Override
//    public Page<Revision<Integer, Question>> findRevisionByIdAndChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable) {
//        return questionAuditRepository.findRevisionsByIdAndChangeKindNotIn(id, changeKinds,pageable);
//    }

}
