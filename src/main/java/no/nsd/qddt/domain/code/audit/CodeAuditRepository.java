package no.nsd.qddt.domain.code.audit;

import no.nsd.qddt.domain.code.Code;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface CodeAuditRepository extends EnversRevisionRepository<Code, UUID, Integer> {

}
