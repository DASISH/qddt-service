package no.nsd.qddt.domain.study.audit;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.comment.CommentService;
import no.nsd.qddt.domain.study.Study;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Dag Østgulen Heradstveit
 */
@Service("studyAuditService")
class StudyAuditServiceImpl implements StudyAuditService {

    private final StudyAuditRepository studyAuditRepository;
    private final CommentService commentService;
    private boolean showPrivateComments;

    @Autowired
    public StudyAuditServiceImpl(StudyAuditRepository studyAuditRepository,CommentService commentService) {
        this.studyAuditRepository = studyAuditRepository;
        this.commentService = commentService;
    }


    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, Study> findLastChange(UUID uuid) {
        return studyAuditRepository.findLastChangeRevision(uuid);
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, Study> findRevision(UUID uuid, Integer revision) {
        return studyAuditRepository.findRevision(uuid, revision);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Revision<Integer, Study>> findRevisions(UUID uuid, Pageable pageable) {
        return studyAuditRepository.findRevisions(uuid,pageable);
    }

    @Override
    public Revision<Integer, Study> findFirstChange(UUID uuid) {
        return studyAuditRepository.findRevisions(uuid).
                getContent().stream().
                min(Comparator.comparing(Revision::getRevisionNumber)).orElse(null);
    }

    @Override
    public void setShowPrivateComment(boolean showPrivate) {
        showPrivateComments=showPrivate;
    }

    @Override
    public Page<Revision<Integer, Study>> findRevisionByIdAndChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable) {
        int skip = pageable.getOffset();
        int limit = pageable.getPageSize();
        return new PageImpl<>(
                studyAuditRepository.findRevisions(id).getContent().stream()
                        .filter(f -> !changeKinds.contains(f.getEntity().getChangeKind()))
                        .skip(skip)
                        .limit(limit)
                        .collect(Collectors.toList())
        );
    }

    protected Revision<Integer, Study> postLoadProcessing(Revision<Integer, Study> instance) {
        assert  (instance != null);
        postLoadProcessing(instance.getEntity());
        return instance;
    }

    private Study postLoadProcessing(Study instance) {
        assert  (instance != null);
        try{
            List<Comment> coms;
            if (showPrivateComments)
                coms = commentService.findAllByOwnerId(instance.getId());
            else
                coms  =commentService.findAllByOwnerIdPublic(instance.getId());
            instance.setComments(new HashSet<>(coms));

            instance.getTopicGroups().forEach(c->{
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
