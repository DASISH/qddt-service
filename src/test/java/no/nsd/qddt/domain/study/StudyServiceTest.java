package no.nsd.qddt.domain.study;

import no.nsd.qddt.domain.AbstractServiceTest;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
        super.setup();
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


    @Test(expected = ResourceNotFoundException.class)
    @Override
    public void testDelete() throws Exception {
        Study study = new Study();
        study.setName("Existing study");
        study = studyService.save(study);
        studyService.delete(study.getId());

        assertNull("Should return null", studyService.findOne(study.getId()));
    }


}
