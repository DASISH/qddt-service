package no.nsd.qddt.service;

import no.nsd.qddt.domain.Question;
import no.nsd.qddt.domain.instrument.Instrument;
import no.nsd.qddt.domain.instrument.InstrumentQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.history.Revision;

import java.util.List;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface InstrumentQuestionService {

    public InstrumentQuestion findById(Long id);

    public List<InstrumentQuestion> findAll();

    public InstrumentQuestion save(InstrumentQuestion instrumentQuestion);

    public List<InstrumentQuestion> findByInstrument(Instrument instrument);

    public List<InstrumentQuestion> findByQuestion(Question question);

    public Revision<Integer, InstrumentQuestion> findLastChange(Long id);

    public Page<Revision<Integer, InstrumentQuestion>> findAllRevisionsPageable(InstrumentQuestion instrumentQuestion , int min, int max);
}
