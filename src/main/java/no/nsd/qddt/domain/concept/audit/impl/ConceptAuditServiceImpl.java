package no.nsd.qddt.domain.concept.audit.impl;

import no.nsd.qddt.classes.AbstractAuditFilter;
import no.nsd.qddt.classes.AbstractEntityAudit;
import no.nsd.qddt.classes.elementref.ElementLoader;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.concept.audit.ConceptAuditRepository;
import no.nsd.qddt.domain.concept.audit.ConceptAuditService;
import no.nsd.qddt.domain.questionitem.QuestionItem;
import no.nsd.qddt.domain.questionitem.audit.QuestionItemAuditService;
import no.nsd.qddt.classes.exception.StackTraceFilter;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Service("conceptAuditService")
class ConceptAuditServiceImpl extends AbstractAuditFilter<Integer, Concept> implements ConceptAuditService {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private final ConceptAuditRepository conceptAuditRepository;
//    private final CommentService commentService;
    private final ElementLoader<QuestionItem> qiLoader;

    private boolean showPrivateComments;

    @Autowired
    ConceptAuditServiceImpl(ConceptAuditRepository conceptAuditRepository,
                            QuestionItemAuditService questionAuditService
    ){
        this.conceptAuditRepository = conceptAuditRepository;
        this.qiLoader = new ElementLoader<>(questionAuditService  );
    }

    @Override
    public Revision<Integer, Concept> findLastChange(UUID uuid) {
        return postLoadProcessing(conceptAuditRepository.findLastChangeRevision(uuid).orElseThrow( ResourceNotFoundException::new ));
    }


    @Override
    public Revision<Integer, Concept> findRevision(UUID uuid, Integer revision) {
        return postLoadProcessing(conceptAuditRepository.findRevision(uuid, revision).orElseThrow( ResourceNotFoundException::new ));
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
//        return conceptAuditRepository.findConceptByIdAndChangeKindNotIn(id,changeKinds,pageable);
        return null;
    }

    @Override
    public Page<Revision<Integer, Concept>> findRevisionsByChangeKindIncludeLatest(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable) {
        return getPageIncLatest(conceptAuditRepository.findRevisions(id),changeKinds,pageable);

    }
    @Override
    protected Revision<Integer, Concept> postLoadProcessing(Revision<Integer, Concept> instance) {
        assert  (instance != null);
        instance.getEntity().getVersion().setRevision( instance.getRevisionNumber().orElseThrow( ResourceNotFoundException::new ) );
        return Revision.of( instance.getMetadata(),postLoadProcessing(instance.getEntity()));
    }

    private Concept postLoadProcessing(Concept instance) {
        assert  (instance != null);
        try{
//            List<Comment> coms  =commentService.findAllByOwnerId(instance.getId(),showPrivateComments);
//            instance.setComments(new ArrayList<>(coms));
            Hibernate.initialize( instance.getComments() );
            instance.getConceptQuestionItems().forEach( qiLoader::fill );
            instance.getChildren().forEach(this::postLoadProcessing);


        } catch (Exception ex){
            LOG.error("postLoadProcessing exception",ex);
            StackTraceFilter.filter(ex.getStackTrace()).stream()
                .map( StackTraceElement::toString )
                .forEach(LOG::info);
        }
        return instance;
    }

}
