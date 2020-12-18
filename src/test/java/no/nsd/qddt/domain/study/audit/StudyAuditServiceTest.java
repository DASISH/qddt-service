package no.nsd.qddt.domain.study.audit;

import no.nsd.qddt.domain.AbstractAuditServiceTest;
import no.nsd.qddt.domain.study.Study;
import no.nsd.qddt.domain.study.StudyService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * @author Stig Norland
 */
public class StudyAuditServiceTest  extends AbstractAuditServiceTest {

    @Autowired
    private StudyService studyService;

    @Autowired
    private StudyAuditService studyAuditService;

    private Study study;

    @Before
    public void setUp() {


        study = studyService.save(new Study());

        study.setName("First");
        study = studyService.save(study);
        study.setName("Second");
        study = studyService.save(study);
        study.setName("Third");
        study = studyService.save(study);
    }

    @Test
    public void testSaveSurveyWithAudit() throws Exception {
        study = studyService.findOne(study.getId());

        // Find the last revision based on the entity id
        // Revision<Integer, Study> revision = studyAuditService.findLastChange(study.getId());

        // Find all revisions based on the entity id as a page
        Page<Revision<Integer, Study>> revisions = studyAuditService.findRevisions(
                study.getId(), new PageRequest(0, 10));

        Revisions<Integer, Study> wrapper = Revisions.of(revisions.getContent());

        assertEquals(wrapper.getLatestRevision().getEntity().hashCode(), study.hashCode());
        assertThat(revisions.getNumberOfElements(), is(4));
    }

    @Test
    public void getAllRevisionsTest() throws Exception {
        Page<Revision<Integer, Study>> revisions =
                studyAuditService.findRevisions(study.getId(), new PageRequest(0, 20));

        assertEquals("Excepted four revisions.",
                revisions.getNumberOfElements(), 4);
    }

    @Test
    public void getLastRevisionTest() throws Exception {
        Revision<Integer, Study> revision = studyAuditService.findLastChange(study.getId());

        assertEquals("Excepted initial ResponseDomain Object.",
                revision.getEntity().hashCode(), study.hashCode());
        assertEquals("Expected Name to be 'Third'", revision.getEntity().getName(), "Third");
    }
}

