package no.nsd.qddt.domain.conceptquestionitem;

import no.nsd.qddt.domain.BaseService;

import java.util.Set;

/**
 * @author Stig Norland
 */
public interface ConceptQuestionItemService extends BaseService<ConceptQuestionItem, ParentQuestionItemId> {

    Set<ConceptQuestionItem> save(Set<ConceptQuestionItem> instances);

}
