package no.nsd.qddt.repository;

import no.nsd.qddt.domain.Agency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Repository
public interface AgencyRepository extends BaseRepository<Agency,UUID>, EnversRevisionRepository<Agency, UUID, Integer> {}
