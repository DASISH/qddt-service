package no.nsd.qddt.domain.instruction.audit;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.interfaces.BaseServiceAudit;
import no.nsd.qddt.domain.instruction.Instruction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;

import java.util.Collection;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface InstructionAuditService extends BaseServiceAudit<Instruction, UUID, Integer> {

    Page<Revision<Integer, Instruction>> findRevisionByIdAndChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable);
}
