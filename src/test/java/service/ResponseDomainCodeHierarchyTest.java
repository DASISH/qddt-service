package service;

import no.nsd.qddt.QDDT;
import no.nsd.qddt.domain.User;
import no.nsd.qddt.domain.response.Code;
import no.nsd.qddt.domain.response.ResponseDomain;
import no.nsd.qddt.domain.response.ResponseDomainCode;
import no.nsd.qddt.domain.response.ResponseDomainCodeId;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Stig Norland
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QDDT.class)
public class ResponseDomainCodeHierarchyTest {


    @Autowired
    private ResponseDomainCodeService responseDomainCodeService;

    @Autowired
    private ResponseDomainService responseDomainService;

    @Autowired
    private CodeService codeService;

    private ResponseDomain responseDomain;

    @Before
    public void setUp() {

        User user = new User("test@nsd.uib.no","password","Test User");

        codeService.save(new Code("KVINNE","0",user));
        codeService.save(new Code("MANN", "1", user));
        codeService.save(new Code("TVEKJØNNET","2",user));


        responseDomain = new ResponseDomain();
        responseDomain.setName("response domain Kjønn");
        responseDomain = responseDomainService.save(responseDomain);
    }

    @Test
    public void saveCodeAndResponseDomainToResponseDomainCodeTest() throws Exception {

        Code lastCode = null;
        int i=0;
        for(Code code: codeService.findAll())
        {
            ResponseDomainCode responseDomainCode = new ResponseDomainCode();
            responseDomainCode.setRank(i++);
            responseDomainCode.setCode(code);
            responseDomainCode.setResponseDomain(responseDomain);
            responseDomainCodeService.save(responseDomainCode);
            lastCode = code;
        }

        assertNotNull(lastCode);
        // Fetch a fresh entity
        ResponseDomainCode rdc = responseDomainCodeService.findByPk(new ResponseDomainCodeId(responseDomain, lastCode));

        // Check for everything that should be in the OM.
        assertNotNull(rdc.getResponseDomain());
        assertNotNull(rdc.getCode());
        assertNotNull(rdc.getPk());
        assertEquals(rdc.getRank(), 2);
    }
}
