package no.nsd.qddt.domain.questionitem;

import no.nsd.qddt.domain.classes.interfaces.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */
public interface QuestionItemService extends BaseService<QuestionItem, UUID> {

    Page<QuestionItem> findAllPageable(Pageable pageable);

    Page<QuestionItem> findByNameOrQuestionOrResponseName(String name, String question, String responseName, String xmlLang, Pageable pageable);

}
