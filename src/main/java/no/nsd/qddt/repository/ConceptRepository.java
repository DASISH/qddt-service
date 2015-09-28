package no.nsd.qddt.repository;

import no.nsd.qddt.domain.Concept;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Stig Norland
 */
@Repository
public interface ConceptRepository extends BaseRepository<Concept,UUID>,EnversRevisionRepository<Concept, UUID, Integer> {

    //Page<Concept> findByModulePageable(Long id, Pageable pageable);
}
