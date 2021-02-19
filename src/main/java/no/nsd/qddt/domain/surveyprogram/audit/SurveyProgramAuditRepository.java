package no.nsd.qddt.domain.surveyprogram.audit;

import no.nsd.qddt.domain.surveyprogram.SurveyProgram;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface SurveyProgramAuditRepository extends RevisionRepository<SurveyProgram, UUID, Integer> {


    //    Page<Revision<Integer,SurveyProgram>> findChangeKindNotInRevision(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable);
}
