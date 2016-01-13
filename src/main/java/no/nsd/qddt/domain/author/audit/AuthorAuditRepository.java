package no.nsd.qddt.domain.author.audit;

import no.nsd.qddt.domain.author.Author;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Stig Norland
 */
@Repository
public interface AuthorAuditRepository extends EnversRevisionRepository<Author, UUID, Integer> {
}
