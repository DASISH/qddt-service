package no.nsd.qddt.domain.concept.audit;

import no.nsd.qddt.domain.concept.Concept;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface ConceptAuditRepository extends EnversRevisionRepository<Concept, UUID, Integer> {

}