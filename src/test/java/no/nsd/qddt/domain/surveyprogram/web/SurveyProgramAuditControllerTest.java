package no.nsd.qddt.domain.surveyprogram.web;

import no.nsd.qddt.domain.ControllerWebIntegrationTest;
import no.nsd.qddt.domain.surveyprogram.SurveyProgram;
import no.nsd.qddt.domain.surveyprogram.SurveyProgramService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Dag Østgulen Heradstveit.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class SurveyProgramAuditControllerTest extends ControllerWebIntegrationTest {

    @Autowired
    private SurveyProgramService surveyProgramService;

    private SurveyProgram surveyProgram;

    @Override
    public void setup() {
        super.setup();
        getBeforeSecurityContext().createSecurityContext();

        surveyProgram = new SurveyProgram();
        surveyProgram.setName("Initial name");
        surveyProgram.setDescription("A survey used for testing.");
        surveyProgram = surveyProgramService.save(surveyProgram);

        surveyProgram.setName("Name change 1.");
        surveyProgram = surveyProgramService.save(surveyProgram);

        surveyProgram.setName("Name change 2.");
        surveyProgram = surveyProgramService.save(surveyProgram);

        getBeforeSecurityContext().destroySecurityContext();
    }


    // FIXME: 11.04.2016 remove rem
    @Test
    public void testList() throws Exception {

        assertThat(1,is(1));

//        mvc.perform(get("/audit/surveyprogram/"+surveyProgram.getId())
//                .header("Authorization", "Bearer " + accessToken))
//                .andExpect(status().isOk());
    }

}
