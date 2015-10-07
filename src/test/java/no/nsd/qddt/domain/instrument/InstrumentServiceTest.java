package no.nsd.qddt.domain.instrument;

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
public class InstrumentServiceTest extends AbstractServiceTest {

    @Autowired
    private InstrumentService instrumentService;

    @Autowired
    private InstrumentRepository instrumentRepository;

    @Before
    public void setup() {
        super.setBaseRepositories(instrumentRepository);
    }

    @Test
    @Override
    public void testCount() throws Exception {
        Instrument instrument = new Instrument();
        instrument.setName("Test Instrument One");
        instrumentService.save(instrument);

        instrument = new Instrument();
        instrument.setName("Test Instrument Two");
        instrumentService.save(instrument);

        instrument = new Instrument();
        instrument.setName("Test Instrument Three");
        instrumentService.save(instrument);

        assertThat("Should be three", instrumentService.count(), is(3L));
    }

    @Test
    @Override
    public void testExists() throws Exception {
        Instrument instrument = new Instrument();
        instrument.setName("Existing instrument");
        instrument = instrumentService.save(instrument);
        assertTrue("Instrument should exist", instrumentService.exists(instrument.getId()));
    }

    @Test
    @Override
    public void testFindOne() throws Exception {
        Instrument instrument = new Instrument();
        instrument.setName("Existing instrument");
        instrument = instrumentService.save(instrument);
        assertNotNull("Instrument should not be null", instrumentService.findOne(instrument.getId()));
    }

    @Test
    @Override
    public void testSave() throws Exception {
        Instrument instrument = new Instrument();
        instrument.setName("Existing instrument");
        assertNotNull("Instrument should be saved", instrumentService.save(instrument));
    }

    @Test
    @Override
    public void testSaveAll() throws Exception {
        List<Instrument> agencyList = new ArrayList<>();
        Instrument instrument = new Instrument();
        instrument.setName("Test Instrument One");
        agencyList.add(instrument);

        instrument = new Instrument();
        instrument.setName("Test Instrument Two");
        agencyList.add(instrument);

        instrument = new Instrument();
        instrument.setName("Test Instrument Three");
        agencyList.add(instrument);

        instrumentService.save(agencyList);

        assertEquals("Should return 3", instrumentService.count(), 3L);
    }

    @Test(expected = ResourceNotFoundException.class)
    @Override
    public void testDelete() throws Exception {
        Instrument instrument = new Instrument();
        instrument.setName("Existing instrument");
        instrument = instrumentService.save(instrument);
        instrumentService.delete(instrument.getId());

        assertNull("Should return null", instrumentService.findOne(instrument.getId()));
    }

    @Test(expected = ResourceNotFoundException.class)
    @Override
    public void testDeleteAll() throws Exception {
        List<Instrument> agencyList = new ArrayList<>();
        Instrument instrument = new Instrument();
        instrument.setName("Test Instrument One");
        agencyList.add(instrument);

        instrument = new Instrument();
        instrument.setName("Test Instrument Two");
        agencyList.add(instrument);

        instrument = new Instrument();
        instrument.setName("Test Instrument Three");
        agencyList.add(instrument);

        agencyList = instrumentService.save(agencyList);
        instrumentService.delete(agencyList);

        agencyList.forEach(a -> assertNull("Should return null", instrumentService.findOne(a.getId())));

    }
}