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
public interface InstrumentQuestionService  extends  AbstractServiceAudit<InstrumentQuestion> {


    public List<InstrumentQuestion> findByInstrument(Instrument instrument);

    public List<InstrumentQuestion> findByQuestion(Question question);

}
