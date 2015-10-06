package no.nsd.qddt.domain.instruction.audit;

import no.nsd.qddt.domain.BaseServiceAudit;
import no.nsd.qddt.domain.instruction.Instruction;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface InstructionAuditService extends BaseServiceAudit<Instruction, UUID, Integer> {

}
