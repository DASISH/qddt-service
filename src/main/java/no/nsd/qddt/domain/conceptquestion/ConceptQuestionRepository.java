package no.nsd.qddt.domain.conceptquestion;

import no.nsd.qddt.domain.BaseRepository;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Repository
interface ConceptQuestionRepository  extends BaseRepository<ConceptQuestion,UUID>,EnversRevisionRepository<ConceptQuestion, UUID, Integer> {

    /**
     *
     * @param conceptId
     * @return
     */
    List<ConceptQuestion> findByConceptId(UUID conceptId);

    /**
     *
     * @param questionId
     * @return
     */
    List<ConceptQuestion> findByQuestionId(UUID questionId);
}
