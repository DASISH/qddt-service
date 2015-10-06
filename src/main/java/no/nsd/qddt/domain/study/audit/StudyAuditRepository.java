package no.nsd.qddt.domain.study.audit;

import no.nsd.qddt.domain.study.Study;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface StudyAuditRepository extends EnversRevisionRepository<Study, UUID, Integer> {

}
