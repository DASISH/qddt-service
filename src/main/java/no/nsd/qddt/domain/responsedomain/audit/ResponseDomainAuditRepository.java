package no.nsd.qddt.domain.responsedomain.audit;

import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface ResponseDomainAuditRepository extends EnversRevisionRepository<ResponseDomain, UUID, Integer> {
}
