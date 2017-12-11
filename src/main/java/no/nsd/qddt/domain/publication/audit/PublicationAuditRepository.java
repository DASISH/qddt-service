package no.nsd.qddt.domain.publication.audit;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.publication.Publication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface PublicationAuditRepository extends RevisionRepository<Publication, UUID, Long> {

    Page<Revision<Long,Publication>> findRevisionsByIdAndChangeKindNotIn(UUID uuid, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable);
}

