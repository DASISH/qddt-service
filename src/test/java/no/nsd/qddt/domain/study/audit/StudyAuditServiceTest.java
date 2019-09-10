package no.nsd.qddt.domain.study.audit;

import no.nsd.qddt.domain.AbstractAuditServiceTest;
import no.nsd.qddt.domain.study.Study;
import no.nsd.qddt.domain.study.StudyService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
    public void testSaveSurveyWithAudit()  {
        study = studyService.findOne(study.getId());

        // Find all revisions based on the entity id as a page
        var revisions = studyAuditService.findRevisions(
                study.getId(), PageRequest.of(0, 10));

        var wrapper = Revisions.of(revisions.getContent());

        assertEquals(wrapper.getLatestRevision().getEntity().hashCode(), study.hashCode());
        assertThat(revisions.getNumberOfElements(), is(4));
    }

    @Test
    public void getAllRevisionsTest() {
        var revisions =
                studyAuditService.findRevisions(study.getId(), PageRequest.of(0, 20));

        assertEquals("Excepted four revisions.",
                revisions.getNumberOfElements(), 4);
    }

    @Test
    public void getLastRevisionTest() {
        var  revision = studyAuditService.findLastChange(study.getId());

        assertEquals("Excepted initial ResponseDomain Object.",
                revision.getEntity().hashCode(), study.hashCode());
        assertEquals("Expected Name to be 'Third'", revision.getEntity().getName(), "Third");
    }
}

