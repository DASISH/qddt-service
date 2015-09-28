package no.nsd.qddt.service;

import no.nsd.qddt.domain.Concept;

import java.util.UUID;

/**
 * @author Stig Norland
 */

public interface ConceptService  extends BaseServiceAudit<Concept,UUID> {

    //public Page<Concept> findSiblingsPageable(Long moduleId, Pageable pageable);

}
