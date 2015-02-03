package no.nsd.qddt.service;

import no.nsd.qddt.domain.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */
public interface QuestionService extends BaseServiceAudit<Question> {

    Page<Question> findByParentPageable(Long parentId, Pageable pageable);

    Page<Question> findQuestionConceptPageable(Long id, Pageable pageable);

     Page<Question> findQuestionInstrumentPageable(Long id, Pageable pageable);

}
