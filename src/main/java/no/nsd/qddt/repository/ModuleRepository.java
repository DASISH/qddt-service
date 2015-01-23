package no.nsd.qddt.repository;

import no.nsd.qddt.domain.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Stig Norland
 */
@Repository
public interface ModuleRepository  extends RevisionRepository<Module, Long, Integer>, JpaRepository<Module, Long> {}
