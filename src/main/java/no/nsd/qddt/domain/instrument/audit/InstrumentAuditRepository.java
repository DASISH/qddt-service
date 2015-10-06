package no.nsd.qddt.domain.instrument.audit;

import no.nsd.qddt.domain.instrument.Instrument;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface InstrumentAuditRepository extends EnversRevisionRepository<Instrument, UUID, Integer> {}

