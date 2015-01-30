package service.audit;

import no.nsd.qddt.QDDT;
import no.nsd.qddt.domain.Study;
import no.nsd.qddt.domain.Survey;
import no.nsd.qddt.service.SurveyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * This test serves as an example that Hibernat Envers is actually
 * working and that Spring Data Envers is picking up on it.
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QDDT.class)
public class SurveyServiceAuditTest {

    @Autowired
    private SurveyService surveyService;

    @Test
    public void testSaveSurveyWithAudit() throws Exception {
        Survey survey = new Survey();
        survey.setSurveyName("This is the FIRST version");

        survey = surveyService.save(survey);

        survey.setSurveyName("This is the SECOND version");
        survey = surveyService.save(survey);

        survey.setSurveyName("This is the THIRD version");
        survey = surveyService.save(survey);

        // Find the last revision based on the entity id
        Revision<Integer, Survey> revision = surveyService.findLastChange(survey.getId());

        // Find all revisions based on the entity id as a page
        Page<Revision<Integer, Survey>> revisions = surveyService.findAllRevisionsPageable(
                survey.getId(), new PageRequest(0, 10));
        assertThat(revisions.getNumberOfElements(), is(3));

        // Find all revisions
        Revisions<Integer, Survey> wrapper = new Revisions<>(revisions.getContent());
        assertThat(wrapper.getLatestRevision(), is(revision));
    }

    @Test
    public void findSurveyByRevisionTest() throws Exception {
        Revision<Integer, Survey> surveyRevision = surveyService.findLastChange(6L);

        Revision<Integer, Survey> survey = surveyService.findEntityAtRevision(
                surveyRevision.getEntity().getId(), 3);

        assertThat(survey.getRevisionNumber(), is(3));
    }
}