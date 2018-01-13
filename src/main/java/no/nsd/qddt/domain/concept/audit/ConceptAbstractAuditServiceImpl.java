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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
class ConceptAbstractAuditServiceImpl extends AbstractAuditFilter<Integer, Concept> implements ConceptAuditService  {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());
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
    public Revision<Integer, Concept> findLastChange(UUID uuid) {
        return postLoadProcessing(conceptAuditRepository.findLastChangeRevision(uuid));
    }

    @Override
    public Revision<Integer, Concept> findRevision(UUID uuid, Integer revision) {
        return postLoadProcessing(conceptAuditRepository.findRevision(uuid, revision));
    }

    @Override
    public Page<Revision<Integer, Concept>> findRevisions(UUID uuid, Pageable pageable) {
        return conceptAuditRepository.findRevisions(uuid, pageable).
                map(this::postLoadProcessing);
    }

    @Override
    public Revision<Integer, Concept> findFirstChange(UUID uuid) {
        return postLoadProcessing(conceptAuditRepository.findRevisions(uuid).
            getContent().get(0));
    }

    @Override
    public void setShowPrivateComment(boolean showPrivate) {
        showPrivateComments=showPrivate;
    }

    @Override
    public Page<Revision<Integer, Concept>> findRevisionsByChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable) {
        return getPage(conceptAuditRepository.findRevisions(id),changeKinds,pageable);
    }

    @Override
    public Page<Revision<Integer, Concept>> findRevisionsByChangeKindIncludeLatest(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable) {
        return getPageIncLatest(conceptAuditRepository.findRevisions(id),changeKinds,pageable);

    }
    @Override
    protected Revision<Integer, Concept> postLoadProcessing(Revision<Integer, Concept> instance) {
        assert  (instance != null);
        return new Revision<>(
                instance.getMetadata(),
                postLoadProcessing(instance.getEntity()));
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
            LOG.error("postLoadProcessing exception",ex);
            StackTraceFilter.filter(ex.getStackTrace()).stream()
                .map(a->a.toString())
                .forEach(LOG::info);
        }
        return instance;
    }

    private QuestionItem getQuestionItemLastOrRevision(ParentQuestionItem cqi){
//        System.out.println("Fetching QI " + cqi.getId() + " " + cqi.getQuestionItemRevision());

        return questionAuditService.getQuestionItemLastOrRevision(
                cqi.getId().getQuestionItemId(),
                cqi.getQuestionItemRevision().intValue()).getEntity();
    }
}
