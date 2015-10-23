package no.nsd.qddt.domain.group.audit;

import no.nsd.qddt.domain.group.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author Stig Norland
 */
@Service("groupAuditService")
class GroupAuditServiceImpl implements GroupAuditService {

    private GroupAuditRepository repository;

    @Autowired
    GroupAuditServiceImpl(GroupAuditRepository repository){
        this.repository = repository;
    }

    @Override
    public Revision<Integer, Group> findLastChange(UUID uuid) {
        return repository.findLastChangeRevision(uuid);
    }

    @Override
    public Revision<Integer, Group> findRevision(UUID uuid, Integer revision) {
        return repository.findRevision(uuid, revision);
    }

    @Override
    public Page<Revision<Integer, Group>> findRevisions(UUID uuid, Pageable pageable) {
        return repository.findRevisions(uuid, pageable);
    }

}
