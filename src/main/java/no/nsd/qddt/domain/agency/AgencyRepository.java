package no.nsd.qddt.domain.agency;

import no.nsd.qddt.domain.interfaces.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Stig Norland
 */
@Repository
interface AgencyRepository extends BaseRepository<Agency,UUID> {

    Page<Agency>  findByNameLike(String name, Pageable pageable);
}
