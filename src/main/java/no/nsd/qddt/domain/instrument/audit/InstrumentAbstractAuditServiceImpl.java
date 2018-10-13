package no.nsd.qddt.domain.instrument.audit;

import no.nsd.qddt.domain.AbstractAuditFilter;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.comment.CommentService;
import no.nsd.qddt.domain.instrument.Instrument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Service("instrumentAuditService")
class InstrumentAbstractAuditServiceImpl extends AbstractAuditFilter<Integer,Instrument> implements InstrumentAuditService {

    private final InstrumentAuditRepository instrumentAuditRepository;
    private final CommentService commentService;
    private boolean showPrivateComments;

    @Autowired
    public InstrumentAbstractAuditServiceImpl(InstrumentAuditRepository instrumentAuditRepository, CommentService commentService) {
        this.instrumentAuditRepository = instrumentAuditRepository;
        this.commentService = commentService;
    }

    @Override
    public Revision<Integer, Instrument> findLastChange(UUID uuid) {
        return postLoadProcessing(instrumentAuditRepository.findLastChangeRevision(uuid));
    }

    @Override
    public Revision<Integer, Instrument> findRevision(UUID uuid, Integer revision) {
        return postLoadProcessing(instrumentAuditRepository.findRevision(uuid, revision));
    }

    @Override
    public Page<Revision<Integer, Instrument>> findRevisions(UUID uuid, Pageable pageable) {
        return instrumentAuditRepository.findRevisions(uuid, pageable).
                map(this::postLoadProcessing);
    }

    @Override
    public Revision<Integer, Instrument> findFirstChange(UUID uuid) {
        return postLoadProcessing(
            instrumentAuditRepository.findRevisions(uuid)
                .reverse().getContent().get(0));
    }

    @Override
    public void setShowPrivateComment(boolean showPrivate) {
        showPrivateComments = showPrivate;
    }

    @Override
    public Page<Revision<Integer, Instrument>> findRevisionByIdAndChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable) {
        return getPage(instrumentAuditRepository.findRevisions(id),changeKinds,pageable);
    }

    @Override
    protected Revision<Integer, Instrument> postLoadProcessing(Revision<Integer, Instrument> instance) {
        assert  (instance != null);
        instance.getEntity().getVersion().setRevision( instance.getRevisionNumber() );
        List<Comment> coms  =commentService.findAllByOwnerId(instance.getEntity().getId(),showPrivateComments);
        instance.getEntity().setComments(new ArrayList<>(coms));
        return instance;
    }

}
