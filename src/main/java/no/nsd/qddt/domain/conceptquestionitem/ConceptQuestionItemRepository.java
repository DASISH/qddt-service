package no.nsd.qddt.domain.conceptquestionitem;

import no.nsd.qddt.domain.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Repository
public interface ConceptQuestionItemRepository  extends BaseRepository<ConceptQuestionItem, ConceptQuestionItemId>{

}
