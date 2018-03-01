package no.nsd.qddt.domain.surveyprogram;

import no.nsd.qddt.domain.ArchivableService;
import no.nsd.qddt.domain.user.User;

import java.util.List;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
public interface SurveyProgramService extends ArchivableService<SurveyProgram> {

    List<SurveyProgram> findByModifiedBy(User user);

    List<SurveyProgram> findByAgency(User user);

}
