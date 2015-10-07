package no.nsd.qddt.domain.study;

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
public class StudyServiceTest extends AbstractServiceTest {

    @Autowired
    private StudyService studyService;

    @Autowired
    private StudyRepository studyRepository;

    @Before
    public void setup() {
        super.setBaseRepositories(studyRepository);
    }

    @Test
    @Override
    public void testCount() throws Exception {
        Study study = new Study();
        study.setName("Test Study One");
        studyService.save(study);

        study = new Study();
        study.setName("Test Study Two");
        studyService.save(study);

        study = new Study();
        study.setName("Test Study Three");
        studyService.save(study);

        assertThat("Should be three", studyService.count(), is(3L));
    }

    @Test
    @Override
    public void testExists() throws Exception {
        Study study = new Study();
        study.setName("Existing study");
        study = studyService.save(study);
        assertTrue("Study should exist", studyService.exists(study.getId()));
    }

    @Test
    @Override
    public void testFindOne() throws Exception {
        Study study = new Study();
        study.setName("Existing study");
        study = studyService.save(study);
        assertNotNull("Study should not be null", studyService.findOne(study.getId()));
    }

    @Test
    @Override
    public void testSave() throws Exception {
        Study study = new Study();
        study.setName("Existing study");
        assertNotNull("Study should be saved", studyService.save(study));
    }

    @Test
    @Override
    public void testSaveAll() throws Exception {
        List<Study> agencyList = new ArrayList<>();
        Study study = new Study();
        study.setName("Test Study One");
        agencyList.add(study);

        study = new Study();
        study.setName("Test Study Two");
        agencyList.add(study);

        study = new Study();
        study.setName("Test Study Three");
        agencyList.add(study);

        studyService.save(agencyList);

        assertEquals("Should return 3", studyService.count(), 3L);
    }

    @Test(expected = ResourceNotFoundException.class)
    @Override
    public void testDelete() throws Exception {
        Study study = new Study();
        study.setName("Existing study");
        study = studyService.save(study);
        studyService.delete(study.getId());

        assertNull("Should return null", studyService.findOne(study.getId()));
    }

    @Test(expected = ResourceNotFoundException.class)
    @Override
    public void testDeleteAll() throws Exception {
        List<Study> agencyList = new ArrayList<>();
        Study study = new Study();
        study.setName("Test Study One");
        agencyList.add(study);

        study = new Study();
        study.setName("Test Study Two");
        agencyList.add(study);

        study = new Study();
        study.setName("Test Study Three");
        agencyList.add(study);

        agencyList = studyService.save(agencyList);
        studyService.delete(agencyList);

        agencyList.forEach(a -> assertNull("Should return null", studyService.findOne(a.getId())));

    }
}
