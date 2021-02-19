package no.nsd.qddt.domain.study.audit;

import no.nsd.qddt.domain.study.Study;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface StudyAuditRepository extends RevisionRepository<Study, UUID, Integer> {

}
