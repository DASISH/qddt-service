package no.nsd.qddt.repository;

import no.nsd.qddt.domain.Question;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Stig Norland
 */
@Repository
public interface QuestionRepository extends EnversRevisionRepository<Question, Long, Integer> {}

