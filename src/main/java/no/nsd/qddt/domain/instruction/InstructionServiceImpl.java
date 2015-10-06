package no.nsd.qddt.domain.instruction;

import no.nsd.qddt.domain.instrument.Instrument;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    public long count() {
        return instructionRepository.count();
    }

    @Override
    public boolean exists(UUID uuid) {
        return instructionRepository.exists(uuid);
    }

    @Override
    public Instrument findOne(UUID uuid) {
        return instructionRepository.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException(uuid, Instrument.class));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Instrument> findAll() {
        return instructionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Instrument> findAll(Pageable pageable) {
        return instructionRepository.findAll(pageable);
    }

    @Override
    public List<Instrument> findAll(Iterable<UUID> uuids) {
        return instructionRepository.findAll(uuids);
    }

    @Override
    @Transactional(readOnly = false)
    public Instrument save(Instrument instance) {

        instance.setCreated(LocalDateTime.now());
        return instructionRepository.save(instance);
    }

    @Override
    public void delete(UUID uuid) {
        instructionRepository.delete(uuid);
    }
}
