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
public class StudyServiceAuditTest  extends AbstractAuditServiceTest {

    @Autowired
    private StudyService instructionService;

    @Autowired
    private StudyAuditService instructionAuditService;

    private Study instruction;

    @Before
    public void setUp() {


        instruction = instructionService.save(new Study());

        instruction.setName("First");
        instruction = instructionService.save(instruction);
        instruction.setName("Second");
        instruction = instructionService.save(instruction);
        instruction.setName("Third");
        instruction = instructionService.save(instruction);
    }

    @Test
    public void testSaveSurveyWithAudit() throws Exception {
        instruction = instructionService.findOne(instruction.getId());

        // Find the last revision based on the entity id
        Revision<Integer, Study> revision = instructionAuditService.findLastChange(instruction.getId());

        // Find all revisions based on the entity id as a page
        Page<Revision<Integer, Study>> revisions = instructionAuditService.findRevisions(
                instruction.getId(), new PageRequest(0, 10));

        Revisions<Integer, Study> wrapper = new Revisions<>(revisions.getContent());

        assertEquals(wrapper.getLatestRevision().getEntity(), instruction);
        assertThat(revisions.getNumberOfElements(), is(4));
    }

    @Test
    public void getAllRevisionsTest() throws Exception {
        Page<Revision<Integer, Study>> revisions =
                instructionAuditService.findRevisions(instruction.getId(), new PageRequest(0, 20));

        assertEquals("Excepted four revisions.",
                revisions.getNumberOfElements(), 4);
    }

    @Test
    public void getLastRevisionTest() throws Exception {
        Revision<Integer, Study> revision = instructionAuditService.findLastChange(instruction.getId());

        assertEquals("Excepted initial ResponseDomain Object.",
                revision.getEntity(), instruction);
        assertEquals("Expected Name to be 'Third'", revision.getEntity().getName(), "Third");
    }
}

