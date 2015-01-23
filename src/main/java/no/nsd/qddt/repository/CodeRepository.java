package no.nsd.qddt.repository;

import no.nsd.qddt.domain.response.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface CodeRepository   extends RevisionRepository<Code, Long, Integer>, JpaRepository<Code, Long> {
}
