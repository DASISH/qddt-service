package no.nsd.qddt.service;

import no.nsd.qddt.domain.Concept;
import no.nsd.qddt.repository.ConceptRepository;
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
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */

@Service("conceptService")
public class ConceptServiceImpl implements ConceptService {

    private ConceptRepository conceptRepository;

    @Autowired
    ConceptServiceImpl(ConceptRepository conceptRepository){
        this.conceptRepository = conceptRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Concept findById(Long id) {
        return conceptRepository.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Concept> findAll() {
        return conceptRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Concept> findAllPageable(Pageable pageable) {
        return conceptRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = false)
    public Concept save(Concept instance) {

        instance.setCreated(LocalDateTime.now());
        return conceptRepository.save(instance);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Concept instance) {
        conceptRepository.delete(instance);
    }

    @Override
    @Transactional(readOnly = true)
    public Concept findById(UUID id) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, Concept> findLastChange(Long id) {
        return conceptRepository.findLastChangeRevision(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, Concept> findEntityAtRevision(Long id, Integer revision) {
        return conceptRepository.findEntityAtRevision(id, revision);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Revision<Integer, Concept>> findAllRevisionsPageable(Long id, Pageable pageable) {
        return conceptRepository.findRevisions(id,pageable);
    }

//    @Override
//    @Transactional(readOnly = true)
//    public Page<Concept> findSiblingsPageable(Long moduleId, Pageable pageable) {
//
//        return conceptRepository.findByModulePageable(moduleId,pageable);
//    }

}
