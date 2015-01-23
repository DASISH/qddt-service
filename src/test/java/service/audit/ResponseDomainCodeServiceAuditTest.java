package service.audit;

import no.nsd.qddt.QDDT;
import no.nsd.qddt.domain.response.ResponseDomainCode;
import no.nsd.qddt.service.ResponseDomainCodeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.history.Revision;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author Dag Østgulen Heradstveit
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QDDT.class)
public class ResponseDomainCodeServiceAuditTest {

    @Autowired
    private ResponseDomainCodeService responseDomainCodeService;

    @Test
    public void getAllRevisions() throws Exception {
        ResponseDomainCode rdc = responseDomainCodeService.findAll().get(0);

        rdc.setRank("Second rank");
        rdc = responseDomainCodeService.save(rdc);
        rdc.setRank("Third rank");
        rdc = responseDomainCodeService.save(rdc);
        rdc.setRank("Fourth rank");
        rdc = responseDomainCodeService.save(rdc);


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

        System.out.println(revision.getEntity());

        assertThat(revision.getEntity(), not(null));
        assertThat(revision.getEntity().getRank(), is("Fourth rank"));
    }
}
