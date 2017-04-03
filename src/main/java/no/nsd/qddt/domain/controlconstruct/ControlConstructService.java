package no.nsd.qddt.domain.controlconstruct;

import no.nsd.qddt.domain.BaseService;
import no.nsd.qddt.domain.controlconstruct.jsonconverter.ConstructJson;
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
     * @param questionItemIds
     * @return
     */
    List<ConstructJson> findByQuestionItems(List<UUID> questionItemIds);


    @Transactional(readOnly = true)
    List<ConstructJson> findTop25ByQuestionItemQuestion(String question);

//    Page<ConstructJson> findByNameLikeOrQuestionLike(String name, String question, Pageable pageable);

    Page<ConstructJson> findByNameLikeAndControlConstructKind(String name, ControlConstructKind kind, Pageable pageable);

}
