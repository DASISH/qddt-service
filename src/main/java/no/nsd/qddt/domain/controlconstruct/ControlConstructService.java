package no.nsd.qddt.domain.controlconstruct;

import no.nsd.qddt.domain.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

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
    List<ControlConstruct> findByInstrumentId(UUID instrumentId);

    /**
     *
     * @param questionItemIds
     * @return
     */
    List<ControlConstruct> findByQuestionItems(List<UUID> questionItemIds);


    @Transactional(readOnly = true)
    List<ControlConstruct> findTop25ByQuestionItemQuestion(String question);

    Page<ControlConstruct> findByNameLikeOrQuestionLike(String name, String question, Pageable pageable);
}
