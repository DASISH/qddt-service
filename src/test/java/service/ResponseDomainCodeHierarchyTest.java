package service;

import no.nsd.qddt.QDDT;
import no.nsd.qddt.domain.User;
import no.nsd.qddt.domain.response.Code;
import no.nsd.qddt.domain.response.ResponseDomain;
import no.nsd.qddt.domain.response.ResponseDomainCode;
import no.nsd.qddt.service.CodeService;
import no.nsd.qddt.service.ResponseDomainCodeService;
import no.nsd.qddt.service.ResponseDomainService;
import no.nsd.qddt.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Stig Norland
 */
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

    private  Code code;

    @Before
    public void setUp() {


        code = codeService.save(new Code("Opel","0","#BILER"));
        codeService.save(new Code("KVINNE","0","#KJØNN"));
        codeService.save(new Code("MANN", "1", "#KJØNN"));
        codeService.save(new Code("TVEKJØNNET","2","#KJØNN"));

        responseDomain = new ResponseDomain();
        responseDomain.setName("response domain Kjønn");
        responseDomain = responseDomainService.save(responseDomain);

    }

    @Test
    public void saveCodeAndResponseDomainToResponseDomainCodeTest() throws Exception {

        codeService.findByHashTag("jØnn").forEach(System.out::println);
        int i=0;
        for(Code code: codeService.findByHashTag("#KjøNN"))
        {
            ResponseDomainCode responseDomainCode = new ResponseDomainCode();
            responseDomainCode.setRank(i++);
            responseDomainCode.setCode(code);
            responseDomainCode.setResponseDomain(responseDomain);
            responseDomainCodeService.save(responseDomainCode);
        }

        assertEquals(responseDomainCodeService.findByResponseDomainId(responseDomain.getId()).size(),3);
        responseDomainCodeService.findByResponseDomainId(responseDomain.getId()).forEach(System.out::println);
        System.out.println("----");

        assertEquals(responseDomainCodeService.findByCodeId(code.getId()).size(),0);
        responseDomainCodeService.findByCodeId(code.getId()).forEach(System.out::println);


//        // Fetch a fresh entity
//        ResponseDomainCode rdc = responseDomainCodeService.findByPk(new ResponseDomainCodeId(responseDomain, lastCode));
//
//        // Check for everything that should be in the OM.
//        assertNotNull(rdc.getResponseDomain());
//        assertNotNull(rdc.getCode());
//        assertNotNull(rdc.getPk());
//        assertEquals(rdc.getRank(), 2);
    }
}
