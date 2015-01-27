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

import java.util.Optional;

import static org.junit.Assert.assertEquals;

/**
 * @author Dag Østgulen Heradstveit
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QDDT.class)
public class ResponseDomainCodeServiceTest {

    @Autowired
    private ResponseDomainCodeService responseDomainCodeService;

    @Autowired
    private ResponseDomainService responseDomainService;

    @Autowired
    private CodeService codeService;

    private ResponseDomain responseDomain;

    private ResponseDomainCode responseDomainCode;

    @Before
    public void setUp() {
        Code code = new Code();
        code.setCategory("Test class code");
        code.setCodeValue("500");
        code = codeService.save(code);

        responseDomain = new ResponseDomain();
        responseDomain.setName("This is a response domain");
        responseDomain = responseDomainService.save(responseDomain);

        responseDomainCode = new ResponseDomainCode();
        responseDomainCode.setRank(1);
        responseDomainCode.setCode(code);
        responseDomainCode.setResponseDomain(responseDomain);
        responseDomainCodeService.save(responseDomainCode);
    }

    @Test
    public void findByResponseDomain() throws Exception {
        ResponseDomainCode rdc = responseDomainCodeService.findByResponseDomainId(responseDomain.getId()).get(0);
        assertEquals("Expected objects to be equal().", responseDomainCode, rdc);
        assertEquals("Excepted rank to be 1", responseDomainCode.getRank(), 1);
    }
}