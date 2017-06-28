package no.nsd.qddt.domain.author;

import no.nsd.qddt.domain.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface AuthorService extends BaseService<Author,UUID> {
    Page<Author> findAllPageable(Pageable pageable);
}
