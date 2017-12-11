package no.nsd.qddt.domain.author;

import no.nsd.qddt.domain.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Stig Norland
 */
@Repository
public interface AuthorRepository extends BaseRepository<Author,UUID> {

    @Override
    Page<Revision<Long, Author>> findRevisions(UUID uuid, Pageable pageable);
}
