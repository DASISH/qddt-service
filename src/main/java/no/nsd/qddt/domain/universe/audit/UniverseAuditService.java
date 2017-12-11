package no.nsd.qddt.domain.universe.audit;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.BaseServiceAudit;
import no.nsd.qddt.domain.universe.Universe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;

import java.util.Collection;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface UniverseAuditService extends BaseServiceAudit<Universe, UUID, Long> {

    Page<Revision<Long, Universe>> findRevisionByIdAndChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable);
}
