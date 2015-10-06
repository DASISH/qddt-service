package no.nsd.qddt.domain.agency.audit;

import no.nsd.qddt.domain.agency.Agency;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface AgencyAuditRepository extends EnversRevisionRepository<Agency, UUID, Integer> {

}