package no.nsd.qddt.domain.instruction;

import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Service("instructionService")
class InstructionServiceImpl implements InstructionService {

    private final InstructionRepository instructionRepository;

    @Autowired
    public InstructionServiceImpl(InstructionRepository instructionRepository) {
        this.instructionRepository = instructionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return instructionRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(UUID uuid) {
        return instructionRepository.exists(uuid);
    }

    @Override
    @Transactional(readOnly = true)
    public Instruction findOne(UUID uuid) {
        return instructionRepository.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException(uuid, Instruction.class));
    }

    @Override
    @Transactional()
    public Instruction save(Instruction instruction) {
        return instructionRepository.save(instruction);
    }

    @Override
    @Transactional()
    public List<Instruction> save(List<Instruction> instructions) {
        return instructionRepository.save(instructions);
    }

    @Override
    @Transactional()
    public void delete(UUID uuid) {
        instructionRepository.delete(uuid);
    }

    @Override
    @Transactional()
    public void delete(List<Instruction> instructions) {
        instructionRepository.delete(instructions);
    }


    protected Instruction prePersistProcessing(Instruction instance) {
        return instance;
    }


    protected Instruction postLoadProcessing(Instruction instance) {
        return instance;
    }

    @Override
    public Page<Instruction> findByDescriptionLike(String description, Pageable pageable) {
        return instructionRepository.findByDescriptionIgnoreCaseLike(description,pageable);
    }
}
