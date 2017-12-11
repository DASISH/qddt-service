package no.nsd.qddt.domain.concept.audit;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.AbstractAuditFilter;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.comment.CommentService;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.conceptquestionitem.ParentQuestionItem;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.questionItem.audit.QuestionItemAuditService;
import no.nsd.qddt.exception.StackTraceFilter;
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
@Service("conceptAuditService")
class ConceptAbstractAuditServiceImpl extends AbstractAuditFilter<Long, Concept> implements ConceptAuditService  {

    private final ConceptAuditRepository conceptAuditRepository;
    private final QuestionItemAuditService questionAuditService;
    private final CommentService commentService;
    private boolean showPrivateComments;

    @Autowired
    ConceptAbstractAuditServiceImpl(ConceptAuditRepository conceptAuditRepository,
                                    QuestionItemAuditService questionAuditService,
                                    CommentService commentService
    ){
        this.conceptAuditRepository = conceptAuditRepository;
        this.questionAuditService = questionAuditService;
        this.commentService = commentService;
    }

    @Override
    public Revision<Long, Concept> findLastChange(UUID uuid) {
        return postLoadProcessing(conceptAuditRepository.findLastChangeRevision(uuid).get());
    }

    @Override
    public Revision<Long, Concept> findRevision(UUID uuid, Long revision) {
        return postLoadProcessing(conceptAuditRepository.findRevision(uuid, revision).get());
    }

    @Override
    public Page<Revision<Long, Concept>> findRevisions(UUID uuid, Pageable pageable) {
        return conceptAuditRepository.findRevisions(uuid, pageable).
                map(this::postLoadProcessing);
    }

    @Override
    public Revision<Long, Concept> findFirstChange(UUID uuid) {
        return postLoadProcessing(conceptAuditRepository.findRevisions(uuid).
            getContent().get(0));
    }

    @Override
    public void setShowPrivateComment(boolean showPrivate) {
        showPrivateComments=showPrivate;
    }

    @Override
    public Page<Revision<Long, Concept>> findRevisionsByChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable) {
        return getPage(conceptAuditRepository.findRevisions(id),changeKinds,pageable);
    }

    @Override
    public Page<Revision<Long, Concept>> findRevisionsByChangeKindIncludeLatest(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable) {
        return getPageIncLatest(conceptAuditRepository.findRevisions(id),changeKinds,pageable);

    }
    @Override
    protected Revision<Long, Concept> postLoadProcessing(Revision<Long, Concept> instance) {
        assert  (instance != null);
        return Revision.of(instance.getMetadata(),postLoadProcessing(instance.getEntity()));
    }

    private Concept postLoadProcessing(Concept instance) {
        assert  (instance != null);
//        System.out.println("Concept postLoadProcessing " + instance.getName());
        try{
            List<Comment> coms;
            if (showPrivateComments)
                coms = commentService.findAllByOwnerId(instance.getId());
            else
                coms  =commentService.findAllByOwnerIdPublic(instance.getId());
            instance.setComments(new HashSet<>(coms));
            instance.getConceptQuestionItems()
                    .forEach(cqi-> cqi.setQuestionItem(
                            getQuestionItemLastOrRevision(cqi)));

            instance.getChildren().forEach(this::postLoadProcessing);

        } catch (Exception ex){
            System.out.println("postLoadProcessing exception " + ex.getMessage());
            StackTraceFilter.println(ex.getStackTrace());
        }
        return instance;
    }

    private QuestionItem getQuestionItemLastOrRevision(ParentQuestionItem cqi){
//        System.out.println("Fetching QI " + cqi.getId() + " " + cqi.getQuestionItemRevision());

        return questionAuditService.getQuestionItemLastOrRevision(
                cqi.getId().getQuestionItemId(),
                cqi.getQuestionItemRevision()).getEntity();
    }
}
