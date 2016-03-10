package no.nsd.qddt.domain.surveyprogram;

import no.nsd.qddt.domain.BaseRepository;
import no.nsd.qddt.domain.agency.Agency;
import no.nsd.qddt.domain.user.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface SurveyProgramRepository extends BaseRepository<SurveyProgram,UUID> {

    List<SurveyProgram> findByModifiedByOrderByModifiedAsc(User user);

    List<SurveyProgram> findByAgencyOrderByModifiedAsc(Agency agency);
}
