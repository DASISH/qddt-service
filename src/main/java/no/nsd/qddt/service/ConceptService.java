package no.nsd.qddt.service;

import no.nsd.qddt.domain.Concept;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;

/**
 * @author Stig Norland
 */
public interface ConceptService {

    public Concept findById(Long id);

    public Concept save(Concept concept);

    public Page<Concept> findByIdPageable(Long id, Pageable pageable);

    public void delete(Concept concept);

    public Revision<Integer, Concept> findLastChange(Long id);

    public Page<Revision<Integer, Concept>> findAllRevisionsPageable(Concept concept, Pageable pageable);

}
