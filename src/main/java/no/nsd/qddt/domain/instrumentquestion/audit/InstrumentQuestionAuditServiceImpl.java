package no.nsd.qddt.domain.instrumentquestion.audit;

import no.nsd.qddt.domain.instrumentquestion.InstrumentQuestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Service("instrumentAuditQuestionService")
class InstrumentQuestionAuditServiceImpl implements InstrumentQuestionAuditService {

    private InstrumentQuestionAuditRepository instrumentQuestionAuditRepository;

    @Autowired
    public InstrumentQuestionAuditServiceImpl(InstrumentQuestionAuditRepository instrumentQuestionAuditRepository) {
        this.instrumentQuestionAuditRepository = instrumentQuestionAuditRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, InstrumentQuestion> findLastChange(UUID id) {
        return instrumentQuestionAuditRepository.findLastChangeRevision(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, InstrumentQuestion> findRevision(UUID id, Integer revision) {
        return instrumentQuestionAuditRepository.findRevision(id, revision);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Revision<Integer, InstrumentQuestion>> findRevisions(UUID id, Pageable pageable) {
        return instrumentQuestionAuditRepository.findRevisions(id,pageable);
    }

}
