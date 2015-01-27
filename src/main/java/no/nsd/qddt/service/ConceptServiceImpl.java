package no.nsd.qddt.service;

import no.nsd.qddt.domain.Concept;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;

/**
 * @author Stig Norland
 */
public class ConceptServiceImpl implements ConceptService {
    @Override
    public Concept findById(Long id) {
        return null;
    }

    @Override
    public Concept save(Concept concept) {
        return null;
    }

    @Override
    public Page<Concept> findByIdPageable(Long id, Pageable pageable) {
        return null;
    }

    @Override
    public void delete(Concept concept) {

    }

    @Override
    public Revision<Integer, Concept> findLastChange(Long id) {
        return null;
    }

    @Override
    public Page<Revision<Integer, Concept>> findAllRevisionsPageable(Concept concept, Pageable pageable) {
        return null;
    }
}
