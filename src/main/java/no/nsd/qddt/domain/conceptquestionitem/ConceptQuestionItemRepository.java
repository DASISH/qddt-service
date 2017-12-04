package no.nsd.qddt.domain.conceptquestionitem;

import no.nsd.qddt.domain.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Stig Norland
 */
@Repository
public interface ConceptQuestionItemRepository  extends BaseRepository<ConceptQuestionItem, ParentQuestionItemId>{

    @Query(name = "GetConceptQuestionItem")
    Optional<ConceptQuestionItem> findById(ParentQuestionItemId id);





}
