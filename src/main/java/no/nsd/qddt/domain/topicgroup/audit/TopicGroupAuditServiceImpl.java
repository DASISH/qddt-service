package no.nsd.qddt.domain.topicgroup.audit;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.comment.CommentService;
import no.nsd.qddt.domain.conceptquestionitem.ConceptQuestionItem;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
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
@Service("topicGroupAuditService")
class TopicGroupAuditServiceImpl implements TopicGroupAuditService {

    private TopicGroupAuditRepository topicGroupAuditRepository;
    private CommentService commentService;

    @Autowired
    public TopicGroupAuditServiceImpl(TopicGroupAuditRepository topicGroupAuditRepository,CommentService commentService) {
        this.topicGroupAuditRepository = topicGroupAuditRepository;
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
                .map(c-> postLoadProcessing(c));
    }

    @Override
    public Revision<Integer, TopicGroup> findFirstChange(UUID uuid) {
        return topicGroupAuditRepository.findRevisions(uuid).
                getContent().stream()
                .min((i,o)->i.getRevisionNumber())
                .map(c-> postLoadProcessing(c))
                .get();
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
                        .map(c-> postLoadProcessing(c))
                        .collect(Collectors.toList())
        );
    }

    protected Revision<Integer, TopicGroup> postLoadProcessing(Revision<Integer, TopicGroup> instance) {
        assert  (instance != null);
        postLoadProcessing(instance.getEntity());
        return instance;
    }

    protected TopicGroup postLoadProcessing(TopicGroup instance) {
        assert  (instance != null);
        try{

//            System.out.println("TopicGroupAuditService postLoadProcessing");
            List<Comment> coms = commentService.findAllByOwnerId(instance.getId());
            instance.setComments(new HashSet<>(coms));

            instance.getConcepts().stream().forEach(c->{
                final List<Comment> coms2 = commentService.findAllByOwnerId(c.getId());
                c.setComments(new HashSet<>(coms2));
            });

        } catch (Exception ex){
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
        return instance;
    }
}
