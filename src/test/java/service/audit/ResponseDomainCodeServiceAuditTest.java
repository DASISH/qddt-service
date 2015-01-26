package service.audit;

import no.nsd.qddt.QDDT;
import no.nsd.qddt.domain.Survey;
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
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Dag Østgulen Heradstveit
 */
//@Transactional
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

        responseDomainCode = responseDomainCodeService.save(new ResponseDomainCode(responseDomain.getId(), code.getId(), "1"));

        responseDomainCode.setRank("2");
        responseDomainCode = responseDomainCodeService.save(responseDomainCode);
        responseDomainCode.setRank("3");
        responseDomainCode = responseDomainCodeService.save(responseDomainCode);
        responseDomainCode.setRank("4");
        responseDomainCode = responseDomainCodeService.save(responseDomainCode);
    }

    @Test
    public void getAllRevisions() throws Exception {
        responseDomainCodeService.findAllRevisionsPageable(responseDomainCode, 0, 10);

        // Find all revisions based on the entity id as a page
        Page<Revision<Integer, ResponseDomainCode>> revisions = responseDomainCodeService.findAllRevisionsPageable(
                responseDomainCode, 0, 10);
        assertThat(revisions.getNumberOfElements(), is(4));
    }

    @Test
    public void updateRefrenceTest() throws Exception {
        Revision<Integer, ResponseDomainCode> revision = responseDomainCodeService.findLastChange(responseDomainCode.getId());

        System.out.println(revision.getEntity());

        assertThat(revision.getEntity(), is(responseDomainCode));
        assertThat(revision.getEntity().getRank(), is("4"));
    }
}
