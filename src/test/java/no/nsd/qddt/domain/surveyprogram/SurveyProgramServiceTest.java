package no.nsd.qddt.domain.surveyprogram;

import no.nsd.qddt.domain.AbstractServiceTest;
import no.nsd.qddt.classes.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by Dag Østgulen Heradstveit.
 */
public class SurveyProgramServiceTest extends AbstractServiceTest {

    @Autowired
    private SurveyProgramService surveyProgramService;

    @Autowired
    private SurveyProgramRepository surveyProgramRepository;

    @Before
    public void setup() {
        super.setup();
        super.setBaseRepositories(surveyProgramRepository);
    }

    @Test
    @Override
    public void testCount()  {
        SurveyProgram surveyProgram = new SurveyProgram();
        surveyProgram.setName("Test SurveyProgramService One");
        surveyProgramService.save(surveyProgram);

        surveyProgram = new SurveyProgram();
        surveyProgram.setName("Test SurveyProgramService Two");
        surveyProgramService.save(surveyProgram);

        surveyProgram = new SurveyProgram();
        surveyProgram.setName("Test SurveyProgramService Three");
        surveyProgramService.save(surveyProgram);

        assertThat("Should be three", surveyProgramService.count(), is(3L));
    }

    @Test
    @Override
    public void testExists()  {
        SurveyProgram surveyProgram = new SurveyProgram();
        surveyProgram.setName("Existing surveyProgramService");
        surveyProgram = surveyProgramService.save(surveyProgram);
        assertTrue("SurveyProgramService should exist", surveyProgramService.exists(surveyProgram.getId()));
    }

    @Test
    @Override
    public void testFindOne()  {
        SurveyProgram surveyProgram = new SurveyProgram();
        surveyProgram.setName("Existing surveyProgramService");
        surveyProgram = surveyProgramService.save(surveyProgram);
        assertNotNull("SurveyProgramService should not be null", surveyProgramService.findOne(surveyProgram.getId()));
    }

    @Test
    @Override
    public void testSave()  {
        SurveyProgram surveyProgram = new SurveyProgram();
        surveyProgram.setName("Existing surveyProgramService");
        assertNotNull("SurveyProgramService should be saved", surveyProgramService.save(surveyProgram));
    }


    @Test(expected = ResourceNotFoundException.class)
    @Override
    public void testDelete() throws Exception {
        SurveyProgram surveyProgram = new SurveyProgram();
        surveyProgram.setName("Existing surveyProgramService");
        surveyProgram = surveyProgramService.save(surveyProgram);
        surveyProgramService.delete(surveyProgram.getId());

        assertNull("Should return null", surveyProgramService.findOne(surveyProgram.getId()));
    }


}
