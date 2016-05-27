package no.nsd.qddt.domain.questionItem;

import no.nsd.qddt.domain.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */
public interface QuestionItemService extends BaseService<QuestionItem, UUID> {

    /**
     *
     * @param pageable
     * @return
     */
    Page<QuestionItem> getHierarchy(Pageable pageable);

    Page<QuestionItem> findAllPageable(Pageable pageable);

	
//    Page<Question> findQuestionConceptPageable(Long id, Pageable pageable);

//    Page<Question> findQuestionInstrumentPageable(Long id, Pageable pageable);

//    Page<Question> findQuestionInstrument(Long id, Pageable pageable);
}