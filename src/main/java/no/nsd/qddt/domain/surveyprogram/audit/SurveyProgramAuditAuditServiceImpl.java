package no.nsd.qddt.domain.surveyprogram.audit;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.surveyprogram.SurveyProgram;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Dag Østgulen Heradstveit
 */
@Service("surveyProgramAuditService")
class SurveyProgramAuditAuditServiceImpl implements SurveyProgramAuditService {

    private SurveyProgramAuditRepository surveyProgramAuditRepository;

    @Autowired
    public SurveyProgramAuditAuditServiceImpl(SurveyProgramAuditRepository surveyProgramAuditRepository) {
        this.surveyProgramAuditRepository = surveyProgramAuditRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, SurveyProgram> findLastChange(UUID uuid) {
        return surveyProgramAuditRepository.findLastChangeRevision(uuid);
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, SurveyProgram> findRevision(UUID uuid, Integer revision) {
        return surveyProgramAuditRepository.findRevision(uuid, revision);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Revision<Integer, SurveyProgram>> findRevisions(UUID uuid, Pageable pageable) {
//        return surveyProgramAuditRepository.findRevisionsByIdAndChangeKindNotIn(uuid,
//                Arrays.asList(AbstractEntityAudit.ChangeKind.IN_DEVELOPMENT),pageable);
        return surveyProgramAuditRepository.findRevisions(uuid,pageable);
    }

    @Override
    public Revision<Integer, SurveyProgram> findFirstChange(UUID uuid) {
        return surveyProgramAuditRepository.findRevisions(uuid).
                getContent().stream().
                min((i,o)->i.getRevisionNumber()).get();
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
                        .collect(Collectors.toList())
        );
    }

}