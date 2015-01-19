package no.nsd.qddt.repository;

import no.nsd.qddt.domain.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface StudyRepository  extends RevisionRepository<Study, Long, Integer>, JpaRepository<Study, Long> {
}
