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
import org.springframework.data.history.Revision;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
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
    private ResponseDomain responseDomain;
    private Code code;


    @Before
    @Transactional
    public void setUp() {
        code = new Code();
        code.setCategory("Test class code");
        code.setCodeValue("500");
        code = codeService.save(code);

        responseDomain = new ResponseDomain();
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
    public void getAllRevisionsTest() throws Exception {
        Page<Revision<Integer, ResponseDomainCode>> revisions =
                responseDomainCodeService.findAllRevisionsPageable(responseDomainCode, 0, 10);

        assertThat(revisions.getNumberOfElements(), is(4));
    }

    @Test
    public void getLastRevisionTest() throws Exception {
        Revision<Integer, ResponseDomainCode> revision = responseDomainCodeService.findLastChange(responseDomainCode.getId());

        System.out.println(revision.getRevisionNumber());

        assertThat(revision.getEntity(), is(responseDomainCode));
        assertThat(revision.getEntity().getRank(), is("4"));
    }
}
