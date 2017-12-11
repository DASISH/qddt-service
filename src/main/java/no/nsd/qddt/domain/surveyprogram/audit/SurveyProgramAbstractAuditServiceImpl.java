package no.nsd.qddt.domain.surveyprogram.audit;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.AbstractAuditFilter;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.comment.CommentService;
import no.nsd.qddt.domain.surveyprogram.SurveyProgram;
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
@Service("surveyProgramAuditService")
class SurveyProgramAbstractAuditServiceImpl extends AbstractAuditFilter<Long,SurveyProgram> implements SurveyProgramAuditService {

    private final SurveyProgramAuditRepository surveyProgramAuditRepository;
    private final CommentService commentService;
    private boolean showPrivateComments;

    @Autowired
    public SurveyProgramAbstractAuditServiceImpl(SurveyProgramAuditRepository surveyProgramAuditRepository, CommentService commentService) {
        this.surveyProgramAuditRepository = surveyProgramAuditRepository;
        this.commentService = commentService;
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Long, SurveyProgram> findLastChange(UUID uuid) {
        return postLoadProcessing(surveyProgramAuditRepository.findLastChangeRevision(uuid).get());
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Long, SurveyProgram> findRevision(UUID uuid, Long revision) {
        return postLoadProcessing(surveyProgramAuditRepository.findRevision(uuid, revision).get());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Revision<Long, SurveyProgram>> findRevisions(UUID uuid, Pageable pageable) {
        return surveyProgramAuditRepository.findRevisions(uuid,pageable)
            .map(this::postLoadProcessing);
    }

    @Override
    public Revision<Long, SurveyProgram> findFirstChange(UUID uuid) {
        return postLoadProcessing(
            surveyProgramAuditRepository.findRevisions(uuid)
                .reverse().getContent().get(0));
    }

    @Override
    public void setShowPrivateComment(boolean showPrivate) {
        showPrivateComments=showPrivate;
    }

    @Override
    public Page<Revision<Long, SurveyProgram>> findRevisionByIdAndChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable) {
        return getPage(surveyProgramAuditRepository.findRevisions(id),changeKinds,pageable);
    }

    @Override
    protected Revision<Long, SurveyProgram> postLoadProcessing(Revision<Long, SurveyProgram> instance) {
        assert  (instance != null);
        postLoadProcessing(instance.getEntity());
        return instance;
    }

    private SurveyProgram postLoadProcessing(SurveyProgram instance) {
        assert  (instance != null);
        try{
            List<Comment> coms;
            if (showPrivateComments)
                coms = commentService.findAllByOwnerId(instance.getId());
            else
                coms  =commentService.findAllByOwnerIdPublic(instance.getId());
            instance.setComments(new HashSet<>(coms));

            instance.getStudies().forEach(c->{
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