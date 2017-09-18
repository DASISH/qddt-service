package no.nsd.qddt.domain.universe.audit;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.universe.Universe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Comparator;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Dag Østgulen Heradstveit
 */
@Service("universeAuditService")
class UniverseAuditServiceImpl implements UniverseAuditService {

    private final UniverseAuditRepository universeAuditRepository;

    @Autowired
    public UniverseAuditServiceImpl(UniverseAuditRepository instrumentRepository) {
        this.universeAuditRepository = instrumentRepository;
    }

    @Override
    public Revision<Integer, Universe> findLastChange(UUID uuid) {
        return universeAuditRepository.findLastChangeRevision(uuid);
    }

    @Override
    public Revision<Integer, Universe> findRevision(UUID uuid, Integer revision) {
        return universeAuditRepository.findRevision(uuid, revision);
    }

    @Override
    public Page<Revision<Integer, Universe>> findRevisions(UUID uuid, Pageable pageable) {
        return universeAuditRepository.findRevisions(uuid, pageable);
    }

    @Override
    public Revision<Integer, Universe> findFirstChange(UUID uuid) {
        return universeAuditRepository.findRevisions(uuid).
                getContent().stream().
                min(Comparator.comparing(Revision::getRevisionNumber)).orElse(null);
    }

    @Override
    public Page<Revision<Integer, Universe>> findRevisionByIdAndChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable) {
        int skip = pageable.getOffset();
        int limit = pageable.getPageSize();
        return new PageImpl<>(
                universeAuditRepository.findRevisions(id).getContent().stream()
                        .filter(f -> !changeKinds.contains(f.getEntity().getChangeKind()))
                        .skip(skip)
                        .limit(limit)
                        .collect(Collectors.toList())
        );
    }

    // we don't have an interface for editing instructions, hence we don't need to fetch comments that never are there...
    @Override
    public void setShowPrivateComment(boolean showPrivate) {
        // no implementation
    }

//    protected Revision<Integer, Universe> postLoadProcessing(Revision<Integer, Universe> instance) {
//        assert  (instance != null);
//        postLoadProcessing(instance.getEntity());
//        return instance;
//    }
//
//    protected Universe postLoadProcessing(Universe instance) {
//        assert  (instance != null);
//        List<Comment> coms = commentService.findAllByOwnerId(instance.getId());
//        instance.setComments(new HashSet<>(coms));
//        return instance;
//    }
}

