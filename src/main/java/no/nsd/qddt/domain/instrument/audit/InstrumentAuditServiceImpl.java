package no.nsd.qddt.domain.instrument.audit;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.comment.CommentService;
import no.nsd.qddt.domain.instrument.Instrument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Dag Østgulen Heradstveit
 */
@Service("instrumentAuditService")
class InstrumentAuditServiceImpl implements InstrumentAuditService {

    private final InstrumentAuditRepository instrumentAuditRepository;
    private final CommentService commentService;
    private boolean showPrivateComments;

    @Autowired
    public InstrumentAuditServiceImpl(InstrumentAuditRepository instrumentAuditRepository, CommentService commentService) {
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
        return instrumentAuditRepository.findRevisions(uuid).
                getContent().stream().
                min(Comparator.comparing(Revision::getRevisionNumber)).
                map(this::postLoadProcessing).
                orElse(null);
    }

    @Override
    public void setShowPrivateComment(boolean showPrivate) {
        showPrivateComments = showPrivate;
    }

    @Override
    public Page<Revision<Integer, Instrument>> findRevisionByIdAndChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable) {
        int skip = pageable.getOffset();
        int limit = pageable.getPageSize();
        return new PageImpl<>(
                instrumentAuditRepository.findRevisions(id).getContent().stream()
                        .filter(f -> !changeKinds.contains(f.getEntity().getChangeKind()))
                        .skip(skip)
                        .limit(limit)
                        .map(this::postLoadProcessing)
                        .collect(Collectors.toList())
        );
    }


    private Revision<Integer, Instrument> postLoadProcessing(Revision<Integer, Instrument> instance) {
        assert  (instance != null);
        postLoadProcessing(instance.getEntity());
        return instance;
    }

    private Instrument postLoadProcessing(Instrument instance) {
        assert  (instance != null);
        List<Comment> coms;
        if (showPrivateComments)
            coms = commentService.findAllByOwnerId(instance.getId());
        else
            coms  =commentService.findAllByOwnerIdPublic(instance.getId());
        instance.setComments(new HashSet<>(coms));
        return instance;
    }
}
