package no.nsd.qddt.domain.concept.audit;

import no.nsd.qddt.domain.concept.Concept;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
public interface ConceptAuditRepository extends RevisionRepository<Concept, UUID, Integer> {
//    Page<Revision<Integer,Concept>> findConceptByIdAndChangeKindNotIn(UUID uuid, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable);
}

