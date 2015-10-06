package no.nsd.qddt.domain.instruction.audit;

import no.nsd.qddt.domain.instruction.Instruction;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface InstructionAuditRepository extends EnversRevisionRepository<Instruction, UUID, Integer> {}

