package no.nsd.qddt.domain.universe.audit;

import no.nsd.qddt.domain.AbstractAuditFilter;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.universe.Universe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

/**
 * @author Stig Norland
 */

@Service("universeAuditService")
class UniverseAuditServiceImpl extends AbstractAuditFilter<Integer,Universe> implements UniverseAuditService {

    private final UniverseAuditRepository universeAuditRepository;

    @Autowired
    public UniverseAuditServiceImpl(UniverseAuditRepository instrumentRepository) {
        this.universeAuditRepository = instrumentRepository;
    }

    @Override
    public Revision<Integer, Universe> findLastChange(UUID id) {
        return universeAuditRepository.findLastChangeRevision(id).get();
    }

    @Override
    public Revision<Integer, Universe> findRevision(UUID id, Integer revision) {
        return universeAuditRepository.findRevision(id, revision).get();
    }

    @Override
    public Page<Revision<Integer, Universe>> findRevisions(UUID id, Pageable pageable) {
        return universeAuditRepository.findRevisions(id, pageable);
    }

    @Override
    public Revision<Integer, Universe> findFirstChange(UUID id) {
        return postLoadProcessing(
            universeAuditRepository.findRevisions(id).
                getContent().get(0));
    }

    @Override
    public Page<Revision<Integer, Universe>> findRevisionByIdAndChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable) {
        return getPage(universeAuditRepository.findRevisions(id),changeKinds,pageable);
    }

    // we don't have an interface for editing instructions, hence we don't need to fetch comments that never are there...
    @Override
    public void setShowPrivateComment(boolean showPrivate) {
        // no implementation
    }

    @Override
    protected Revision<Integer, Universe> postLoadProcessing(Revision<Integer, Universe> instance) {
        return instance;
    }

}

