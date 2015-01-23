package no.nsd.qddt.repository;

import no.nsd.qddt.domain.Agency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Stig Norland
 */
@Repository
public interface AgencyRepository extends RevisionRepository<Agency, Long, Integer>, JpaRepository<Agency, Long> {}
