package no.nsd.qddt.domain.responsedomaincode;

import no.nsd.qddt.domain.AbstractServiceTest;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class ResponseDomainCategoryServiceTest extends AbstractServiceTest {

    @Autowired
    private ResponseDomainCodeService responseDomainCodeService;

    @Autowired
    private ResponseDomainCodeRepository responseDomainCodeRepository;

    @Before
    public void setup() {
        super.setBaseRepositories(responseDomainCodeRepository);
    }

    @Test
    @Override
    public void testCount() throws Exception {
        ResponseDomainCode responseDomainCode = new ResponseDomainCode();
        responseDomainCode.setName("Test ResponseDomainCode One");
        responseDomainCodeService.save(responseDomainCode);

        responseDomainCode = new ResponseDomainCode();
        responseDomainCode.setName("Test ResponseDomainCode Two");
        responseDomainCodeService.save(responseDomainCode);

        responseDomainCode = new ResponseDomainCode();
        responseDomainCode.setName("Test ResponseDomainCode Three");
        responseDomainCodeService.save(responseDomainCode);

        assertThat("Should be three", responseDomainCodeService.count(), is(3L));
    }

    @Test
    @Override
    public void testExists() throws Exception {
        ResponseDomainCode responseDomainCode = new ResponseDomainCode();
        responseDomainCode.setName("Existing responseDomainCode");
        responseDomainCode = responseDomainCodeService.save(responseDomainCode);
        assertTrue("ResponseDomainCode should exist", responseDomainCodeService.exists(responseDomainCode.getId()));
    }

    @Test
    @Override
    public void testFindOne() throws Exception {
        ResponseDomainCode responseDomainCode = new ResponseDomainCode();
        responseDomainCode.setName("Existing responseDomainCode");
        responseDomainCode = responseDomainCodeService.save(responseDomainCode);
        assertNotNull("ResponseDomainCode should not be null", responseDomainCodeService.findOne(responseDomainCode.getId()));
    }

    @Test
    @Override
    public void testSave() throws Exception {
        ResponseDomainCode responseDomainCode = new ResponseDomainCode();
        responseDomainCode.setName("Existing responseDomainCode");
        assertNotNull("ResponseDomainCode should be saved", responseDomainCodeService.save(responseDomainCode));
    }

    @Test
    @Override
    public void testSaveAll() throws Exception {
        List<ResponseDomainCode> agencyList = new ArrayList<>();
        ResponseDomainCode responseDomainCode = new ResponseDomainCode();
        responseDomainCode.setName("Test ResponseDomainCode One");
        agencyList.add(responseDomainCode);

        responseDomainCode = new ResponseDomainCode();
        responseDomainCode.setName("Test ResponseDomainCode Two");
        agencyList.add(responseDomainCode);

        responseDomainCode = new ResponseDomainCode();
        responseDomainCode.setName("Test ResponseDomainCode Three");
        agencyList.add(responseDomainCode);

        responseDomainCodeService.save(agencyList);

        assertEquals("Should return 3", responseDomainCodeService.count(), 3L);
    }

    @Test //(expected = ResourceNotFoundException.class)
    @Override
    public void testDelete() throws Exception {
        ResponseDomainCode responseDomainCode = new ResponseDomainCode();
        responseDomainCode.setName("Existing responseDomainCode");
        responseDomainCode = responseDomainCodeService.save(responseDomainCode);
        responseDomainCodeService.delete(responseDomainCode.getId());

        assertNull("Should return null", responseDomainCodeService.findOne(responseDomainCode.getId()));
    }

    @Test //(expected = ResourceNotFoundException.class)
    @Override
    public void testDeleteAll() throws Exception {
        List<ResponseDomainCode> agencyList = new ArrayList<>();
        ResponseDomainCode responseDomainCode = new ResponseDomainCode();
        responseDomainCode.setName("Test ResponseDomainCode One");
        agencyList.add(responseDomainCode);

        responseDomainCode = new ResponseDomainCode();
        responseDomainCode.setName("Test ResponseDomainCode Two");
        agencyList.add(responseDomainCode);

        responseDomainCode = new ResponseDomainCode();
        responseDomainCode.setName("Test ResponseDomainCode Three");
        agencyList.add(responseDomainCode);

        agencyList = responseDomainCodeService.save(agencyList);
        responseDomainCodeService.delete(agencyList);

        agencyList.forEach(a -> assertNull("Should return null", responseDomainCodeService.findOne(a.getId())));
    }
}
