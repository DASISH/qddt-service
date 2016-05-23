package no.nsd.qddt.domain.controlconstruct;

import no.nsd.qddt.domain.BaseService;

import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface ControlConstructService extends BaseService<ControlConstruct, UUID> {


    /**
     *
     * @param instrumentId
     * @return
     */
    public List<ControlConstruct> findByInstrumentId(UUID instrumentId);

    /**
     *
     * @param questionItemId
     * @return
     */
    public List<ControlConstruct> findByQuestionItemId(UUID questionItemId);

}
