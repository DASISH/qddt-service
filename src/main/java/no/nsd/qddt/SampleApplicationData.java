package no.nsd.qddt;

import no.nsd.qddt.domain.Study;
import no.nsd.qddt.domain.SurveyProgram;
import no.nsd.qddt.domain.User;
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

    private User user;

    public SampleApplicationData(StudyService studyService, SurveyService surveyService, UserService userService) {
        this.studyService = studyService;
        this.surveyService = surveyService;
        this.userService = userService;
        this.user = userService.findByEmail("user@example.org");

        SurveyProgram surveyProgram = addSurvey();
        addStudy(surveyProgram);
        addSecondStudy(surveyProgram);
    }

    private SurveyProgram addSurvey() {
        SurveyProgram surveyProgram = new SurveyProgram();
        surveyProgram.setCreatedBy(user);
        surveyProgram.setName("A demo survey");
        surveyProgram.setCreatedBy(userService.findByEmail("user@example.org"));

        surveyProgram.setChangeComment("Changed it.");
        surveyProgram = surveyService.save(surveyProgram);

        surveyProgram.setChangeComment("Changed it once again.");
        surveyProgram = surveyService.save(surveyProgram);

        surveyProgram.setChangeComment("Yet again reasons to change.");
        return surveyService.save(surveyProgram);
    }

    public void addStudy(SurveyProgram surveyProgram) {
        Study study = new Study();
        study.setCreatedBy(user);
        study.setName("A study of something");
        study.setChangeComment("Nothing yet.");
        study.setSurveyProgram(surveyProgram);
        study = studyService.save(study);

        study.setChangeComment("We had to change it again.");
        study = studyService.save(study);

        study.setChangeComment("Yet again it had to be changed.");
        study = studyService.save(study);

        study.setChangeComment("This is the last version");
        studyService.save(study);
    }

    public void addSecondStudy(SurveyProgram surveyProgram) {
        Study study = new Study();
        study.setCreatedBy(user);
        study.setName("Study on studies");
        study.setChangeComment("Nothing yet.");
        study.setSurveyProgram(surveyProgram);
        study = studyService.save(study);

        study.setChangeComment("Had to change study on studies.");
        study = studyService.save(study);

        study.setChangeComment("CHANGE.");
        study = studyService.save(study);

        study.setChangeComment("After the brutal CHANGE we are back with a study on studies.");
        studyService.save(study);
    }
}
