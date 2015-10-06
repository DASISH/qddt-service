package service.audit;

import no.nsd.qddt.QDDT;
import no.nsd.qddt.domain.surveyprogram.SurveyProgram;
import no.nsd.qddt.domain.surveyprogram.SurveyProgramService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * This test serves as an example that Hibernat Envers is actually
 * working and that Spring Data Envers is picking up on it.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QDDT.class)
public class SurveyProgramServiceAuditTest {

    @Autowired
    private SurveyProgramService surveyProgramService;

    SurveyProgram surveyProgram;

    @Before
    public void setup(){
        surveyProgram = new SurveyProgram();
        surveyProgram.setName("This is the FIRST version");

        surveyProgram = surveyProgramService.save(surveyProgram);

        surveyProgram.setName("This is the SECOND version");
        surveyProgram = surveyProgramService.save(surveyProgram);

        surveyProgram.setName("This is the THIRD version");
        surveyProgram = surveyProgramService.save(surveyProgram);
    }


    @Test
    public void testSaveSurveyWithAudit() throws Exception {
        surveyProgram = surveyProgramService.findOne(surveyProgram.getId());

        // Find the last revision based on the entity id
        Revision<Integer, SurveyProgram> revision = surveyProgramService.findLastChange(surveyProgram.getId());

        // Find all revisions based on the entity id as a page
        Page<Revision<Integer, SurveyProgram>> revisions = surveyProgramService.findAllRevisionsPageable(
                surveyProgram.getId(), new PageRequest(0, 10));

        Revisions<Integer, SurveyProgram> wrapper = new Revisions<>(revisions.getContent());

        assertEquals(wrapper.getLatestRevision().getEntity(), surveyProgram);
        assertThat(revisions.getNumberOfElements(), is(3));
    }

    @Test
    public void findSurveyByRevisionTest() throws Exception {
        surveyProgram = surveyProgramService.findOne(surveyProgram.getId());

        Revision<Integer, SurveyProgram> surveyRevision = surveyProgramService.findLastChange(surveyProgram.getId());

        Revision<Integer, SurveyProgram> survey = surveyProgramService.findEntityAtRevision(
                surveyRevision.getEntity().getId(), 2);

        assertThat(survey.getRevisionNumber(), is(2));
    }
}