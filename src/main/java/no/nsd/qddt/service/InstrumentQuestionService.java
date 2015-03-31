package no.nsd.qddt.service;

import no.nsd.qddt.domain.instrument.InstrumentQuestion;

import java.util.List;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface InstrumentQuestionService  extends BaseServiceAudit<InstrumentQuestion> {


    /**
     *
     * @param instrumentId
     * @return
     */
    public List<InstrumentQuestion> findByInstrumentId(Long instrumentId);

    /**
     *
     * @param questionId
     * @return
     */
    public List<InstrumentQuestion> findByQuestionId(Long questionId);

}
