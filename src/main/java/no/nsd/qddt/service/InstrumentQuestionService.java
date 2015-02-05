package no.nsd.qddt.service;

import no.nsd.qddt.domain.Question;
import no.nsd.qddt.domain.instrument.Instrument;
import no.nsd.qddt.domain.instrument.InstrumentQuestion;

import java.util.List;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface InstrumentQuestionService  extends BaseServiceAudit<InstrumentQuestion> {


    public List<InstrumentQuestion> findByInstrument(Long instrumentId);

    public List<InstrumentQuestion> findByQuestion(Long questionId);

}
