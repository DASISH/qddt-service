package no.nsd.qddt.domain.controlconstruct;

import no.nsd.qddt.domain.AbstractServiceTest;
import no.nsd.qddt.domain.controlconstruct.pojo.ControlConstruct;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by Dag Østgulen Heradstveit.
 */
public class ControlConstructServiceTest extends AbstractServiceTest {

    @Autowired
    private ControlConstructService controlConstructService;

    @Autowired
    private ControlConstructRepository instrumentRepository;

    @Before
    public void setup() {
        super.setup();
        super.setBaseRepositories(instrumentRepository);
    }

    @Test
    @Override
    public void testCount() throws Exception {
        ControlConstruct controlConstruct = new ControlConstruct();
        controlConstruct.setName("Test InstrumentQuestion One");
        controlConstructService.save(controlConstruct);

        controlConstruct = new ControlConstruct();
        controlConstruct.setName("Test InstrumentQuestion Two");
        controlConstructService.save(controlConstruct);

        controlConstruct = new ControlConstruct();
        controlConstruct.setName("Test InstrumentQuestion Three");
        controlConstructService.save(controlConstruct);

        assertThat("Should be three", controlConstructService.count(), is(3L));
    }

    @Test
    @Override
    public void testExists() throws Exception {
        ControlConstruct controlConstruct = new ControlConstruct();
        controlConstruct.setName("Existing instrumentQuestion");
        controlConstruct = controlConstructService.save(controlConstruct);
        assertTrue("InstrumentQuestion should exist", controlConstructService.exists(controlConstruct.getId()));
    }

    @Test
    @Override
    public void testFindOne() throws Exception {
        ControlConstruct controlConstruct = new ControlConstruct();
        controlConstruct.setName("Existing instrumentQuestion");
        controlConstruct = controlConstructService.save(controlConstruct);
        assertNotNull("InstrumentQuestion should not be null", controlConstructService.findOne(controlConstruct.getId()));
    }

    @Test
    @Override
    public void testSave() throws Exception {
        ControlConstruct controlConstruct = new ControlConstruct();
        controlConstruct.setName("Existing instrumentQuestion");
        assertNotNull("InstrumentQuestion should be saved", controlConstructService.save(controlConstruct));
    }


    @Test(expected = ResourceNotFoundException.class)
    @Override
    public void testDelete() throws Exception {
        ControlConstruct controlConstruct = new ControlConstruct();
        controlConstruct.setName("Existing instrumentQuestion");
        controlConstruct = controlConstructService.save(controlConstruct);
        controlConstructService.delete(controlConstruct.getId());

        assertNull("Should return null", controlConstructService.findOne(controlConstruct.getId()));
    }


}