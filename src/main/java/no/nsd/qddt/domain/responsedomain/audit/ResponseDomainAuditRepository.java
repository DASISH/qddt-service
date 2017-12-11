package no.nsd.qddt.domain.responsedomain.audit;

import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface ResponseDomainAuditRepository extends RevisionRepository<ResponseDomain, UUID, Long> {

//    Page<Revision<Long,ResponseDomain>> findRevisionsByIdAndChangeKindNotIn(UUID uuid, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable);


}
