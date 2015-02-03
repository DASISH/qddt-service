package no.nsd.qddt.repository;

import no.nsd.qddt.domain.Agency;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Stig Norland
 */
@Repository
public interface AgencyRepository extends BaseRepository<Agency>, EnversRevisionRepository<Agency, Long, Integer> {}
