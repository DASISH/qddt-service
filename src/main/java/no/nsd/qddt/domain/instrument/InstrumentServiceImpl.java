package no.nsd.qddt.domain.instrument;

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
 * @author Stig Norland
 */
@Service("instrumentService")
class InstrumentServiceImpl implements InstrumentService {

    private final InstrumentRepository instrumentRepository;

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
    @Transactional()
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

    @Override
    public List<Instrument> findByStudy(UUID studyId) {
        return instrumentRepository.findByStudy(studyId);
    }

    @Override
    public Page<Instrument> findAllPageable(Pageable pageable) {
        return instrumentRepository.findAll(pageable);
    }

    @Override
    public Page<Instrument> findByNameAndDescriptionPageable(String name, String description, Pageable pageable) {
        return instrumentRepository.findByNameLikeIgnoreCaseOrDescriptionLikeIgnoreCase(name,description,pageable);
    }


    protected Instrument prePersistProcessing(Instrument instance) {
        return instance;
    }


    protected Instrument postLoadProcessing(Instrument instance) {
        return instance;
    }
}
