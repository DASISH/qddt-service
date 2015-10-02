package service;

import no.nsd.qddt.QDDT;
import no.nsd.qddt.domain.code.Code;
import no.nsd.qddt.domain.code.CodeService;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import no.nsd.qddt.domain.responsedomain.ResponseDomainService;
import no.nsd.qddt.domain.responsedomaincode.ResponseDomainCode;
import no.nsd.qddt.domain.responsedomaincode.ResponseDomainCodeService;
import no.nsd.qddt.utils.builders.CodeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

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

    private Code code;

    private static final String HASH_TAG_SEX = "#KJØNN";
    private static final String HASH_TAG_CAR = "#BIL";

    @Before
    public void setUp() {


<<<<<<< HEAD
        code = codeService.save(new CodeBuilder().setCategory("Opel").setTag("#BILER").createCode());
        codeService.save(new CodeBuilder().setCategory("KVINNE").setTag("#KJØNN").createCode());
        codeService.save(new CodeBuilder().setCategory("MANN").setTag("#KJØNN").createCode());
        codeService.save(new CodeBuilder().setCategory("TVEKJØNNET").setTag("#KJØNN").createCode());
=======
        code = codeService.save(new CodeBuilder().setCategory("Opel").setValue("0").setTag(HASH_TAG_SEX).createCode());
        codeService.save(new CodeBuilder().setCategory("KVINNE").setValue("0").setTag(HASH_TAG_SEX).createCode());
        codeService.save(new CodeBuilder().setCategory("MANN").setValue("1").setTag(HASH_TAG_SEX).createCode());
        codeService.save(new CodeBuilder().setCategory("TVEKJØNNET").setValue("2").setTag(HASH_TAG_CAR).createCode());
>>>>>>> 51123bf444acc8f829c291d0acb8d07cd5c7d7cb

        responseDomain = new ResponseDomain();
        responseDomain.setName("response domain Kjønn");
        responseDomain = responseDomainService.save(responseDomain);

    }

    @Test
    public void saveCodeAndResponseDomainToResponseDomainCodeTest() throws Exception {

        codeService.findByHashTag(HASH_TAG_SEX).forEach(System.out::println);
        int i = 0;
        for (Code code : codeService.findByHashTag(HASH_TAG_SEX)) {
            ResponseDomainCode responseDomainCode = new ResponseDomainCode();
            responseDomainCode.setCodeIdx(i++);
            responseDomainCode.setCode(code);
            responseDomainCode.setResponseDomain(responseDomain);
            responseDomainCodeService.save(responseDomainCode);
        }

        assertEquals(responseDomainCodeService.findByResponseDomainId(responseDomain.getId()).size(), 3);
        responseDomainCodeService.findByResponseDomainId(responseDomain.getId()).forEach(System.out::println);

        // responseDomainCode doesn't have this code attached
        assertEquals(responseDomainCodeService.findByCodeId(code.getId()).size(), 0);
        responseDomainCodeService.findByCodeId(code.getId()).forEach(System.out::println);

    }
}
