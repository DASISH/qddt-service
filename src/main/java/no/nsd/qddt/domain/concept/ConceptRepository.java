package no.nsd.qddt.domain.concept;

import no.nsd.qddt.domain.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Stig Norland
 */
@Repository
interface ConceptRepository extends BaseRepository<Concept,UUID> {

}
