package no.nsd.qddt.domain.comment.audit;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.comment.Comment;
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
@Service("commentAuditService")
class CommentAuditServiceImpl implements CommentAuditService {

    private CommentAuditRepository commentAuditRepository;

    @Autowired
    CommentAuditServiceImpl(CommentAuditRepository commentAuditRepository){
        this.commentAuditRepository = commentAuditRepository;
    }

    @Override
    public Revision<Integer, Comment> findLastChange(UUID uuid) {
        return commentAuditRepository.findLastChangeRevision(uuid);
    }

    @Override
    public Revision<Integer, Comment> findRevision(UUID uuid, Integer revision) {
        return commentAuditRepository.findRevision(uuid, revision);
    }

    @Override
    public Page<Revision<Integer, Comment>> findRevisions(UUID uuid, Pageable pageable) {
        return commentAuditRepository.findRevisions(uuid, pageable);
    }

    @Override
    public Page<Revision<Integer, Comment>> findRevisionsByChangeKindNotIn(UUID uuid, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable) {
        return null;
    }
}
