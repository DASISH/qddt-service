package no.nsd.qddt.repository;

import no.nsd.qddt.domain.Study;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
public interface StudyRepository extends EnversRevisionRepository<Study, Long, Integer> {
}
