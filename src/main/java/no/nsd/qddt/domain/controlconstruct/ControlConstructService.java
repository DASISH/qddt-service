package no.nsd.qddt.domain.controlconstruct;

import no.nsd.qddt.domain.interfaces.BaseService;
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

    <S extends ConstructJsonView> Page<S> findBySearcAndControlConstructKind(String kind, String superKind, String name, String description, String xmlLang, Pageable pageable);

    <S extends ConstructJsonView> Page<S> findQCBySearch(String name, String questionName, String questionText, String xmlLang, Pageable pageable);

}
