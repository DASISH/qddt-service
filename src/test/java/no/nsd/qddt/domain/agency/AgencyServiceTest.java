package no.nsd.qddt.domain.agency;

import no.nsd.qddt.domain.AbstractServiceTest;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;

import java.util.ArrayList;
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

    @Test
    @Override
    public void testSaveAll() throws Exception {
        super.getBeforeSecurityContext();
        List<Agency> agencyList = new ArrayList<>();
        Agency agency = new Agency();
        agency.setName("Test Agency One");
        agencyList.add(agency);

        agency = new Agency();
        agency.setName("Test Agency Two");
        agencyList.add(agency);

        agency = new Agency();
        agency.setName("Test Agency Three");
        agencyList.add(agency);

        agencyService.save(agencyList);

        assertEquals("Should have saved (3+1) agencies", agencyService.count(), 4L);
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

    @Test(expected = ResourceNotFoundException.class)
    @Override
    public void testDeleteAll() throws Exception {
        super.getBeforeSecurityContext();
        List<Agency> agencyList = new ArrayList<>();
        Agency agency = new Agency();
        agency.setName("Test Agency One");
        agencyList.add(agency);

        agency = new Agency();
        agency.setName("Test Agency Two");
        agencyList.add(agency);

        agency = new Agency();
        agency.setName("Test Agency Three");
        agencyList.add(agency);

        agencyList = agencyService.save(agencyList);
        agencyService.delete(agencyList);

        agencyList.forEach(a -> assertNull("Should return null", agencyService.findOne(a.getId())));

    }
}