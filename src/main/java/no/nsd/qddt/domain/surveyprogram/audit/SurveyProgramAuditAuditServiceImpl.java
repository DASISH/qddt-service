package no.nsd.qddt.domain.surveyprogram.audit;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.comment.CommentService;
import no.nsd.qddt.domain.surveyprogram.SurveyProgram;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Dag Østgulen Heradstveit
 */
@Service("surveyProgramAuditService")
class SurveyProgramAuditAuditServiceImpl implements SurveyProgramAuditService {

    private SurveyProgramAuditRepository surveyProgramAuditRepository;
    private CommentService commentService;

    @Autowired
    public SurveyProgramAuditAuditServiceImpl(SurveyProgramAuditRepository surveyProgramAuditRepository,CommentService commentService) {
        this.surveyProgramAuditRepository = surveyProgramAuditRepository;
        this.commentService = commentService;
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, SurveyProgram> findLastChange(UUID uuid) {
        return postLoadProcessing(surveyProgramAuditRepository.findLastChangeRevision(uuid));
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, SurveyProgram> findRevision(UUID uuid, Integer revision) {
        return postLoadProcessing(surveyProgramAuditRepository.findRevision(uuid, revision));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Revision<Integer, SurveyProgram>> findRevisions(UUID uuid, Pageable pageable) {
//        return surveyProgramAuditRepository.findRevisionsByIdAndChangeKindNotIn(uuid,
//                Arrays.asList(AbstractEntityAudit.ChangeKind.IN_DEVELOPMENT),pageable);
        return surveyProgramAuditRepository.findRevisions(uuid,pageable)
                .map(c-> postLoadProcessing(c));
    }

    @Override
    public Revision<Integer, SurveyProgram> findFirstChange(UUID uuid) {
        return surveyProgramAuditRepository.findRevisions(uuid).
                getContent().stream().
                min((i,o)->i.getRevisionNumber())
                .map(c-> postLoadProcessing(c))
                .get();
    }

    @Override
    public Page<Revision<Integer, SurveyProgram>> findRevisionByIdAndChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable) {
        int skip = pageable.getOffset();
        int limit = pageable.getPageSize();
        return new PageImpl<>(
                surveyProgramAuditRepository.findRevisions(id).getContent().stream()
                        .filter(f -> !changeKinds.contains(f.getEntity().getChangeKind()))
                        .skip(skip)
                        .limit(limit)
                        .map(c-> postLoadProcessing(c))
                        .collect(Collectors.toList())
        );
    }

    protected Revision<Integer, SurveyProgram> postLoadProcessing(Revision<Integer, SurveyProgram> instance) {
        assert  (instance != null);
        postLoadProcessing(instance.getEntity());
        return instance;
    }

    protected SurveyProgram postLoadProcessing(SurveyProgram instance) {
        assert  (instance != null);
        try{
            List<Comment> coms = commentService.findAllByOwnerId(instance.getId());
            instance.setComments(new HashSet<>(coms));

            instance.getStudies().stream().forEach(c->{
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