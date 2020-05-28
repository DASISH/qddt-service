package no.nsd.qddt.domain.author;

import no.nsd.qddt.domain.interfaces.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface AuthorService extends BaseService<Author,UUID> {
    Page<Author> findAllPageable(Pageable pageable);

    Page<Author> findbyPageable(String name, String about, String email, Pageable pageable);

}
