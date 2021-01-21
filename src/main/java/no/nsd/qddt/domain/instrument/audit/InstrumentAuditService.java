package no.nsd.qddt.domain.instrument.audit;

import no.nsd.qddt.domain.classes.AbstractEntityAudit;
import no.nsd.qddt.domain.instrument.pojo.Instrument;
import no.nsd.qddt.domain.classes.interfaces.BaseServiceAudit;
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
