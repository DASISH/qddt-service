package no.nsd.qddt.domain.questionItem.audit;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.AbstractAuditFilter;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.comment.CommentService;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.responsedomain.web.ResponseDomainAuditController;
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
@Service("questionItemAuditService")
class QuestionItemAbstractAuditServiceImpl extends AbstractAuditFilter<Integer,QuestionItem> implements QuestionItemAuditService {

    private final QuestionItemAuditRepository questionItemAuditRepository;
    private final ResponseDomainAuditController rdAuditController;
    private final CommentService commentService;
    private boolean showPrivateComments;

    @Autowired
    public QuestionItemAbstractAuditServiceImpl(QuestionItemAuditRepository questionItemAuditRepository, ResponseDomainAuditController rdAuditController,
                                                CommentService commentService) {
        this.questionItemAuditRepository = questionItemAuditRepository;
        this.rdAuditController = rdAuditController;
        this.commentService = commentService;
    }

    @Override
    public Revision<Integer, QuestionItem> findLastChange(UUID uuid) {
        return postLoadProcessing(questionItemAuditRepository.findLastChangeRevision(uuid));
    }

    @Override
    public Revision<Integer, QuestionItem> findRevision(UUID uuid, Integer revision) {
        return postLoadProcessing(questionItemAuditRepository.findRevision(uuid, revision));
    }

    @Override
    public Page<Revision<Integer, QuestionItem>> findRevisions(UUID uuid, Pageable pageable) {
        return questionItemAuditRepository.findRevisions(uuid, pageable).
            map(this::postLoadProcessing);
    }

    @Override
    public Revision<Integer, QuestionItem> findFirstChange(UUID uuid) {
        return postLoadProcessing(
            questionItemAuditRepository.findRevisions(uuid)
                .reverse().getContent().get(0));
    }

    @Override
    public void setShowPrivateComment(boolean showPrivate) {
        showPrivateComments = showPrivate;
    }

    @Override
    public Page<Revision<Integer, QuestionItem>> findRevisionByIdAndChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable) {
        return getPage(questionItemAuditRepository.findRevisions(id),changeKinds,pageable);
    }

    @Override
    public Revision<Integer, QuestionItem> getQuestionItemLastOrRevision(UUID id, Integer revision) {
        Revision<Integer, QuestionItem> retval;
        if (revision == null || revision <= 0)
            retval = findLastChange(id);
        else
            retval = findRevision(id, revision);
        if (retval == null)
            System.out.println("getQuestionItemLastOrRevision returned with null (" + id + "," + revision + ")");
        return retval;
    }

    @Override
    protected Revision<Integer, QuestionItem> postLoadProcessing(Revision<Integer, QuestionItem> rev){
        if (rev.getEntity().getResponseDomainUUID() != null) {
            rev.getEntity().setResponseDomain(
                rdAuditController.getByRevision(
                    rev.getEntity().getResponseDomainUUID(),
                    rev.getEntity().getResponseDomainRevision().intValue()).getEntity());
        }
        List<Comment> coms;
        if (showPrivateComments)
            coms = commentService.findAllByOwnerId(rev.getEntity().getId());
        else
            coms  =commentService.findAllByOwnerIdPublic(rev.getEntity().getId());
        rev.getEntity().setComments(new HashSet<>(coms));
        return rev;
    }


}
