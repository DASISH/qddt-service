package no.nsd.qddt.domain.surveyprogram;

import no.nsd.qddt.domain.BaseService;
import no.nsd.qddt.domain.user.User;

import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
public interface SurveyProgramService extends BaseService<SurveyProgram, UUID> {

    List<SurveyProgram> findByCreatedBy(User user);
}
