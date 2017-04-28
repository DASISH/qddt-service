package no.nsd.qddt.domain.questionItem.audit;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.comment.CommentService;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.responsedomain.web.ResponseDomainAuditController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * @author Dag Østgulen Heradstveit
 */
@Service("questionItemAuditService")
class QuestionItemItemAuditServiceImpl implements QuestionItemAuditService {

    private QuestionItemAuditRepository questionItemAuditRepository;
    private ResponseDomainAuditController rdAuditController;
    private CommentService commentService;

    @Autowired
    public QuestionItemItemAuditServiceImpl(QuestionItemAuditRepository questionItemAuditRepository,ResponseDomainAuditController rdAuditController,
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
        return new PageImpl<>(questionItemAuditRepository.findRevisions(uuid,pageable).getContent().stream()
                .map(rev -> postLoadProcessing(rev))
                .collect(Collectors.toList())
        );
    }

    @Override
    public Revision<Integer, QuestionItem> findFirstChange(UUID uuid) {
        return questionItemAuditRepository.findRevisions(uuid).
                getContent().stream()
                .min((i,o)->i.getRevisionNumber())
                .map(rev -> postLoadProcessing(rev))
                .get();
    }

    @Override
    public Page<Revision<Integer, QuestionItem>> findRevisionByIdAndChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable) {
        int skip = pageable.getOffset();
        int limit = pageable.getPageSize();
        return new PageImpl<>(
                questionItemAuditRepository.findRevisions(id).getContent().stream()
                        .filter(f -> !changeKinds.contains(f.getEntity().getChangeKind()))
                        .skip(skip)
                        .limit(limit)
                        .map(rev -> postLoadProcessing(rev))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Revision<Integer, QuestionItem> getQuestionItemLastOrRevision(UUID id, Integer revision) {
        if (revision == null || revision <= 0)
            return findLastChange(id);
        else
            return findRevision(id, revision);
    }

    private Revision<Integer, QuestionItem> postLoadProcessing(Revision<Integer, QuestionItem> rev){
        if (rev.getEntity().getResponseDomainUUID() != null) {
            rev.getEntity().setResponseDomain(
                rdAuditController.getByRevision(
                    rev.getEntity().getResponseDomainUUID(),
                    rev.getEntity().getResponseDomainRevision()).getEntity());
        }
        List<Comment> coms = commentService.findAllByOwnerId( rev.getEntity().getId());
        rev.getEntity().setComments(new HashSet<>(coms));
        return rev;
    }


}
