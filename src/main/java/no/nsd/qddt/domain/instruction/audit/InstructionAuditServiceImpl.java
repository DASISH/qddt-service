package no.nsd.qddt.domain.instruction.audit;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.instruction.Instruction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Service("instructionAuditService")
class InstructionAuditServiceImpl implements InstructionAuditService {

    private InstructionAuditRepository instructionAuditRepository;

    @Autowired
    public InstructionAuditServiceImpl(InstructionAuditRepository instrumentRepository) {
        this.instructionAuditRepository = instrumentRepository;
    }

    @Override
    public Revision<Integer, Instruction> findLastChange(UUID uuid) {
        return instructionAuditRepository.findLastChangeRevision(uuid);
    }

    @Override
    public Revision<Integer, Instruction> findRevision(UUID uuid, Integer revision) {
        return instructionAuditRepository.findRevision(uuid, revision);
    }

    @Override
    public Page<Revision<Integer, Instruction>> findRevisions(UUID uuid, Pageable pageable) {
        return instructionAuditRepository.findRevisions(uuid, pageable);
    }

    @Override
    public Page<Revision<Integer, Instruction>> findRevisionsByChangeKindNotIn(UUID uuid, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable) {
        return instructionAuditRepository.findRevisionsByChangeKindNotIn(uuid, changeKinds,pageable);
    }
}
