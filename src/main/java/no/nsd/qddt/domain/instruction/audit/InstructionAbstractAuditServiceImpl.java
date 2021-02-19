package no.nsd.qddt.domain.instruction.audit;

import no.nsd.qddt.domain.AbstractAuditFilter;
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
class InstructionAbstractAuditServiceImpl extends AbstractAuditFilter<Integer,Instruction> implements InstructionAuditService {

    private final InstructionAuditRepository instructionAuditRepository;

    @Autowired
    public InstructionAbstractAuditServiceImpl(InstructionAuditRepository instrumentRepository) {
        this.instructionAuditRepository = instrumentRepository;
    }

    @Override
    public Revision<Integer, Instruction> findLastChange(UUID id) {
        return instructionAuditRepository.findLastChangeRevision(id).get();
    }

    @Override
    public Revision<Integer, Instruction> findRevision(UUID id, Integer revision) {
        return instructionAuditRepository.findRevision(id, revision).get();
    }

    @Override
    public Page<Revision<Integer, Instruction>> findRevisions(UUID id, Pageable pageable) {
        return instructionAuditRepository.findRevisions(id, pageable);
    }

    @Override
    public Revision<Integer, Instruction> findFirstChange(UUID id) {
        return instructionAuditRepository.findRevisions(id).reverse().getContent().get(0);
    }


    @Override
    public Page<Revision<Integer, Instruction>> findRevisionByIdAndChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable) {
        return getPage(instructionAuditRepository.findRevisions(id),changeKinds,pageable);
    }

    // we don't have an interface for editing instructions, hence we don't need to fetch comments that never are there...
    @Override
    public void setShowPrivateComment(boolean showPrivate) {
        // no implementation
    }

    @Override
    protected Revision<Integer, Instruction> postLoadProcessing(Revision<Integer, Instruction> instance) {
        return instance;
    }
    
}

