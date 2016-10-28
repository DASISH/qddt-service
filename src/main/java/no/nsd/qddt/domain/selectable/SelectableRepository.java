package no.nsd.qddt.domain.selectable;

import no.nsd.qddt.domain.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Stig Norland
 */
@Repository
public interface SelectableRepository extends BaseRepository<Selectable,UUID>{
}
