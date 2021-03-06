package no.nsd.qddt.domain.topicgroup.audit;

import no.nsd.qddt.domain.AbstractAuditFilter;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.classes.elementref.ElementLoader;
import no.nsd.qddt.domain.classes.exception.ResourceNotFoundException;
import no.nsd.qddt.domain.classes.exception.StackTraceFilter;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.comment.CommentService;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.questionitem.QuestionItem;
import no.nsd.qddt.domain.questionitem.audit.QuestionItemAuditService;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Service("topicGroupAuditService")
class TopicGroupAuditServiceImpl extends AbstractAuditFilter<Integer,TopicGroup> implements TopicGroupAuditService {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final TopicGroupAuditRepository topicGroupAuditRepository;
    private final CommentService commentService;
    private final ElementLoader<QuestionItem> qiLoader;
    private boolean showPrivateComments;


    @Autowired
    public TopicGroupAuditServiceImpl(TopicGroupAuditRepository topicGroupAuditRepository,
                                      QuestionItemAuditService questionItemAuditService,
                                      CommentService commentService ) {
        this.topicGroupAuditRepository = topicGroupAuditRepository;
        this.commentService = commentService;
        this.qiLoader = new ElementLoader<>( questionItemAuditService );
    }

    @Override
    public Revision<Integer, TopicGroup> findLastChange(UUID id) {
        return postLoadProcessing(
            topicGroupAuditRepository.findLastChangeRevision(id).orElseThrow(() -> {
                throw  new ResourceNotFoundException(id, this.getClass());
            })
        );
    }

    @Override
    public Revision<Integer, TopicGroup> findRevision(UUID id, Integer revision) {
        return  postLoadProcessing(topicGroupAuditRepository.findRevision(id, revision).orElseThrow(() -> {
            throw  new ResourceNotFoundException(id, this.getClass());
        }));
    }

    @Override
    public Page<Revision<Integer, TopicGroup>> findRevisions(UUID id, Pageable pageable) {
        return topicGroupAuditRepository.findRevisions(id, pageable)
            .map(this::postLoadProcessing);
    }

    @Override
    public Revision<Integer, TopicGroup> findFirstChange(UUID id) {
        return postLoadProcessing(
            topicGroupAuditRepository.findRevisions(id).
                getContent().get(0));
    }

    @Override
    public void setShowPrivateComment(boolean showPrivate) {
        showPrivateComments=showPrivate;
    }

    @Override
    public Page<Revision<Integer, TopicGroup>> findRevisionByIdAndChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable) {
        return getPage(topicGroupAuditRepository.findRevisions(id),changeKinds,pageable);
    }

    @Override
    public Page<Revision<Integer, TopicGroup>> findRevisionsByChangeKindIncludeLatest(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable) {
        return getPageIncLatest(topicGroupAuditRepository.findRevisions(id),changeKinds,pageable);
    }

    @Override
    protected Revision<Integer, TopicGroup> postLoadProcessing(Revision<Integer, TopicGroup> instance) {
        assert  (instance != null);
        instance.getEntity().getVersion().setRevision( instance.getRevisionNumber().orElseThrow(() -> {
            throw  new ResourceNotFoundException(instance.getEntity().getId(), this.getClass());
        }) );

        return Revision.of(instance.getMetadata(),postLoadProcessing(instance.getEntity()));

    }

    private TopicGroup postLoadProcessing(TopicGroup instance) {
        assert  (instance != null);
        try{
//            LOG.info("postLoadProcessing starts -> " + instance.getName() + " - " +  instance.getVersion().getRevision() );
            Hibernate.initialize( instance.getStudy() );

            Hibernate.initialize(instance.getConcepts());
            instance.getConcepts().forEach(this::postLoadProcessing);

//            LOG.info(
//                instance.getConcepts().stream()
//                    .map( c -> String.valueOf(c.getConceptIdx()))
//                    .collect( Collectors.joining(",") ) );


            Hibernate.initialize(instance.getTopicQuestionItems());
            instance.getTopicQuestionItems().forEach( qiLoader::fill );

            Hibernate.initialize(instance.getOtherMaterials());

            instance.setComments(loadComments(instance.getId()));

//            LOG.info("postLoadProcessing ends -> " + instance.getName()  );

        } catch (Exception ex){
            LOG.error("postLoadProcessing(TG)",ex);
            StackTraceFilter.filter(ex.getStackTrace()).stream()
                    .map( stackTraceElement -> stackTraceElement.toString() + "Line number: "  + stackTraceElement.getLineNumber() )
                    .forEach(LOG::info);
        }
        return instance;
    }

    private Concept postLoadProcessing(Concept instance) {
        assert  (instance != null);
        try{
//            LOG.info("postLoad-concept-Processing starts -> " + instance.getName() );

            instance.getChildren().forEach(this::postLoadProcessing);
            instance.setComments(loadComments(instance.getId()));
            instance.getConceptQuestionItems().forEach( qiLoader::fill );

        } catch (Exception ex){
            LOG.error("postLoadProcessing(CO)",ex);
            StackTraceFilter.filter(ex.getStackTrace()).stream()
                    .map( StackTraceElement::toString )
                    .forEach(LOG::info);
        }
        return instance;
    }


    private List<Comment> loadComments(UUID id){
        List<Comment> coms  =commentService.findAllByOwnerId(id,showPrivateComments);

        return new ArrayList<>(coms);
    }

}
