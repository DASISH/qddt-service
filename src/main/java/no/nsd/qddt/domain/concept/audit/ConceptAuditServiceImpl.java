package no.nsd.qddt.domain.concept.audit;

import no.nsd.qddt.domain.AbstractAuditFilter;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.comment.CommentService;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.elementref.ElementLoader;
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
class ConceptAuditServiceImpl extends AbstractAuditFilter<Integer, Concept> implements ConceptAuditService  {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private final ConceptAuditRepository conceptAuditRepository;
    private final CommentService commentService;
    private final ElementLoader qiLoader;

    private boolean showPrivateComments;

    @Autowired
    ConceptAuditServiceImpl(ConceptAuditRepository conceptAuditRepository,
                                    QuestionItemAuditService questionAuditService,
                                    CommentService commentService
    ){
        this.conceptAuditRepository = conceptAuditRepository;
        this.commentService = commentService;
        this.qiLoader = new ElementLoader( questionAuditService );
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
        try{
            List<Comment> coms;
            if (showPrivateComments)
                coms = commentService.findAllByOwnerId(instance.getId());
            else
                coms  =commentService.findAllByOwnerIdPublic(instance.getId());

            instance.setComments(new HashSet<>(coms));
            instance.getConceptQuestionItems().forEach( cqi -> qiLoader.fill( cqi ));
            instance.getChildren().forEach(this::postLoadProcessing);

//                cqi.getElement().setConceptRefs(
//                    findByQuestionItem(cqi.getId()).stream()
//                        .map( ConceptRef::new )
//                        .collect( Collectors.toList())
//                );

        } catch (Exception ex){
            LOG.error("postLoadProcessing exception",ex);
            StackTraceFilter.filter(ex.getStackTrace()).stream()
                .map(a->a.toString())
                .forEach(LOG::info);
        }
        return instance;
    }

}
