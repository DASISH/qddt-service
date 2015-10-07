package no.nsd.qddt.domain.surveyprogram.audit;

import no.nsd.qddt.domain.AbstractServiceTest;
import no.nsd.qddt.domain.agency.Agency;
import no.nsd.qddt.domain.surveyprogram.SurveyProgram;
import no.nsd.qddt.domain.surveyprogram.SurveyProgramService;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class SurveyProgramAuditServicTest  extends AbstractServiceTest {

    @Autowired
    private SurveyProgramService surveyProgramService;

    @Autowired
    private SurveyProgramAuditService surveyProgramAuditService;

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
        Revision<Integer, SurveyProgram> revision = surveyProgramAuditService.findLastChange(surveyProgram.getId());

        // Find all revisions based on the entity id as a page
        Page<Revision<Integer, SurveyProgram>> revisions = surveyProgramAuditService.findRevisions(
                surveyProgram.getId(), new PageRequest(0, 10));

        Revisions<Integer, SurveyProgram> wrapper = new Revisions<>(revisions.getContent());

        assertEquals(wrapper.getLatestRevision().getEntity(), surveyProgram);
        assertThat(revisions.getNumberOfElements(), is(3));
    }

    @Test
    public void findSurveyByRevisionTest() throws Exception {
        surveyProgram = surveyProgramService.findOne(surveyProgram.getId());

        Revision<Integer, SurveyProgram> surveyRevision = surveyProgramAuditService.findLastChange(surveyProgram.getId());

        Revision<Integer, SurveyProgram> survey = surveyProgramAuditService.findRevision(surveyRevision.getEntity().getId(), 2);

        assertThat(survey.getRevisionNumber(), is(2));
    }

    @Test
    @Override
    public void testCount() throws Exception {
        SurveyProgram surveyProgram = new SurveyProgram();
        surveyProgram.setName("First");
        surveyProgramService.save(surveyProgram);

        surveyProgram = new SurveyProgram();
        surveyProgram.setName("Second");
        surveyProgramService.save(surveyProgram);

        surveyProgram = new SurveyProgram();
        surveyProgram.setName("Third");
        surveyProgramService.save(surveyProgram);

        assertThat("Should be three", surveyProgramService.count(), is(3L));

    }

    @Test
    @Override
    public void testExists() throws Exception {
        SurveyProgram surveyProgram = new SurveyProgram();
        surveyProgram.setName("First");
        surveyProgramService.save(surveyProgram);
        assertTrue("SurveyProgram should exist", surveyProgramService.exists(surveyProgram.getId()));
    }

    @Test
    @Override
    public void testFindOne() throws Exception {
        SurveyProgram surveyProgram = new SurveyProgram();
        surveyProgram.setName("First");
        surveyProgramService.save(surveyProgram);
        assertNotNull("SurveyProgram should not be null", surveyProgramService.findOne(surveyProgram.getId()));
    }

    @Test
    @Override
    public void testSave() throws Exception {
        SurveyProgram surveyProgram = new SurveyProgram();
        surveyProgram.setName("First");
        assertNotNull("SurveyProgram should be saved", surveyProgramService.save(surveyProgram));
    }

    @Test
    @Override
    public void testSaveAll() throws Exception {
        List<SurveyProgram> surveyProgramArrayList = new ArrayList<>();
        SurveyProgram surveyProgram = new SurveyProgram();
        surveyProgram.setName("First");
        surveyProgramArrayList.add(surveyProgram);

        surveyProgram = new SurveyProgram();
        surveyProgram.setName("Second");
        surveyProgramArrayList.add(surveyProgram);

        surveyProgram = new SurveyProgram();
        surveyProgram.setName("Third");
        surveyProgramArrayList.add(surveyProgram);

        assertNotNull("Should have saved 3 Surveys",surveyProgramService.save(surveyProgramArrayList));

        assertEquals("Should have saved 3 Surveys", surveyProgramService.count(), is(3L));

    }

    @Test(expected = ResourceNotFoundException.class)
    @Override
    public void testDelete() throws Exception {

    }

    @Test
    @Override
    public void testDeleteAll() throws Exception {

    }
}