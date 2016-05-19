package no.nsd.qddt.domain.controlconstruct.audit;

import no.nsd.qddt.domain.controlconstruct.ControlConstruct;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface ControlConstructAuditRepository extends EnversRevisionRepository<ControlConstruct, UUID, Integer> {

}