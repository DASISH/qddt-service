package no.nsd.qddt;

import no.nsd.qddt.domain.Study;
import no.nsd.qddt.domain.Survey;
import no.nsd.qddt.service.StudyService;
import no.nsd.qddt.service.SurveyService;
import no.nsd.qddt.service.UserService;

/**
 * @author Dag Østgulen Heradstveit
 */
public class SampleApplicationData {

    private StudyService studyService;
    private SurveyService surveyService;
    private UserService userService;

    public SampleApplicationData(StudyService studyService, SurveyService surveyService, UserService userService) {
        this.studyService = studyService;
        this.surveyService = surveyService;
        this.userService = userService;

        Survey survey = addSurvey();

        addStudy(survey);
        addSecondStudy(survey);
    }

    private Survey addSurvey() {
        Survey survey = new Survey();
        survey.setSurveyName("A demo survey");
        survey.setCreatedBy(userService.findByEmail("user@example.org"));

        survey.setChangeComment("Changed it.");
        survey = surveyService.save(survey);

        survey.setChangeComment("Changed it once again.");
        survey = surveyService.save(survey);

        survey.setChangeComment("Yet again reasons to change.");
        return surveyService.save(survey);
    }

    public void addStudy(Survey survey) {
        Study study = new Study();
        study.setName("A study of something");
        study.setChangeComment("Nothing yet.");
        study.setSurvey(survey);
        study = studyService.save(study);

        study.setChangeComment("We had to change it again.");
        study = studyService.save(study);

        study.setChangeComment("Yet again it had to be changed.");
        study = studyService.save(study);

        study.setChangeComment("This is the last version");
        studyService.save(study);
    }

    public void addSecondStudy(Survey survey) {
        Study study = new Study();
        study.setName("Study on studies");
        study.setChangeComment("Nothing yet.");
        study.setSurvey(survey);
        study = studyService.save(study);

        study.setChangeComment("Had to change study on studies.");
        study = studyService.save(study);

        study.setChangeComment("CHANGE.");
        study = studyService.save(study);

        study.setChangeComment("After the brutal CHANGE we are back with a study on studies.");
        studyService.save(study);
    }
}
