package no.nsd.qddt.repository;

import no.nsd.qddt.domain.ChangeReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Stig Norland
 */
@Repository
public interface ChangeReasonRepository extends RevisionRepository<ChangeReason, Long, Integer>, JpaRepository<ChangeReason, Long> {}
