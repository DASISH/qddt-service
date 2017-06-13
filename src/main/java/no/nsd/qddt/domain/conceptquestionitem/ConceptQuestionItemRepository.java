package no.nsd.qddt.domain.conceptquestionitem;

import no.nsd.qddt.domain.BaseRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Stig Norland
 */
@Repository
public interface ConceptQuestionItemRepository  extends BaseRepository<ConceptQuestionItem, ParentQuestionItemId>{

}
