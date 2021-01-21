package no.nsd.qddt.domain.agency;

import no.nsd.qddt.domain.AbstractServiceTest;
import no.nsd.qddt.domain.classes.exception.ResourceNotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class AgencyServiceTest extends AbstractServiceTest {

    @Autowired
    private AgencyService agencyService;

    @Autowired
    private AgencyRepository agencyRepository;

    @Before
    public void setup() {
        super.setup();
        super.setBaseRepositories(agencyRepository);
    }

    @After
    @Override
    public void tearDown() {
        List<Agency> agencyList = agencyRepository.findAll();
        for (Agency ag :agencyList ) {
            if(!ag.getName().equals("NSD-qddt")){
                agencyRepository.delete(ag);
            }
        }
    }

    @Test
    @Override
    public void testCount() throws Exception {
        super.getBeforeSecurityContext();
        Agency agency = new Agency();
        agency.setName("Test Agency One");
        agencyService.save(agency);

        agency = new Agency();
        agency.setName("Test Agency Two");
        agencyService.save(agency);

        agency = new Agency();
        agency.setName("Test Agency Three");
        agencyService.save(agency);

        assertThat("Should be four (3+1)", agencyService.count(), is(4L));
    }

    @Test
    @Override
    public void testExists() throws Exception {
        super.getBeforeSecurityContext();
        Agency agency = new Agency();
        agency.setName("Existing agency");
        agency = agencyService.save(agency);
        assertTrue("Agency should exist", agencyService.exists(agency.getId()));
    }

    @Test
    @Override
    public void testFindOne() throws Exception {
        super.getBeforeSecurityContext();
        Agency agency = new Agency();
        agency.setName("Existing agency");
        agency = agencyService.save(agency);
        assertNotNull("Agency should not be null", agencyService.findOne(agency.getId()));
    }

    @Test
    @Override
    public void testSave() throws Exception {
        super.getBeforeSecurityContext();
        Agency agency = new Agency();
        agency.setName("Existing agency");
        assertNotNull("Agency should be saved", agencyService.save(agency));
    }


    @Test(expected = ResourceNotFoundException.class)
    @Override
    public void testDelete() throws Exception {
        super.getBeforeSecurityContext();
        Agency agency = new Agency();
        agency.setName("Existing agency");
        agency = agencyService.save(agency);
        agencyService.delete(agency.getId());

        assertNull("Should return null", agencyService.findOne(agency.getId()));
    }


}
