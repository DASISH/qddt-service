package domain.responsedomain.audit;

import no.nsd.qddt.QDDT;
import no.nsd.qddt.domain.code.Code;
import no.nsd.qddt.domain.code.CodeService;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import no.nsd.qddt.domain.responsedomain.ResponseDomainService;
import no.nsd.qddt.domain.responsedomain.ResponseKind;
import no.nsd.qddt.domain.responsedomaincode.ResponseDomainCode;
import no.nsd.qddt.domain.responsedomaincode.ResponseDomainCodeService;
import no.nsd.qddt.domain.responsedomaincode.audit.ResponseDomainCodeAuditService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Test is provided to make sure that we still get the relevant
 * data after moving from @EmbeddedId and @Embeddable composite
 * keys to surrogate keys to provide Envers support.
 *
 * @author Dag Østgulen Heradstveit
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QDDT.class)
public class ResponseDomainCodeAuditServiceTest {

    @Autowired
    private ResponseDomainCodeService responseDomainCodeService;

    @Autowired
    private ResponseDomainService responseDomainService;

    @Autowired
    private ResponseDomainCodeAuditService responseDomainCodeAuditService;


    @Autowired
    private CodeService codeService;

    private ResponseDomainCode responseDomainCode;

    @Before
    public void setUp() {
        Code code = new Code();
        code.setCategory("Test class code");
        code.setName("DaCode");
        code = codeService.save(code);

        ResponseDomain responseDomain = new ResponseDomain();
        responseDomain.setName("This is a response domain");
        responseDomain.setResponseKind(ResponseKind.Code);
        responseDomain = responseDomainService.save(responseDomain);

        responseDomainCode = responseDomainCodeService.save(new ResponseDomainCode(1, responseDomain, code));

        responseDomainCode.setCodeIdx(2);
        responseDomainCode = responseDomainCodeService.save(responseDomainCode);
        responseDomainCode.setCodeIdx(10);
        responseDomainCode = responseDomainCodeService.save(responseDomainCode);
        responseDomainCode.setCodeIdx(4);
        responseDomainCode = responseDomainCodeService.save(responseDomainCode);
    }

    @Test
    public void testSaveSurveyWithAudit() throws Exception {
        responseDomainCode = responseDomainCodeService.findOne(responseDomainCode.getId());

        // Find the last revision based on the entity id
        Revision<Integer, ResponseDomainCode> revision = responseDomainCodeAuditService.findLastChange(responseDomainCode.getId());

        // Find all revisions based on the entity id as a page
        Page<Revision<Integer, ResponseDomainCode>> revisions = responseDomainCodeAuditService.findRevisions(
                responseDomainCode.getId(), new PageRequest(0, 10));

        Revisions<Integer, ResponseDomainCode> wrapper = new Revisions<>(revisions.getContent());

        assertEquals(wrapper.getLatestRevision().getEntity(), responseDomainCode);
        assertThat(revisions.getNumberOfElements(), is(4));
    }

    @Test
    public void getAllRevisionsTest() throws Exception {
        Page<Revision<Integer, ResponseDomainCode>> revisions =
                responseDomainCodeAuditService.findRevisions(responseDomainCode.getId(), new PageRequest(0, 20));

                assertEquals("Excepted four revisions.",
                        revisions.getNumberOfElements(), 4);
    }

    @Test
    public void getLastRevisionTest() throws Exception {
        Revision<Integer, ResponseDomainCode> revision = responseDomainCodeAuditService.findLastChange(responseDomainCode.getId());

        assertEquals("Excepted initial ResponseDomain Object.",
                revision.getEntity(), responseDomainCode);
        assertEquals("Expected rank to be 4.", revision.getEntity().getCodeIdx(), 4);
    }
}
