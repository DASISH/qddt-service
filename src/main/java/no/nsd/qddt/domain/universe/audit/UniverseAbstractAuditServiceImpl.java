package no.nsd.qddt.domain.universe.audit;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.AbstractAuditFilter;
import no.nsd.qddt.domain.universe.Universe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Service("universeAuditService")
class UniverseAbstractAuditServiceImpl extends AbstractAuditFilter<Long,Universe> implements UniverseAuditService {

    private final UniverseAuditRepository universeAuditRepository;

    @Autowired
    public UniverseAbstractAuditServiceImpl(UniverseAuditRepository instrumentRepository) {
        this.universeAuditRepository = instrumentRepository;
    }

    @Override
    public Revision<Long, Universe> findLastChange(UUID uuid) {
        return universeAuditRepository.findLastChangeRevision(uuid).get();
    }

    @Override
    public Revision<Long, Universe> findRevision(UUID uuid, Long revision) {
        return universeAuditRepository.findRevision(uuid, revision).get();
    }

    @Override
    public Page<Revision<Long, Universe>> findRevisions(UUID uuid, Pageable pageable) {
        return universeAuditRepository.findRevisions(uuid, pageable);
    }

    @Override
    public Revision<Long, Universe> findFirstChange(UUID uuid) {
        return postLoadProcessing(
            universeAuditRepository.findRevisions(uuid).
                getContent().get(0));
    }

    @Override
    public Page<Revision<Long, Universe>> findRevisionByIdAndChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable) {
        return getPage(universeAuditRepository.findRevisions(id),changeKinds,pageable);
    }

    // we don't have an interface for editing instructions, hence we don't need to fetch comments that never are there...
    @Override
    public void setShowPrivateComment(boolean showPrivate) {
        // no implementation
    }

    @Override
    protected Revision<Long, Universe> postLoadProcessing(Revision<Long, Universe> instance) {
        return instance;
    }

}

