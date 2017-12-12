package no.nsd.qddt.domain.author;

import no.nsd.qddt.domain.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Stig Norland
 */
@Repository
public interface AuthorRepository extends BaseRepository<Author,UUID> {

//    @Override
//    Page<Revision<Integer, Author>> findRevisions(UUID uuid, Pageable pageable);
}
