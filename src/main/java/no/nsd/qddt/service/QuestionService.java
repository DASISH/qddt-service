package no.nsd.qddt.service;

import no.nsd.qddt.domain.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */
public interface QuestionService extends BaseServiceAudit<Question> {

    /**
     *
     * @param parentId
     * @param pageable
     * @return
     */
    Page<Question> findByParentPageable(Long parentId, Pageable pageable);

    /**
     *
     * @param parentId
     * @param pageable
     * @return
     */
    Page<Question> findByParentPageable(UUID parentId, Pageable pageable);

//    Page<Question> findQuestionConceptPageable(Long id, Pageable pageable);

//    Page<Question> findQuestionInstrumentPageable(Long id, Pageable pageable);

//    Page<Question> findQuestionInstrument(Long id, Pageable pageable);
}
