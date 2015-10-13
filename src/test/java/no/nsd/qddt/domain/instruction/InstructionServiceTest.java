package no.nsd.qddt.domain.instruction;

import no.nsd.qddt.domain.AbstractServiceTest;

import no.nsd.qddt.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

/**
 * @author Stig Norland
 */
public class InstructionServiceTest extends AbstractServiceTest {

    @Autowired
    private InstructionService instructionService;

    @Autowired
    private InstructionRepository instructionRepository;

    @Before
    public void setup() {
        super.setBaseRepositories(instructionRepository);
    }

    @Test
    @Override
    public void testCount() throws Exception {
        Instruction instruction = new Instruction();
        instruction.setName("Test Instruction One");
        instructionService.save(instruction);

        instruction = new Instruction();
        instruction.setName("Test Instruction Two");
        instructionService.save(instruction);

        instruction = new Instruction();
        instruction.setName("Test Instruction Three");
        instructionService.save(instruction);

        assertThat("Should be three", instructionService.count(), is(3L));
    }

    @Test
    @Override
    public void testExists() throws Exception {
        Instruction instruction = new Instruction();
        instruction.setName("Existing instruction");
        instruction = instructionService.save(instruction);
        assertTrue("Instruction should exist", instructionService.exists(instruction.getId()));
    }

    @Test
    @Override
    public void testFindOne() throws Exception {
        Instruction instruction = new Instruction();
        instruction.setName("Existing instruction");
        instruction = instructionService.save(instruction);
        assertNotNull("Instruction should not be null", instructionService.findOne(instruction.getId()));
    }

    @Test
    @Override
    public void testSave() throws Exception {
        Instruction instruction = new Instruction();
        instruction.setName("Existing instruction");
        assertNotNull("Instruction should be saved", instructionService.save(instruction));
    }

    @Test
    @Override
    public void testSaveAll() throws Exception {
        List<Instruction> agencyList = new ArrayList<>();
        Instruction instruction = new Instruction();
        instruction.setName("Test Instruction One");
        agencyList.add(instruction);

        instruction = new Instruction();
        instruction.setName("Test Instruction Two");
        agencyList.add(instruction);

        instruction = new Instruction();
        instruction.setName("Test Instruction Three");
        agencyList.add(instruction);

        instructionService.save(agencyList);

        assertEquals("Should return 3", instructionService.count(), 3L);
    }

    @Test(expected = ResourceNotFoundException.class)
    @Override
    public void testDelete() throws Exception {
        Instruction instruction = new Instruction();
        instruction.setName("Existing instruction");
        instruction = instructionService.save(instruction);
        instructionService.delete(instruction.getId());

        assertNull("Should return null", instructionService.findOne(instruction.getId()));
    }

    @Test(expected = ResourceNotFoundException.class)
    @Override
    public void testDeleteAll() throws Exception {
        List<Instruction> agencyList = new ArrayList<>();
        Instruction instruction = new Instruction();
        instruction.setName("Test Instruction One");
        agencyList.add(instruction);

        instruction = new Instruction();
        instruction.setName("Test Instruction Two");
        agencyList.add(instruction);

        instruction = new Instruction();
        instruction.setName("Test Instruction Three");
        agencyList.add(instruction);

        agencyList = instructionService.save(agencyList);
        instructionService.delete(agencyList);

        agencyList.forEach(a -> assertNull("Should return null", instructionService.findOne(a.getId())));

    }

}
