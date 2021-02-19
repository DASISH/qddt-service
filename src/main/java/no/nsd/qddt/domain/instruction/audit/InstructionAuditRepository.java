package no.nsd.qddt.domain.instruction.audit;

import no.nsd.qddt.domain.instruction.Instruction;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface InstructionAuditRepository extends RevisionRepository<Instruction, UUID, Integer> {

}

