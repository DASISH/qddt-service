package no.nsd.qddt.domain.instrument;

import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Service("instrumentQuestionService")
class InstrumentQuestionServiceImpl implements InstrumentQuestionService {

    private InstrumentQuestionRepository instrumentQuestionRepository;

    @Autowired
    public InstrumentQuestionServiceImpl(InstrumentQuestionRepository instrumentQuestionRepository) {
        this.instrumentQuestionRepository = instrumentQuestionRepository;
    }

    @Override
    public long count() {
        return instrumentQuestionRepository.count();
    }

    @Override
    public boolean exists(UUID uuid) {
        return instrumentQuestionRepository.exists(uuid);
    }

    @Override
    @Transactional(readOnly = true)
    public InstrumentQuestion findOne(UUID id) {
        return instrumentQuestionRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(id, InstrumentQuestion.class)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<InstrumentQuestion> findAll() {
        return instrumentQuestionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InstrumentQuestion> findAll(Pageable pageable) {

        return instrumentQuestionRepository.findAll(pageable);
    }

    @Override
    public List<InstrumentQuestion> findAll(Iterable<UUID> uuids) {
        return instrumentQuestionRepository.findAll(uuids);
    }

    @Override
    @Transactional(readOnly = false)
    public InstrumentQuestion save(InstrumentQuestion instance) {

        instance.setCreated(LocalDateTime.now());
        return instrumentQuestionRepository.save(instance);
    }

    @Override
    public void delete(UUID uuid) {

    }

    @Override
    @Transactional(readOnly = true)
    public List<InstrumentQuestion> findByInstrumentId(UUID instrumentId) {
        return instrumentQuestionRepository.findByInstrumentId(instrumentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InstrumentQuestion> findByQuestionId(UUID questionId) {
        return instrumentQuestionRepository.findByQuestionId(questionId);
    }


    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, InstrumentQuestion> findLastChange(UUID id) {
        return instrumentQuestionRepository.findLastChangeRevision(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, InstrumentQuestion> findEntityAtRevision(UUID id, Integer revision) {
        return instrumentQuestionRepository.findEntityAtRevision(id, revision);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Revision<Integer, InstrumentQuestion>> findAllRevisionsPageable(UUID id, Pageable pageable) {
        return instrumentQuestionRepository.findRevisions(id,pageable);
    }

}
