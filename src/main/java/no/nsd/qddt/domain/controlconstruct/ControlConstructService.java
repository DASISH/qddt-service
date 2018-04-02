package no.nsd.qddt.domain.controlconstruct;

import no.nsd.qddt.domain.BaseService;
import no.nsd.qddt.domain.controlconstruct.json.ConstructJsonView;
import no.nsd.qddt.domain.controlconstruct.json.ConstructQuestionJson;
import no.nsd.qddt.domain.controlconstruct.pojo.ControlConstruct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface ControlConstructService extends BaseService<ControlConstruct, UUID> {

    /**
     * @param questionItemIds
     * @return
     */
    List<ConstructQuestionJson> findByQuestionItems(List<UUID> questionItemIds);

    List<ConstructQuestionJson> findTop25ByQuestionItemQuestion(String question);

    <S extends ConstructJsonView> Page<S> findByNameLikeAndControlConstructKind(String name, String question, String kind, Pageable pageable);

}
