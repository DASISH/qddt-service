package no.nsd.qddt.domain.instrument.audit;

import no.nsd.qddt.domain.instrument.pojo.Instrument;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface InstrumentAuditRepository extends RevisionRepository<Instrument, UUID, Integer> {
}

