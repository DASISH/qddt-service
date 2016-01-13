package no.nsd.qddt.domain.author.audit;

import no.nsd.qddt.domain.BaseServiceAudit;
import no.nsd.qddt.domain.author.Author;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface AuthorAuditService extends BaseServiceAudit<Author, UUID,Integer> {
}
