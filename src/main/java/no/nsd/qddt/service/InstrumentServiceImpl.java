package no.nsd.qddt.service;

import no.nsd.qddt.domain.Attachment;
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
import java.util.Optional;
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
    @Transactional(readOnly = true)
    public Instrument findById(Long id) {
        return instrumentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(id, Instrument.class)
        );
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
    @Transactional(readOnly = false)
    public Instrument save(Instrument instance) {

        instance.setCreated(LocalDateTime.now());
        return instrumentRepository.save(instance);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Instrument instance) {instrumentRepository.delete(instance);   }

    @Override
    @Transactional(readOnly = true)
    public Instrument findById(UUID id) {
        return instrumentRepository.findByGuid(id).orElseThrow(
                ()-> new ResourceNotFoundException(id, Instrument.class)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, Instrument> findLastChange(Long id) {
        return instrumentRepository.findLastChangeRevision(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, Instrument> findEntityAtRevision(Long id, Integer revision) {
        return instrumentRepository.findEntityAtRevision(id, revision);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Revision<Integer, Instrument>> findAllRevisionsPageable( Long id, Pageable pageable) {
        return instrumentRepository.findRevisions(id,pageable);
    }

}
