package service;

import no.nsd.qddt.QDDT;
import no.nsd.qddt.domain.response.Code;
import no.nsd.qddt.domain.response.ResponseDomain;
import no.nsd.qddt.domain.response.ResponseDomainCode;
import no.nsd.qddt.service.CodeService;
import no.nsd.qddt.service.ResponseDomainCodeService;
import no.nsd.qddt.service.ResponseDomainService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertNotNull;

/**
 * @author Dag Østgulen Heradstveit
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QDDT.class)
public class ResponseDomainCodeServiceTest {

    @Autowired
    private ResponseDomainCodeService responseDomainCodeService;

    @Autowired
    private ResponseDomainService responseDomainService;

    @Autowired
    private CodeService codeService;

    private Code code;

    private ResponseDomain responseDomain;

    @Before
    public void setUp() {
        code = new Code();
        code.setCategory("Test class code");
        code.setCodeValue("500");
        code = codeService.save(code);

        responseDomain = new ResponseDomain();
        responseDomain.setName("This is a response domain");
        responseDomain = responseDomainService.save(responseDomain);
    }

    @Test
    public void saveCodeAndResponseDomainToResponseDomainCodeTest() throws Exception {
        ResponseDomainCode responseDomainCode = new ResponseDomainCode();
        responseDomainCode.setRank("FIRST");
        responseDomainCode.setCodeId(code.getId());
        responseDomainCode.setResponseDomainId(responseDomain.getId());
        responseDomainCodeService.save(responseDomainCode);

        // Fetch a fresh entity
        ResponseDomainCode rdc = responseDomainCodeService.findByResponseDomainId(responseDomain.getId());

        // Check for everything that should be in the OM.
        assertNotNull(rdc.getResponseDomainId());
        assertNotNull(rdc.getCodeId());
        assertNotNull(rdc.getRank());

        System.out.println(rdc);
    }
}