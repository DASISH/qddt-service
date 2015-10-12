package no.nsd.qddt.domain.instruction;

import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Service("instructionService")
class InstructionServiceImpl implements InstructionService {

    private InstructionRepository instructionRepository;

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
    @Transactional(readOnly = false)
    public Instruction save(Instruction instruction) {
        return instructionRepository.save(instruction);
    }

    @Override
    @Transactional(readOnly = false)
    public List<Instruction> save(List<Instruction> instructions) {
        return instructionRepository.save(instructions);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(UUID uuid) {
        instructionRepository.delete(uuid);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(List<Instruction> instructions) {
        instructionRepository.delete(instructions);
    }
}
