package no.nsd.qddt.domain.concept;

import no.nsd.qddt.domain.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface ConceptService  extends BaseService<Concept, UUID> {

    Page<Concept> findAllPageable(Pageable pageable);

    Page<Concept> findByTopicGroupPageable(UUID id, Pageable pageable);

    Page<Concept> findByNameAndDescriptionPageable(String name, String name1, Pageable pageable);
}
