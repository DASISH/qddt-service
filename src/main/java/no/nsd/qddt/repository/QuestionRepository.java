package no.nsd.qddt.repository;

import no.nsd.qddt.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Stig Norland
 */
@Repository
public interface QuestionRepository  extends RevisionRepository<Question, Long, Integer>, JpaRepository<Question, Long> {}

