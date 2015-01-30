package service.audit;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.history.Revision;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;

/**
 * Test is provided to make sure that we still get the relevant
 * data after moving from @EmbeddedId and @Embeddable composite
 * keys to surrogate keys to provide Envers support.
 *
 * @author Dag Østgulen Heradstveit
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QDDT.class)
public class ResponseDomainCodeServiceAuditTest {

    @Autowired
    private ResponseDomainCodeService responseDomainCodeService;

    @Autowired
    private ResponseDomainService responseDomainService;

    @Autowired
    private CodeService codeService;

    private ResponseDomainCode responseDomainCode;

    @Before
    @Transactional
    public void setUp() {
        Code code = new Code();
        code.setCategory("Test class code");
        code.setCodeValue("500");
        code = codeService.save(code);

        ResponseDomain responseDomain = new ResponseDomain();
        responseDomain.setName("This is a response domain");
        responseDomain = responseDomainService.save(responseDomain);

        responseDomainCode = responseDomainCodeService.save(new ResponseDomainCode(1, responseDomain, code));

        responseDomainCode.setRank(2);
        responseDomainCode = responseDomainCodeService.save(responseDomainCode);
        responseDomainCode.setRank(3);
        responseDomainCode = responseDomainCodeService.save(responseDomainCode);
        responseDomainCode.setRank(4);
        responseDomainCode = responseDomainCodeService.save(responseDomainCode);
    }

    @Test
    public void getAllRevisionsTest() throws Exception {
        Page<Revision<Integer, ResponseDomainCode>> revisions =
                responseDomainCodeService.findAllRevisionsPageable(responseDomainCode.getId(), new PageRequest(0,20 ));

                assertEquals("Excepted four revisions.",
                revisions.getNumberOfElements(), 4);
    }

    @Test
    public void getLastRevisionTest() throws Exception {
        Revision<Integer, ResponseDomainCode> revision = responseDomainCodeService.findLastChange(responseDomainCode.getId());

        System.out.println(revision.getRevisionNumber());

        assertEquals("Excepted initial ResponseDomain Object.",
                revision.getEntity(), responseDomainCode);
        assertEquals("Expected rank to be 4.", revision.getEntity().getRank(), 4);
    }
}
