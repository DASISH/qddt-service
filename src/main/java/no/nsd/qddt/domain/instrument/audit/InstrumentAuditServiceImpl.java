package no.nsd.qddt.domain.instrument.audit;

import no.nsd.qddt.domain.AbstractAuditFilter;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.comment.CommentService;
import no.nsd.qddt.domain.instrument.pojo.Instrument;
import org.hibernate.Hibernate;
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
class InstrumentAuditServiceImpl extends AbstractAuditFilter<Integer,Instrument> implements InstrumentAuditService {

    private final InstrumentAuditRepository instrumentAuditRepository;
    private final CommentService commentService;
    private boolean showPrivateComments;

    @Autowired
    public InstrumentAuditServiceImpl(InstrumentAuditRepository instrumentAuditRepository, CommentService commentService) {
        this.instrumentAuditRepository = instrumentAuditRepository;
        this.commentService = commentService;
    }

    @Override
    public Revision<Integer, Instrument> findLastChange(UUID id) {
        return postLoadProcessing(instrumentAuditRepository.findLastChangeRevision(id).get());
    }

    @Override
    public Revision<Integer, Instrument> findRevision(UUID id, Integer revision) {
        return postLoadProcessing(instrumentAuditRepository.findRevision(id, revision).get());
    }

    @Override
    public Page<Revision<Integer, Instrument>> findRevisions(UUID id, Pageable pageable) {
        return instrumentAuditRepository.findRevisions(id, pageable).
                map(this::postLoadProcessing);
    }

    @Override
    public Revision<Integer, Instrument> findFirstChange(UUID id) {
        return postLoadProcessing(
            instrumentAuditRepository.findRevisions(id)
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
        instance.getEntity().getVersion().setRevision( instance.getRevisionNumber().get() );
        Hibernate.initialize(instance.getEntity().getRoot());
        List<Comment> coms  =commentService.findAllByOwnerId(instance.getEntity().getId(),showPrivateComments);
        instance.getEntity().setComments(new ArrayList<>(coms));
        return instance;
    }

}
