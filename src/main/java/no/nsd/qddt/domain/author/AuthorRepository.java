package no.nsd.qddt.domain.author;

import no.nsd.qddt.classes.interfaces.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Stig Norland
 */
@Repository
public interface AuthorRepository extends BaseRepository<Author,UUID> {

    Page<Author> findAuthorsByAboutContainingOrNameContainingOrEmailContaining(String about, String name, String email, Pageable pageable);
//    @Override
//    Page<Revision<Integer, Author>> findRevisions(UUID uuid, Pageable pageable);
}
