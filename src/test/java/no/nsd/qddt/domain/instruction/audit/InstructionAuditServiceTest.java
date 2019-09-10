package no.nsd.qddt.domain.instruction.audit;

import no.nsd.qddt.domain.AbstractAuditServiceTest;
import no.nsd.qddt.domain.instruction.Instruction;
import no.nsd.qddt.domain.instruction.InstructionService;
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
public class InstructionAuditServiceTest extends AbstractAuditServiceTest {

    @Autowired
    private InstructionService instructionService;

    @Autowired
    private InstructionAuditService instructionAuditService;

    private Instruction instruction;

    @Before
    public void setUp() {


        instruction = instructionService.save(new Instruction());

        instruction.setName("First");
        instruction = instructionService.save(instruction);
        instruction.setName("Second");
        instruction = instructionService.save(instruction);
        instruction.setName("Third");
        instruction = instructionService.save(instruction);
    }

    @Test
    public void testSaveInstructionWithAudit() {
        instruction = instructionService.findOne(instruction.getId());

        // Find all revisions based on the entity id as a page
        var revisions = instructionAuditService.findRevisions(instruction.getId(), PageRequest.of(0, 10));

        var wrapper = Revisions.of(revisions.getContent());

        assertEquals(wrapper.getLatestRevision().getEntity().hashCode(), instruction.hashCode());
        assertThat(4,is(revisions.getNumberOfElements()));
    }

    @Test
    public void getAllRevisionsTest() {
        var revisions = instructionAuditService.findRevisions(instruction.getId(), PageRequest.of(0, 20));

        assertEquals("Excepted four revisions.",revisions.getNumberOfElements(), 4);
    }

    @Test
    public void getLastRevisionTest() {
        var revision = instructionAuditService.findLastChange(instruction.getId());

        assertEquals("Excepted initial ResponseDomain Object.",revision.getEntity().hashCode(), instruction.hashCode());
        assertEquals("Expected Name to be 'Third'", revision.getEntity().getName(), "Third");
    }
}
