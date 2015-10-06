package no.nsd.qddt.domain.surveyprogram;

import no.nsd.qddt.domain.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface SurveyProgramRepository extends BaseRepository<SurveyProgram,UUID> {

}
