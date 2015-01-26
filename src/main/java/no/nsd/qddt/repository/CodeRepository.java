package no.nsd.qddt.repository;

import no.nsd.qddt.domain.response.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

import java.util.List;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
public interface CodeRepository   extends RevisionRepository<Code, Long, Integer>, JpaRepository<Code, Long> {

    List<Code> findByNameIgnoreCaseContains(String tags);
}
