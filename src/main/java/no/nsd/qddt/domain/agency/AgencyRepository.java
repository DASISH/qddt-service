package no.nsd.qddt.domain.agency;

import no.nsd.qddt.domain.BaseRepository;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Stig Norland
 */
@Repository
interface AgencyRepository extends BaseRepository<Agency,UUID>, EnversRevisionRepository<Agency, UUID, Integer> {}
