package no.nsd.qddt.domain.instrument;

import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Service("instrumentService")
class InstrumentServiceImpl implements InstrumentService {

    private InstrumentRepository instrumentRepository;

    @Autowired
    public InstrumentServiceImpl(InstrumentRepository instrumentRepository) {
        this.instrumentRepository = instrumentRepository;
    }

    @Override
    public long count() {
        return instrumentRepository.count();
    }

    @Override
    public boolean exists(UUID uuid) {
        return instrumentRepository.exists(uuid);
    }

    @Override
    public Instrument findOne(UUID uuid) {
        return instrumentRepository.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException(uuid, Instrument.class));
    }

    @Override
    @Transactional(readOnly = false)
    public Instrument save(Instrument instance) {
        return instrumentRepository.save(instance);
    }

    @Override
    public List<Instrument> save(List<Instrument> instances) {
        return instrumentRepository.save(instances);
    }

    @Override
    public void delete(UUID uuid) {
        instrumentRepository.delete(uuid);
    }

    @Override
    public void delete(List<Instrument> instances) {
        instrumentRepository.delete(instances);
    }
}
