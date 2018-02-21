package no.nsd.qddt.domain.topicgroup.audit;

import no.nsd.qddt.domain.AbstractAuditFilter;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.comment.CommentService;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.concept.ConceptQuestionItemRev;
import no.nsd.qddt.domain.othermaterial.OtherMaterialService;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.questionItem.audit.QuestionItemAuditService;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import no.nsd.qddt.domain.topicgroupquestionitem.TopicGroupQuestionItem;
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
@Service("topicGroupAuditService")
class TopicGroupAbstractAuditServiceImpl extends AbstractAuditFilter<Integer,TopicGroup> implements TopicGroupAuditService {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final TopicGroupAuditRepository topicGroupAuditRepository;
    private final QuestionItemAuditService  questionItemAuditService;
    private final CommentService commentService;
    private final OtherMaterialService otherMaterialService;
    private boolean showPrivateComments;

    @Autowired
    public TopicGroupAbstractAuditServiceImpl(TopicGroupAuditRepository topicGroupAuditRepository, QuestionItemAuditService  questionItemAuditService
                                      , CommentService commentService, OtherMaterialService otherMaterialService) {
        this.topicGroupAuditRepository = topicGroupAuditRepository;
        this.questionItemAuditService = questionItemAuditService;
        this.commentService = commentService;
        this.otherMaterialService = otherMaterialService;
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
        return new Revision<>(instance.getMetadata(),
                postLoadProcessing(instance.getEntity()));

    }

    private TopicGroup postLoadProcessing(TopicGroup instance) {
        assert  (instance != null);
        try{
            instance.getConcepts().forEach(this::postLoadProcessing);

            for (TopicGroupQuestionItem cqi :instance.getTopicQuestionItems()) {
                cqi.setQuestionItem(getQuestionItemLastOrRevision(
                    cqi.getId().getQuestionItemId(),
                    cqi.getQuestionItemRevision().intValue()));
            }

//            List<OtherMaterial> oms = otherMaterialService.findBy(instance.getId());
//            instance.setOtherMaterials(oms.stream()
//                .map( c-> (OtherMaterialT)c )
//                .collect( Collectors.toSet() ) );

            instance.setComments(loadComments(instance.getId()));

        } catch (Exception ex){
            LOG.error("postLoadProcessing",ex);
            StackTraceFilter.filter(ex.getStackTrace()).stream()
                    .map(a->a.toString())
                    .forEach(LOG::info);
        }
        return instance;
    }

    private Concept postLoadProcessing(Concept instance) {
        assert  (instance != null);
        try{
            instance.setComments(loadComments(instance.getId()));
            instance.getConceptQuestionItems()
                .forEach(cqi-> cqi.setQuestionItem(
                    getQuestionItemLastOrRevision(cqi)));

            instance.getChildren().forEach(this::postLoadProcessing);

        } catch (Exception ex){
            LOG.error("postLoadProcessing",ex);
            StackTraceFilter.filter(ex.getStackTrace()).stream()
                    .map(a->a.toString())
                    .forEach(LOG::info);
        }
        return instance;
    }


    private HashSet<Comment> loadComments(UUID id){
        List<Comment> coms;
        if (showPrivateComments)
            coms = commentService.findAllByOwnerId(id);
        else
            coms  =commentService.findAllByOwnerIdPublic(id);
        return new HashSet<>(coms);
    }


    private QuestionItem getQuestionItemLastOrRevision(ConceptQuestionItemRev cqi){
        return questionItemAuditService.getQuestionItemLastOrRevision(
            cqi.getQuestionId(),
            cqi.getQuestionItemRevision().intValue()).getEntity();
    }

    private QuestionItem getQuestionItemLastOrRevision(UUID id, Integer rev){
        return questionItemAuditService.getQuestionItemLastOrRevision(id,rev).getEntity();
    }
}
