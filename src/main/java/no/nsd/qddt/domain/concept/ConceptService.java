package no.nsd.qddt.domain.concept;

import no.nsd.qddt.domain.BaseServiceAudit;

import java.util.UUID;

/**
 * @author Stig Norland
 */

public interface ConceptService  extends BaseServiceAudit<Concept,UUID> {

    //public Page<Concept> findSiblingsPageable(Long moduleId, Pageable pageable);

}
