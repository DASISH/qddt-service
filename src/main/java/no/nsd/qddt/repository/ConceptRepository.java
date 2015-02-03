package no.nsd.qddt.repository;

import no.nsd.qddt.domain.Concept;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Stig Norland
 */
@Repository
public interface ConceptRepository extends BaseRepository<Concept>,EnversRevisionRepository<Concept, Long, Integer> {

    //Page<Concept> findByModulePageable(Long id, Pageable pageable);
}
