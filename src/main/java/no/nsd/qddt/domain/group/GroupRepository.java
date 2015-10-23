package no.nsd.qddt.domain.group;

import no.nsd.qddt.domain.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Stig Norland
 */
@Repository
public interface GroupRepository extends BaseRepository<Group,UUID> {

}
