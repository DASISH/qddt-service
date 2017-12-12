package no.nsd.qddt.domain.surveyprogram.audit;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.surveyprogram.SurveyProgram;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface SurveyProgramAuditRepository extends RevisionRepository<SurveyProgram, UUID, Integer> {


    Page<Revision<Integer,SurveyProgram>> findRevisionsByIdAndChangeKindNotIn(UUID uuid, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable);

//    Page<Revision<Integer,SurveyProgram>> findChangeKindNotInRevision(UUID uuid, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable);
}
