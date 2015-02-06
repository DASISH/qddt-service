package no.nsd.qddt.service;

import no.nsd.qddt.domain.instrument.InstrumentQuestion;
import no.nsd.qddt.exception.ResourceNotFoundException;
import no.nsd.qddt.repository.InstrumentQuestionRepository;
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
public class InstrumentQuestionServiceImpl implements InstrumentQuestionService {

    private InstrumentQuestionRepository instrumentQuestionRepository;

    @Autowired
    public InstrumentQuestionServiceImpl(InstrumentQuestionRepository instrumentQuestionRepository) {
        this.instrumentQuestionRepository = instrumentQuestionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public InstrumentQuestion findById(Long id) {
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
    @Transactional(readOnly = false)
    public InstrumentQuestion save(InstrumentQuestion instance) {

        instance.setCreated(LocalDateTime.now());
        return instrumentQuestionRepository.save(instance);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(InstrumentQuestion instance) {
        instrumentQuestionRepository.delete(instance);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InstrumentQuestion> findByInstrumentId(Long instrumentId) {
        return instrumentQuestionRepository.findByInstrumentId(instrumentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InstrumentQuestion> findByQuestionId(Long questionId) {
        return instrumentQuestionRepository.findByQuestionId(questionId);
    }

    @Override
    @Transactional(readOnly = true)
    public InstrumentQuestion findByGuid(UUID id) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, InstrumentQuestion> findLastChange(Long id) {
        return instrumentQuestionRepository.findLastChangeRevision(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, InstrumentQuestion> findEntityAtRevision(Long id, Integer revision) {
        return instrumentQuestionRepository.findEntityAtRevision(id, revision);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Revision<Integer, InstrumentQuestion>> findAllRevisionsPageable(Long id, Pageable pageable) {
        return instrumentQuestionRepository.findRevisions(id,pageable);
    }

}
