package service;

import no.nsd.qddt.QDDT;
import no.nsd.qddt.domain.response.ResponseDomainCode;
import no.nsd.qddt.service.ResponseDomainService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author Dag Østgulen Heradstveit
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QDDT.class)
public class ResponseDomainServiceTest {

    @Autowired
    private ResponseDomainService responseDomainService;

    @Test
    public void test() throws Exception {
        List<ResponseDomainCode> rdc = responseDomainService.findAll();

        rdc.forEach((r) -> {
            System.out.println(r.getPk().getCode());
            System.out.println(r.getPk().getResponseDomain());
        });
    }

    @Test
    public void updateRefrenceTest() throws Exception {
        ResponseDomainCode rdc = responseDomainService.findAll().get(0);

        //Skriv test som oppdatere en eksisterende relasjon med rank.
        System.out.println(rdc);
    }
}