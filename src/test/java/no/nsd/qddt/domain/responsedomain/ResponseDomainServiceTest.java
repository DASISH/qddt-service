package no.nsd.qddt.domain.responsedomain;

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
public class ResponseDomainServiceTest  extends AbstractServiceTest {

    @Autowired
    private ResponseDomainService responseDomainService;

    @Autowired
    private ResponseDomainRepository responseDomainRepository;

    @Before
    public void setup() {
        super.setup();
        super.setBaseRepositories(responseDomainRepository);
    }

    @Test
    @Override
    public void testCount() throws Exception {
        ResponseDomain responseDomain = new ResponseDomain();
        responseDomain.setName("Test ResponseDomain One");
        responseDomainService.save(responseDomain);

        responseDomain = new ResponseDomain();
        responseDomain.setName("Test ResponseDomain Two");
        responseDomainService.save(responseDomain);

        responseDomain = new ResponseDomain();
        responseDomain.setName("Test ResponseDomain Three");
        responseDomainService.save(responseDomain);

        assertThat("Should be three", responseDomainService.count(), is(3L));
    }

    @Test
    @Override
    public void testExists() throws Exception {
        ResponseDomain responseDomain = new ResponseDomain();
        responseDomain.setName("Existing responseDomain");
        responseDomain = responseDomainService.save(responseDomain);
        assertTrue("ResponseDomain should exist", responseDomainService.exists(responseDomain.getId()));
    }

    @Test
    @Override
    public void testFindOne() throws Exception {
        ResponseDomain responseDomain = new ResponseDomain();
        responseDomain.setName("Existing responseDomain");
        responseDomain = responseDomainService.save(responseDomain);
        assertNotNull("ResponseDomain should not be null", responseDomainService.findOne(responseDomain.getId()));
    }

    @Test
    @Override
    public void testSave() throws Exception {
        ResponseDomain responseDomain = new ResponseDomain();
        responseDomain.setName("Existing responseDomain");
        assertNotNull("ResponseDomain should be saved", responseDomainService.save(responseDomain));
    }



    @Test(expected = ResourceNotFoundException.class)
    @Override
    public void testDelete() throws Exception {
        ResponseDomain responseDomain = new ResponseDomain();
        responseDomain.setName("Existing responseDomain");
        responseDomain = responseDomainService.save(responseDomain);
        responseDomainService.delete(responseDomain.getId());

        assertNull("Should return null", responseDomainService.findOne(responseDomain.getId()));
    }

}
