package no.nsd.qddt.domain.instrument.audit;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.interfaces.BaseServiceAudit;
import no.nsd.qddt.domain.instrument.Instrument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;

import java.util.Collection;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface InstrumentAuditService extends BaseServiceAudit<Instrument, UUID, Integer> {

    Page<Revision<Integer, Instrument>> findRevisionByIdAndChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable);
}
