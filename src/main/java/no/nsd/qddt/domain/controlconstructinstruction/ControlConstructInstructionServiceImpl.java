package no.nsd.qddt.domain.controlconstructinstruction;

import no.nsd.qddt.domain.instruction.InstructionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Service("controlConstructInstructionService")
public class ControlConstructInstructionServiceImpl implements ControlConstructInstructionService {


    private ControlConstructInstructionRepository repository;
    private InstructionService instructionService;

    @Autowired
    public ControlConstructInstructionServiceImpl(ControlConstructInstructionRepository controlConstructInstructionRepository,InstructionService instructionService) {
        this.repository = controlConstructInstructionRepository;
        this.instructionService = instructionService;
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public boolean exists(UUID uuid) {
        return repository.exists(uuid);
    }

    @Override
    public ControlConstructInstruction findOne(UUID uuid) {
        return repository.findOne(uuid);
    }


    @Override
    public ControlConstructInstruction save(ControlConstructInstruction instance) {

        if (instance.getInstruction().getId() == null)
            instance.setInstruction(instructionService.save(instance.getInstruction()));
        return repository.save(instance);
    }

    @Override
    public List<ControlConstructInstruction> save(List<ControlConstructInstruction> instances) {
        instances.forEach( i-> i.setInstruction(instructionService.save(i.getInstruction())));
        return repository.save(instances);
    }

    @Override
    public void delete(UUID uuid) {
        repository.delete(uuid);
    }


    @Override
    public void delete(List<ControlConstructInstruction> instances) {
        repository.delete(instances);
    }
}
