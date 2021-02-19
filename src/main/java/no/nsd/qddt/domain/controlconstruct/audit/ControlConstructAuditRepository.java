package no.nsd.qddt.domain.controlconstruct.audit;

import no.nsd.qddt.domain.controlconstruct.pojo.ControlConstruct;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface ControlConstructAuditRepository extends RevisionRepository<ControlConstruct, UUID, Integer> {

}
