package no.nsd.qddt.domain.surveyprogram.audit;

import no.nsd.qddt.domain.AbstractAuditFilter;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.comment.CommentService;
import no.nsd.qddt.domain.surveyprogram.SurveyProgram;
import no.nsd.qddt.domain.classes.exception.StackTraceFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Service("surveyProgramAuditService")
class SurveyProgramAbstractAuditServiceImpl extends AbstractAuditFilter<Integer,SurveyProgram> implements SurveyProgramAuditService {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());
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
    public Revision<Integer, SurveyProgram> findLastChange(UUID id) {
        return postLoadProcessing(surveyProgramAuditRepository.findLastChangeRevision(id).get());
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, SurveyProgram> findRevision(UUID id, Integer revision) {
        return postLoadProcessing(surveyProgramAuditRepository.findRevision(id, revision).get());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Revision<Integer, SurveyProgram>> findRevisions(UUID id, Pageable pageable) {
        return surveyProgramAuditRepository.findRevisions(id,pageable)
            .map(this::postLoadProcessing);
    }

    @Override
    public Revision<Integer, SurveyProgram> findFirstChange(UUID id) {
        return postLoadProcessing(
            surveyProgramAuditRepository.findRevisions(id)
                .reverse().getContent().get(0));
    }

    @Override
    public void setShowPrivateComment(boolean showPrivate) {
        showPrivateComments=showPrivate;
    }

    @Override
    public Page<Revision<Integer, SurveyProgram>> findRevisionByIdAndChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable) {
        return getPage(surveyProgramAuditRepository.findRevisions(id),changeKinds,pageable);
    }

    @Override
    protected Revision<Integer, SurveyProgram> postLoadProcessing(Revision<Integer, SurveyProgram> instance) {
        assert  (instance != null);
        instance.getEntity().getVersion().setRevision( instance.getRevisionNumber().get() );
        postLoadProcessing(instance.getEntity());
        return instance;
    }

    private SurveyProgram postLoadProcessing(SurveyProgram instance) {
        assert  (instance != null);
        try{
            List<Comment> coms  =commentService.findAllByOwnerId(instance.getId(),showPrivateComments);

            instance.setComments(new ArrayList<>(coms));

            instance.getStudies().forEach(c->{
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
