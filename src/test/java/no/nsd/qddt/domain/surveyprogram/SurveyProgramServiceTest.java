package no.nsd.qddt.domain.surveyprogram;

import no.nsd.qddt.domain.AbstractServiceTest;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

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
        super.setBaseRepositories(surveyProgramRepository);
    }

    @Test
    @Override
    public void testCount() throws Exception {
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
    public void testExists() throws Exception {
        SurveyProgram surveyProgram = new SurveyProgram();
        surveyProgram.setName("Existing surveyProgramService");
        surveyProgram = surveyProgramService.save(surveyProgram);
        assertTrue("SurveyProgramService should exist", surveyProgramService.exists(surveyProgram.getId()));
    }

    @Test
    @Override
    public void testFindOne() throws Exception {
        SurveyProgram surveyProgram = new SurveyProgram();
        surveyProgram.setName("Existing surveyProgramService");
        surveyProgram = surveyProgramService.save(surveyProgram);
        assertNotNull("SurveyProgramService should not be null", surveyProgramService.findOne(surveyProgram.getId()));
    }

    @Test
    @Override
    public void testSave() throws Exception {
        SurveyProgram surveyProgram = new SurveyProgram();
        surveyProgram.setName("Existing surveyProgramService");
        assertNotNull("SurveyProgramService should be saved", surveyProgramService.save(surveyProgram));
    }

    @Test
    @Override
    public void testSaveAll() throws Exception {
        List<SurveyProgram> agencyList = new ArrayList<>();
        SurveyProgram surveyProgram = new SurveyProgram();
        surveyProgram.setName("Test SurveyProgramService One");
        agencyList.add(surveyProgram);

        surveyProgram = new SurveyProgram();
        surveyProgram.setName("Test SurveyProgramService Two");
        agencyList.add(surveyProgram);

        surveyProgram = new SurveyProgram();
        surveyProgram.setName("Test SurveyProgramService Three");
        agencyList.add(surveyProgram);

        surveyProgramService.save(agencyList);

        assertEquals("Should return 3", surveyProgramService.count(), 3L);
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

    @Test(expected = ResourceNotFoundException.class)
    @Override
    public void testDeleteAll() throws Exception {
        List<SurveyProgram> agencyList = new ArrayList<>();
        SurveyProgram surveyProgram = new SurveyProgram();
        surveyProgram.setName("Test SurveyProgramService One");
        agencyList.add(surveyProgram);

        surveyProgram = new SurveyProgram();
        surveyProgram.setName("Test SurveyProgramService Two");
        agencyList.add(surveyProgram);

        surveyProgram = new SurveyProgram();
        surveyProgram.setName("Test SurveyProgramService Three");
        agencyList.add(surveyProgram);

        agencyList = surveyProgramService.save(agencyList);
        surveyProgramService.delete(agencyList);

        agencyList.forEach(a -> assertNull("Should return null", surveyProgramService.findOne(a.getId())));

    }
}
