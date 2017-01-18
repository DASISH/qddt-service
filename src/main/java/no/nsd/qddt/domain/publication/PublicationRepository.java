package no.nsd.qddt.domain.publication;

import no.nsd.qddt.domain.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Stig Norland
 */
@Repository
public interface PublicationRepository extends BaseRepository<Publication,UUID>{
}
