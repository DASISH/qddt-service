package no.nsd.qddt.domain.controlconstruct;

import no.nsd.qddt.domain.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface ControlConstructRepository extends BaseRepository<ControlConstruct,UUID> {

    /**
     *
     * @param instrumentId
     * @return
     */
    List<ControlConstruct> findByInstrumentId(UUID instrumentId);

    /**
     *
     * @param questionId
     * @return
     */
    List<ControlConstruct> findByquestionItemUUID(UUID questionId);


    List<ControlConstruct> findByquestionItemReferenceOnlyIdIn(List<UUID> questionItemIds);

    Page<ControlConstruct> findByInstrumentIsNullAndNameLikeIgnoreCaseOrQuestionItemReferenceOnlyQuestionQuestionLikeIgnoreCase(String name, String question, Pageable pageable);
}
