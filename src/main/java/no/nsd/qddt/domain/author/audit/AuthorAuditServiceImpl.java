package no.nsd.qddt.domain.author.audit;

import no.nsd.qddt.domain.author.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author Stig Norland
 */
@Service("authorAuditService")
public class AuthorAuditServiceImpl  implements AuthorAuditService{

    private AuthorAuditRepository authorAuditRepository;

    @Autowired
    AuthorAuditServiceImpl(AuthorAuditRepository authorAuditRepository){
        this.authorAuditRepository = authorAuditRepository;
    }


    @Override
    public Revision<Integer, Author> findLastChange(UUID uuid) {
        return authorAuditRepository.findLastChangeRevision(uuid);
    }

    @Override
    public Revision<Integer, Author> findRevision(UUID uuid, Integer revision) {
        return authorAuditRepository.findRevision(uuid,revision);
    }

    @Override
    public Page<Revision<Integer, Author>> findRevisions(UUID uuid, Pageable pageable) {
        return authorAuditRepository.findRevisions(uuid, pageable);
    }
}
