package no.nsd.qddt.domain.topicgroup.audit;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.comment.CommentService;
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

    @Autowired
    public TopicGroupAuditServiceImpl(TopicGroupAuditRepository topicGroupAuditRepository,QuestionItemAuditService  questionItemAuditService,CommentService commentService) {
        this.topicGroupAuditRepository = topicGroupAuditRepository;
        this.questionItemAuditService = questionItemAuditService;
        this.commentService = commentService;
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
            List<Comment> coms = commentService.findAllByOwnerId(instance.getId());
            instance.setComments(new HashSet<>(coms));
            int size = instance.getConcepts().size();
            if (size>0)
                instance.getConcepts().forEach(c-> c.getConceptQuestionItems().forEach(cqi->{
                    Revision<Integer, QuestionItem> rev = questionItemAuditService.getQuestionItemLastOrRevision(
                            cqi.getId().getQuestionItemId(),
                            cqi.getQuestionItemRevision());
                    cqi.setQuestionItem(rev.getEntity());
                    if (rev.getEntity() == null)
                        System.out.println("Failed loading QI:" + cqi.getId() + "- rev:" + cqi.getQuestionItemRevision());
                }));


            for (TopicGroupQuestionItem cqi :instance.getTopicQuestionItems()) {
                Revision<Integer, QuestionItem> rev = questionItemAuditService.getQuestionItemLastOrRevision(
                        cqi.getId().getQuestionItemId(),
                        cqi.getQuestionItemRevision());
                cqi.setQuestionItem(rev.getEntity());
            }

        } catch (Exception ex){
            System.out.println(instance);
            ex.printStackTrace();
        }
        return instance;
    }
}
