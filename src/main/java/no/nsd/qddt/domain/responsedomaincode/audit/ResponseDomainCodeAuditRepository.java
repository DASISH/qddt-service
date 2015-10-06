package no.nsd.qddt.domain.responsedomaincode.audit;

import no.nsd.qddt.domain.responsedomaincode.ResponseDomainCode;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface ResponseDomainCodeAuditRepository extends EnversRevisionRepository<ResponseDomainCode, UUID, Integer> {

}