package no.nsd.qddt.domain.instrument.audit;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.AbstractAuditFilter;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.comment.CommentService;
import no.nsd.qddt.domain.instrument.Instrument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Service("instrumentAuditService")
class InstrumentAbstractAuditServiceImpl extends AbstractAuditFilter<Long,Instrument> implements InstrumentAuditService {

    private final InstrumentAuditRepository instrumentAuditRepository;
    private final CommentService commentService;
    private boolean showPrivateComments;

    @Autowired
    public InstrumentAbstractAuditServiceImpl(InstrumentAuditRepository instrumentAuditRepository, CommentService commentService) {
        this.instrumentAuditRepository = instrumentAuditRepository;
        this.commentService = commentService;
    }

    @Override
    public Revision<Long, Instrument> findLastChange(UUID uuid) {
        return postLoadProcessing(instrumentAuditRepository.findLastChangeRevision(uuid).get());
    }

    @Override
    public Revision<Long, Instrument> findRevision(UUID uuid, Long revision) {
        return postLoadProcessing(instrumentAuditRepository.findRevision(uuid, revision).get());
    }

    @Override
    public Page<Revision<Long, Instrument>> findRevisions(UUID uuid, Pageable pageable) {
        return instrumentAuditRepository.findRevisions(uuid, pageable).
                map(this::postLoadProcessing);
    }

    @Override
    public Revision<Long, Instrument> findFirstChange(UUID uuid) {
        return postLoadProcessing(
            instrumentAuditRepository.findRevisions(uuid)
                .reverse().getContent().get(0));
    }

    @Override
    public void setShowPrivateComment(boolean showPrivate) {
        showPrivateComments = showPrivate;
    }

    @Override
    public Page<Revision<Long, Instrument>> findRevisionByIdAndChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable) {
        return getPage(instrumentAuditRepository.findRevisions(id),changeKinds,pageable);
    }

    @Override
    protected Revision<Long, Instrument> postLoadProcessing(Revision<Long, Instrument> instance) {
        assert  (instance != null);
        List<Comment> coms;
        if (showPrivateComments)
            coms = commentService.findAllByOwnerId(instance.getEntity().getId());
        else
            coms  =commentService.findAllByOwnerIdPublic(instance.getEntity().getId());
        instance.getEntity().setComments(new HashSet<>(coms));;
        return instance;
    }

}
