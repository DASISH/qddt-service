package no.nsd.qddt.domain.study.audit;

import no.nsd.qddt.domain.AbstractAuditFilter;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.comment.CommentService;
import no.nsd.qddt.domain.study.Study;
import no.nsd.qddt.exception.StackTraceFilter;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author Dag Østgulen Heradstveit
 */
@Service("studyAuditService")
class StudyAuditServiceImpl extends AbstractAuditFilter<Integer,Study> implements StudyAuditService {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final StudyAuditRepository studyAuditRepository;
    private final CommentService commentService;
    private boolean showPrivateComments;

    @Autowired
    public StudyAuditServiceImpl(StudyAuditRepository studyAuditRepository, CommentService commentService) {
        this.studyAuditRepository = studyAuditRepository;
        this.commentService = commentService;
    }


    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, Study> findLastChange(UUID uuid) {
        return studyAuditRepository.findLastChangeRevision(uuid).get();
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, Study> findRevision(UUID uuid, Integer revision) {
        return studyAuditRepository.findRevision(uuid, revision).get();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Revision<Integer, Study>> findRevisions(UUID uuid, Pageable pageable) {
        return studyAuditRepository.findRevisions(uuid,pageable);
    }

    @Override
    public Revision<Integer, Study> findFirstChange(UUID uuid) {
        return postLoadProcessing(
            studyAuditRepository.findRevisions(uuid)
                .reverse().getContent().get(0));
    }

    @Override
    public void setShowPrivateComment(boolean showPrivate) {
        showPrivateComments=showPrivate;
    }

    @Override
    public Page<Revision<Integer, Study>> findRevisionByIdAndChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable) {
        return getPage(studyAuditRepository.findRevisions(id),changeKinds,pageable);
    }

    @Override
    protected Revision<Integer, Study> postLoadProcessing(Revision<Integer, Study> instance) {
        assert  (instance != null);
        instance.getEntity().getVersion().setRevision( instance.getRevisionNumber().get() );
        postLoadProcessing(instance.getEntity());
        return instance;
    }

    private Study postLoadProcessing(Study instance) {
        assert  (instance != null);
        try{
            List<Comment> coms  =commentService.findAllByOwnerId(instance.getId(),showPrivateComments);

            instance.setComments(new ArrayList<>(coms));

            Hibernate.initialize(instance.getTopicGroups());

            instance.getTopicGroups().forEach(c->{
                final List<Comment> coms2 = commentService.findAllByOwnerId(c.getId(),showPrivateComments);
                c.setComments(new ArrayList<>(coms2));
            });

        } catch (Exception ex){
            LOG.error("postLoadProcessing",ex);
            StackTraceFilter.filter(ex.getStackTrace()).stream()
                    .map(a->a.toString())
                    .forEach(LOG::info);

        }
        return instance;
    }


}
