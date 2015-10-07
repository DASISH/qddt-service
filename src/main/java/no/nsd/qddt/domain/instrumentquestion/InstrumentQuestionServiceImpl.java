package no.nsd.qddt.domain.instrumentquestion;

import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(readOnly = false)
    public InstrumentQuestion save(InstrumentQuestion instance) {
        return instrumentQuestionRepository.save(instance);
    }

    @Override
    public List<InstrumentQuestion> save(List<InstrumentQuestion> instances) {
        return instrumentQuestionRepository.save(instances);
    }

    @Override
    public void delete(UUID uuid) {
        instrumentQuestionRepository.delete(uuid);
    }

    @Override
    public void delete(List<InstrumentQuestion> instances) {
        instrumentQuestionRepository.delete(instances);
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
}
