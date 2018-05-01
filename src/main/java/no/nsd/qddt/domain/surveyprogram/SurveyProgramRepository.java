package no.nsd.qddt.domain.surveyprogram;

import no.nsd.qddt.domain.BaseRepository;
import no.nsd.qddt.domain.agency.Agency;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface SurveyProgramRepository extends BaseRepository<SurveyProgram,UUID> {

    List<SurveyProgram> findByAgencyOrderByModifiedAsc(Agency agency);

    List<SurveyProgram> findByAgencyOrIsArchivedOrderByNameAsc(Agency agency);
}
