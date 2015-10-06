package no.nsd.qddt.domain.instrument.audit;

import no.nsd.qddt.domain.BaseServiceAudit;
import no.nsd.qddt.domain.instrument.Instrument;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface InstrumentAuditService extends BaseServiceAudit<Instrument, UUID, Integer> {

}
