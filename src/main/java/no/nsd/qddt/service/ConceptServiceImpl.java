package no.nsd.qddt.service;

import no.nsd.qddt.domain.Concept;
import no.nsd.qddt.repository.ConceptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Stig Norland
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
    @Transactional(readOnly = false)
    public Concept save(Concept concept) {
        return conceptRepository.save(concept);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Concept> findByIdPageable(Long id, Pageable pageable) {
        return conceptRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Concept concept) {
        conceptRepository.delete(concept);
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, Concept> findLastChange(Long id) {
        return conceptRepository.findLastChangeRevision(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Revision<Integer, Concept>> findAllRevisionsPageable(Concept concept, Pageable pageable) {
        return conceptRepository.findRevisions(concept.getId(),pageable);
    }
}
