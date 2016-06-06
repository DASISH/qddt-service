package no.nsd.qddt.domain.responsedomain;

import no.nsd.qddt.domain.AbstractServiceTest;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

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

    @Test
    @Override
    public void testSaveAll() throws Exception {
        List<ResponseDomain> aList = new ArrayList<>();
        ResponseDomain responseDomain = new ResponseDomain();
        responseDomain.setName("Test ResponseDomain One");
        responseDomain.setResponseKind(ResponseKind.LIST);
        aList.add(responseDomain);

        responseDomain = new ResponseDomain();
        responseDomain.setName("Test ResponseDomain Two");
        responseDomain.setResponseKind(ResponseKind.SCALE);
        aList.add(responseDomain);

        responseDomain = new ResponseDomain();
        responseDomain.setName("Test ResponseDomain Three");
        responseDomain.setResponseKind(ResponseKind.MIXED);
        aList.add(responseDomain);

        responseDomainService.save(aList);

        assertEquals("Should return 3", responseDomainService.count(), 3L);
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

    @Test(expected = ResourceNotFoundException.class)
    @Override
    public void testDeleteAll() throws Exception {
        List<ResponseDomain> agencyList = new ArrayList<>();
        ResponseDomain responseDomain = new ResponseDomain();
        responseDomain.setName("Test ResponseDomain One");
        agencyList.add(responseDomain);

        responseDomain = new ResponseDomain();
        responseDomain.setName("Test ResponseDomain Two");
        agencyList.add(responseDomain);

        responseDomain = new ResponseDomain();
        responseDomain.setName("Test ResponseDomain Three");
        agencyList.add(responseDomain);

        agencyList = responseDomainService.save(agencyList);
        responseDomainService.delete(agencyList);

        agencyList.forEach(a -> assertNull("Should return null", responseDomainService.findOne(a.getId())));

    }

    @Test(expected = ResourceNotFoundException.class)

    public void testfindby() throws Exception {
        List<ResponseDomain> agencyList = new ArrayList<>();
        ResponseDomain responseDomain = new ResponseDomain();
        responseDomain.setName("Test ResponseDomain One");
        responseDomain.setResponseKind(ResponseKind.LIST);
        agencyList.add(responseDomain);

        responseDomain = new ResponseDomain();
        responseDomain.setName("Test ResponseDomain Two");
        responseDomain.setResponseKind(ResponseKind.SCALE);
        agencyList.add(responseDomain);

        responseDomain = new ResponseDomain();
        responseDomain.setName("Test ResponseDomain Three");
        responseDomain.setResponseKind(ResponseKind.MIXED);
        agencyList.add(responseDomain);

        responseDomainService.save(agencyList);

        Page<ResponseDomain> result = responseDomainService.findBy(ResponseKind.LIST,"%","",new PageRequest(0, 20));
        result.forEach(a -> System.out.println(a));
    }
}
