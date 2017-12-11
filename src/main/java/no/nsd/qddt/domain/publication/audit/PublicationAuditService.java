package no.nsd.qddt.domain.publication.audit;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.BaseServiceAudit;
import no.nsd.qddt.domain.publication.Publication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;

import java.util.Collection;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface PublicationAuditService extends BaseServiceAudit<Publication, UUID, Long> {

    Page<Revision<Long, Publication>> findRevisionByIdAndChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable);
}
