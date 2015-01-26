package no.nsd.qddt.service;

import no.nsd.qddt.domain.Question;
import no.nsd.qddt.domain.instrument.Instrument;
import no.nsd.qddt.domain.instrument.InstrumentQuestion;
import no.nsd.qddt.repository.InstrumentQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Dag Østgulen Heradstveit
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
        return instrumentQuestionRepository.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InstrumentQuestion> findAll() {
        return instrumentQuestionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = false)
    public InstrumentQuestion save(InstrumentQuestion instrumentQuestion) {
        return instrumentQuestionRepository.save(instrumentQuestion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InstrumentQuestion> findByInstrument(Instrument instrument) {
        return instrumentQuestionRepository.findByInstrument(instrument);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InstrumentQuestion> findByQuestion(Question question) {
        return instrumentQuestionRepository.findByQuestion(question);
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, InstrumentQuestion> findLastChange(Long id) {
        return instrumentQuestionRepository.findLastChangeRevision(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Revision<Integer, InstrumentQuestion>> findAllRevisionsPageable(InstrumentQuestion instrumentQuestion, int min, int max) {
        return instrumentQuestionRepository.findRevisions(instrumentQuestion.getId(), new PageRequest(0, 10));
    }
}
