package no.nsd.qddt.domain.surveyprogram;

import no.nsd.qddt.domain.interfaces.BaseArchivedRepository;
import no.nsd.qddt.domain.agency.Agency;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Repository
interface SurveyProgramRepository extends BaseArchivedRepository<SurveyProgram,UUID> {

    List<SurveyProgram> findByAgencyOrderByModifiedAsc(Agency agency);

    List<SurveyProgram> findByAgencyOrIsArchivedOrderByNameAsc(Agency agency, boolean isArchived);
}
