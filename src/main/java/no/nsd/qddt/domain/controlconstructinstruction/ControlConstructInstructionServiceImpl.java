package no.nsd.qddt.domain.controlconstructinstruction;

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

    @Autowired
    public ControlConstructInstructionServiceImpl(ControlConstructInstructionRepository controlConstructInstructionRepository) {
        this.repository = controlConstructInstructionRepository;
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
        return repository.save(instance);
    }

    @Override
    public List<ControlConstructInstruction> save(List<ControlConstructInstruction> instances) {
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
