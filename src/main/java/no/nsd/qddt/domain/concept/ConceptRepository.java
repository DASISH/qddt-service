package no.nsd.qddt.domain.concept;

import no.nsd.qddt.domain.BaseRepository;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Stig Norland
 */
@Repository
interface ConceptRepository extends BaseRepository<Concept,UUID>,EnversRevisionRepository<Concept, UUID, Integer> {

    //Page<Concept> findByModulePageable(Long id, Pageable pageable);
}
