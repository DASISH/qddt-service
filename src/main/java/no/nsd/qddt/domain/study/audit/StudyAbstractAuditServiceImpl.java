package no.nsd.qddt.domain.study.audit;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.AbstractAuditFilter;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.comment.CommentService;
import no.nsd.qddt.domain.study.Study;
import no.nsd.qddt.exception.StackTraceFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Service("studyAuditService")
class StudyAbstractAuditServiceImpl extends AbstractAuditFilter<Long,Study> implements StudyAuditService {

    private final StudyAuditRepository studyAuditRepository;
    private final CommentService commentService;
    private boolean showPrivateComments;

    @Autowired
    public StudyAbstractAuditServiceImpl(StudyAuditRepository studyAuditRepository, CommentService commentService) {
        this.studyAuditRepository = studyAuditRepository;
        this.commentService = commentService;
    }


    @Override
    @Transactional(readOnly = true)
    public Revision<Long, Study> findLastChange(UUID uuid) {
        return studyAuditRepository.findLastChangeRevision(uuid).get();
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Long, Study> findRevision(UUID uuid, Long revision) {
        return studyAuditRepository.findRevision(uuid, revision).get();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Revision<Long, Study>> findRevisions(UUID uuid, Pageable pageable) {
        return studyAuditRepository.findRevisions(uuid,pageable);
    }

    @Override
    public Revision<Long, Study> findFirstChange(UUID uuid) {
        return postLoadProcessing(
            studyAuditRepository.findRevisions(uuid)
                .reverse().getContent().get(0));
    }

    @Override
    public void setShowPrivateComment(boolean showPrivate) {
        showPrivateComments=showPrivate;
    }

    @Override
    public Page<Revision<Long, Study>> findRevisionByIdAndChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable) {
        return getPage(studyAuditRepository.findRevisions(id),changeKinds,pageable);
    }

    @Override
    protected Revision<Long, Study> postLoadProcessing(Revision<Long, Study> instance) {
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
            StackTraceFilter.println(ex.getStackTrace());
            System.out.println(ex.getMessage());
        }
        return instance;
    }


}
