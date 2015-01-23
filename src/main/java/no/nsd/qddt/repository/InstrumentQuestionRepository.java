package no.nsd.qddt.repository;

import no.nsd.qddt.domain.instrument.InstrumentQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Stig Norland
 */
@Repository
public interface InstrumentQuestionRepository extends RevisionRepository<InstrumentQuestion, Long, Integer>, JpaRepository<InstrumentQuestion, Long> {}
