package no.nsd.qddt.repository;

import no.nsd.qddt.domain.Module;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Stig Norland
 */
@Repository
public interface ModuleRepository  extends BaseRepository<Module>, EnversRevisionRepository<Module, Long, Integer> {}
