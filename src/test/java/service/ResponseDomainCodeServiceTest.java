package service;

import no.nsd.qddt.QDDT;
import no.nsd.qddt.domain.response.Code;
import no.nsd.qddt.domain.response.ResponseDomain;
import no.nsd.qddt.domain.response.ResponseDomainCode;
import no.nsd.qddt.domain.response.ResponseDomainCodeId;
import no.nsd.qddt.service.CodeService;
import no.nsd.qddt.service.ResponseDomainCodeService;
import no.nsd.qddt.service.ResponseDomainService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.history.Revision;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Test
    public void saveCodeAndResponseDomainToResponseDomainCodeTest() throws Exception {
        Code code = new Code();
        code.setCategory("Test class code");
        code.setCodeValue("500");
        code = codeService.save(code);

        ResponseDomain responseDomain = new ResponseDomain();
        responseDomain.setName("This is a response domain");
        responseDomain = responseDomainService.save(responseDomain);

        ResponseDomainCode responseDomainCode = new ResponseDomainCode();
        responseDomainCode.setRank("FIRST");
        responseDomainCode.setCode(code);
        responseDomainCode.setResponseDomain(responseDomain);
        responseDomainCodeService.save(responseDomainCode);

        // Get the data from the database without
        responseDomainCode = responseDomainCodeService.findByPk(new ResponseDomainCodeId(responseDomain, code));

    }

    @Test
    public void getResponseDomainCodeFromPkTest() throws Exception {

    }

    @Test
    public void updateRefrenceTest() throws Exception {
        ResponseDomainCode rdc = responseDomainCodeService.findAll().get(0);

        rdc.setRank("Second rank");
        rdc = responseDomainCodeService.save(rdc);
        rdc.setRank("Third rank");
        rdc = responseDomainCodeService.save(rdc);
        rdc.setRank("Fourth rank");
        rdc = responseDomainCodeService.save(rdc);

        Revision<Integer, ResponseDomainCode> revision = responseDomainCodeService.findLastChange(rdc.getPk());

        System.out.println(revision);
    }

    @Test
    public void findAllResponseDomainCodeRevisionsFromResponseDomainTest() throws Exception {
//        List<ResponseDomain> responseDomain = responseDomainService.findAll();

        ResponseDomainCode rdc = responseDomainCodeService.findAll().get(0);

//        ResponseDomainCode rdc2 = responseDomainCodeService.findL(rdc.getPk());

//        System.out.println(rdc2);

    }


}