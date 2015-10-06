package no.nsd.qddt.domain.instrumentquestion;

import no.nsd.qddt.domain.BaseService;

import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface InstrumentQuestionService  extends BaseService<InstrumentQuestion, UUID> {


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
