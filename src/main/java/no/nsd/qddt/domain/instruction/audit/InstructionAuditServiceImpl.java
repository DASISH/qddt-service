package no.nsd.qddt.domain.instruction.audit;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.instruction.Instruction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public Page<Revision<Integer, Instruction>> findRevisionByIdAndChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable) {
        int skip = pageable.getOffset();
        int limit = pageable.getPageSize();
        return new PageImpl<>(
                instructionAuditRepository.findRevisions(id).getContent().stream()
                        .filter(f -> !changeKinds.contains(f.getEntity().getChangeKind()))
                        .skip(skip)
                        .limit(limit)
                        .collect(Collectors.toList())
        );
    }

}

