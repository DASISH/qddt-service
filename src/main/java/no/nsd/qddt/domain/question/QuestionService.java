package no.nsd.qddt.domain.question;

import no.nsd.qddt.domain.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */
public interface QuestionService extends BaseService<Question, UUID> {

    /**
     *
     * @param parentId
     * @param pageable
     * @return
     */
    Page<Question> findByParentPageable(UUID parentId, Pageable pageable);

    Page<Question> findAllPageable(Pageable pageable);

	
//    Page<Question> findQuestionConceptPageable(Long id, Pageable pageable);

//    Page<Question> findQuestionInstrumentPageable(Long id, Pageable pageable);

//    Page<Question> findQuestionInstrument(Long id, Pageable pageable);
}
