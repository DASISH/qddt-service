package no.nsd.qddt.domain.surveyprogram;

import no.nsd.qddt.domain.classes.interfaces.BaseArchivedService;
import no.nsd.qddt.domain.user.User;

import java.util.List;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
public interface SurveyProgramService extends BaseArchivedService<SurveyProgram> {

    List<SurveyProgram> findByAgency(User user);

    List<SurveyProgram> reOrder(List<SurveyOrder> surveyOrders);
}
