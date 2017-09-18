package no.nsd.qddt.domain.topicgroup.audit;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.comment.CommentService;
import no.nsd.qddt.domain.conceptquestionitem.ConceptQuestionItem;
import no.nsd.qddt.domain.conceptquestionitem.ParentQuestionItem;
import no.nsd.qddt.domain.othermaterial.OtherMaterial;
import no.nsd.qddt.domain.othermaterial.OtherMaterialService;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.questionItem.audit.QuestionItemAuditService;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import no.nsd.qddt.domain.topicgroupquestionitem.TopicGroupQuestionItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Dag Østgulen Heradstveit
 */
@Service("topicGroupAuditService")
class TopicGroupAuditServiceImpl implements TopicGroupAuditService {

    private final TopicGroupAuditRepository topicGroupAuditRepository;
    private final QuestionItemAuditService  questionItemAuditService;
    private final CommentService commentService;
    private final OtherMaterialService otherMaterialService;
    private boolean showPrivateComments;

    @Autowired
    public TopicGroupAuditServiceImpl(TopicGroupAuditRepository topicGroupAuditRepository,QuestionItemAuditService  questionItemAuditService
                                      ,CommentService commentService,OtherMaterialService otherMaterialService) {
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
        return topicGroupAuditRepository.findRevisions(uuid).
                getContent().stream()
                .min(Comparator.comparing(Revision::getRevisionNumber))
                .map(this::postLoadProcessing)
                .orElse(null);
    }

    @Override
    public void setShowPrivateComment(boolean showPrivate) {
        showPrivateComments=showPrivate;
    }

    @Override
    public Page<Revision<Integer, TopicGroup>> findRevisionByIdAndChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable) {
        int skip = pageable.getOffset();
        int limit = pageable.getPageSize();
        return new PageImpl<>(
                topicGroupAuditRepository.findRevisions(id).getContent().stream()
                        .filter(f -> !changeKinds.contains(f.getEntity().getChangeKind()))
                        .skip(skip)
                        .limit(limit)
                        .map(this::postLoadProcessing)
                        .collect(Collectors.toList())
        );
    }

    private Revision<Integer, TopicGroup> postLoadProcessing(Revision<Integer, TopicGroup> instance) {
        assert  (instance != null);
        return new Revision<>(instance.getMetadata(),
                postLoadProcessing(instance.getEntity()));

    }

    private TopicGroup postLoadProcessing(TopicGroup instance) {
        assert  (instance != null);
        try{
            System.out.println("postLoadProcessing TopicGroupAuditService " + instance.getName());
            if (instance.getConcepts().size()>0)
                instance.getConcepts()
                        .forEach(c-> c.getConceptQuestionItems()
                        .forEach(cqi->cqi.setQuestionItem(getQuestionItemLastOrRevision(cqi))));

            for (TopicGroupQuestionItem cqi :instance.getTopicQuestionItems()) {
                cqi.setQuestionItem(getQuestionItemLastOrRevision(cqi));
            }

            // Manually load none audited elements

            List<OtherMaterial> oms = otherMaterialService.findBy(instance.getId());
            instance.setOtherMaterials(new HashSet<>(oms));
            List<Comment> coms;
            if (showPrivateComments)
                coms = commentService.findAllByOwnerId(instance.getId());
            else
                coms  =commentService.findAllByOwnerIdPublic(instance.getId());

            instance.setComments(new HashSet<>(coms));


        } catch (Exception ex){
            System.out.println(instance);
            ex.printStackTrace();
        }
        return instance;
    }

    private QuestionItem getQuestionItemLastOrRevision(ParentQuestionItem cqi){
        return questionItemAuditService.getQuestionItemLastOrRevision(
                cqi.getId().getQuestionItemId(),
                cqi.getQuestionItemRevision()).getEntity();
    }
}
