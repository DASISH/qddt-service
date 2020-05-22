package no.nsd.qddt.domain.topicgroup.audit;

import no.nsd.qddt.domain.AbstractAuditFilter;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.comment.CommentService;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.elementref.ElementLoader;
import no.nsd.qddt.domain.questionitem.QuestionItem;
import no.nsd.qddt.domain.questionitem.audit.QuestionItemAuditService;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import no.nsd.qddt.exception.StackTraceFilter;
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
    public Revision<Integer, TopicGroup> findLastChange(UUID uuid) {
        return postLoadProcessing(topicGroupAuditRepository.findLastChangeRevision(uuid));
    }

    @Override
    public Revision<Integer, TopicGroup> findRevision(UUID uuid, Integer revision) {
        return  postLoadProcessing(topicGroupAuditRepository.findRevision(uuid, revision));
    }

    @Override
    public Page<Revision<Integer, TopicGroup>> findRevisions(UUID uuid, Pageable pageable) {
        return topicGroupAuditRepository.findRevisions(uuid, pageable)
            .map(this::postLoadProcessing);
    }

    @Override
    public Revision<Integer, TopicGroup> findFirstChange(UUID uuid) {
        return postLoadProcessing(
            topicGroupAuditRepository.findRevisions(uuid).
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
        instance.getEntity().getVersion().setRevision( instance.getRevisionNumber() );

        return new Revision<>(instance.getMetadata(),
                postLoadProcessing(instance.getEntity()));

    }

    private TopicGroup postLoadProcessing(TopicGroup instance) {
        assert  (instance != null);
        try{

            Hibernate.initialize( instance.getStudy() );

            if (instance.getParentRef() == null)
                LOG.error( "no parentRef!" );
            else
                LOG.info(instance.getParentRef().toString());

            Hibernate.initialize(instance.getConcepts());
            instance.getConcepts().forEach(this::postLoadProcessing);

            Hibernate.initialize(instance.getTopicQuestionItems());
            instance.getTopicQuestionItems().forEach( qiLoader::fill );

            instance.setComments(loadComments(instance.getId()));

        } catch (Exception ex){
            LOG.error("postLoadProcessing",ex);
            StackTraceFilter.filter(ex.getStackTrace()).stream()
                    .map( StackTraceElement::toString )
                    .forEach(LOG::info);
        }
        return instance;
    }

    private Concept postLoadProcessing(Concept instance) {
        assert  (instance != null);
        try{
            instance.getChildren().forEach(this::postLoadProcessing);
            instance.setComments(loadComments(instance.getId()));
            instance.getConceptQuestionItems().forEach( qiLoader::fill );

        } catch (Exception ex){
            LOG.error("postLoadProcessing",ex);
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
