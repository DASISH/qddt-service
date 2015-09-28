package no.nsd.qddt.service;

import no.nsd.qddt.domain.instrument.Instrument;
import no.nsd.qddt.exception.ResourceNotFoundException;
import no.nsd.qddt.repository.InstrumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
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
public class InstrumentServiceImpl implements InstrumentService {

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
    @Transactional(readOnly = true)
    public List<Instrument> findAll() {
        return instrumentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Instrument> findAll(Pageable pageable) {
        return instrumentRepository.findAll(pageable);
    }

    @Override
    public List<Instrument> findAll(Iterable<UUID> uuids) {
        return instrumentRepository.findAll(uuids);
    }

    @Override
    @Transactional(readOnly = false)
    public Instrument save(Instrument instance) {

        instance.setCreated(LocalDateTime.now());
        return instrumentRepository.save(instance);
    }

    @Override
    public void delete(UUID uuid) {
        instrumentRepository.delete(uuid);
    }


    @Override
    public Revision<Integer, Instrument> findLastChange(UUID uuid) {
        return instrumentRepository.findLastChangeRevision(uuid);
    }

    @Override
    public Revision<Integer, Instrument> findEntityAtRevision(UUID uuid, Integer revision) {
        return instrumentRepository.findEntityAtRevision(uuid,revision);
    }

    @Override
    public Page<Revision<Integer, Instrument>> findAllRevisionsPageable(UUID uuid, Pageable pageable) {
        return instrumentRepository.findRevisions(uuid,pageable);
    }
}
