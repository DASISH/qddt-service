package no.nsd.qddt.service;

import no.nsd.qddt.domain.Concept;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;

/**
 * @author Stig Norland
 */

public interface ConceptService  extends  AbstractServiceAudit<Concept>{

    public Page<Concept> findSiblingsPageable(Long moduleId, Pageable pageable);

}
