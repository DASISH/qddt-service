package no.nsd.qddt.domain.concept;

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
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */

@Service("conceptService")
class ConceptServiceImpl implements ConceptService {

    private ConceptRepository conceptRepository;

    @Autowired
    ConceptServiceImpl(ConceptRepository conceptRepository){
        this.conceptRepository = conceptRepository;
    }

    @Override
    public long count() {
        return conceptRepository.count();
    }

    @Override
    public boolean exists(UUID uuid) {
        return conceptRepository.exists(uuid);
    }

    @Override
    @Transactional(readOnly = true)
    public Concept findOne(UUID uuid) {
        return conceptRepository.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException(uuid, Concept.class));

    }

    @Override
    @Transactional(readOnly = true)
    public List<Concept> findAll() {
        return conceptRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Concept> findAll(Pageable pageable) {
        return conceptRepository.findAll(pageable);
    }

    @Override
    public List<Concept> findAll(Iterable<UUID> uuids) {
        return null;
    }


    @Override
    @Transactional(readOnly = false)
    public Concept save(Concept instance) {

        instance.setCreated(LocalDateTime.now());
        return conceptRepository.save(instance);
    }

    @Override
    public void delete(UUID uuid) {
        conceptRepository.delete(uuid);
    }

}
