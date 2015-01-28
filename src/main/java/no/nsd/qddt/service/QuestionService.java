package no.nsd.qddt.service;

import no.nsd.qddt.domain.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */
public interface QuestionService extends AbstractServiceAudit<Question> {

    public Page<Question> findSiblingsPageable(Long id, Pageable pageable);

    public Page<Question> findQuestionConceptPageable(Long id, Pageable pageable);

    public Page<Question> findQuestionInstrumentPageable(Long id, Pageable pageable);

}
