package no.nsd.qddt.service;

import no.nsd.qddt.domain.instrument.InstrumentQuestion;

import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface InstrumentQuestionService  extends BaseServiceAudit<InstrumentQuestion,UUID> {


    /**
     *
     * @param instrumentId
     * @return
     */
    public List<InstrumentQuestion> findByInstrumentId(UUID instrumentId);

    /**
     *
     * @param questionId
     * @return
     */
    public List<InstrumentQuestion> findByQuestionId(UUID questionId);

}
