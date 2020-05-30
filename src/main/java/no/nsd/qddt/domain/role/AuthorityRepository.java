package no.nsd.qddt.domain.role;

import no.nsd.qddt.domain.interfaces.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * @author Stig Norland
 */
@Repository
interface AuthorityRepository extends BaseRepository<Authority, UUID> {

}
