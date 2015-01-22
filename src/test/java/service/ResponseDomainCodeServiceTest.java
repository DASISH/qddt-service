package service;

import no.nsd.qddt.QDDT;
import no.nsd.qddt.domain.response.ResponseDomain;
import no.nsd.qddt.domain.response.ResponseDomainCode;
import no.nsd.qddt.domain.response.ResponseDomainCodeId;
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

    @Test
    public void test() throws Exception {
        List<ResponseDomainCode> rdc = responseDomainCodeService.findAll();

        rdc.forEach((r) -> {
            System.out.println("CODE: " + r.getPk().getCode());
            System.out.println("RESPONSE: " + r.getPk().getResponseDomain());
        });
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