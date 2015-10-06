package no.nsd.qddt.domain.surveyprogram.audit;

import no.nsd.qddt.domain.surveyprogram.SurveyProgram;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface SurveyProgramAuditRepository extends EnversRevisionRepository<SurveyProgram, UUID, Integer> {

}
