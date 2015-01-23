package no.nsd.qddt.repository;

import no.nsd.qddt.domain.instrument.Instrument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Stig Norland
 */
@Repository
public interface InstrumentRepository extends RevisionRepository<Instrument, Long, Integer>, JpaRepository<Instrument, Long> {}

