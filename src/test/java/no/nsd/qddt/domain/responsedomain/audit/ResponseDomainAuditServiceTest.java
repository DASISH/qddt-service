package no.nsd.qddt.domain.responsedomain.audit;

import no.nsd.qddt.domain.AbstractAuditServiceTest;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import no.nsd.qddt.domain.responsedomain.ResponseDomainService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ResponseDomainAuditServiceTest extends AbstractAuditServiceTest {

    @Autowired
    private ResponseDomainService responseDomainService;

    @Autowired
    private ResponseDomainAuditService responseDomainAuditService;

    private ResponseDomain entity;

    @Before
    public void setUp() {


        entity = responseDomainService.save(new ResponseDomain());

        entity.setName("First");
        entity = responseDomainService.save(entity);
        entity.setName("Second");
        entity = responseDomainService.save(entity);
        entity.setName("Third");
        entity = responseDomainService.save(entity);
    }

    @Test
    public void testSaveSurveyWithAudit() throws Exception {
        entity = responseDomainService.findOne(entity.getId());

        // Find the last revision based on the entity id
        Revision<Integer, ResponseDomain> revision = responseDomainAuditService.findLastChange(entity.getId());

        // Find all revisions based on the entity id as a page
        Page<Revision<Integer, ResponseDomain>> revisions = responseDomainAuditService.findRevisions(
                entity.getId(), new PageRequest(0, 10));

        Revisions<Integer, ResponseDomain> wrapper = new Revisions<>(revisions.getContent());

        assertEquals(wrapper.getLatestRevision().getEntity().hashCode(), entity.hashCode());
        assertThat(revisions.getNumberOfElements(), is(4));
    }

    @Test
    public void getAllRevisionsTest() throws Exception {
        Page<Revision<Integer, ResponseDomain>> revisions =
                responseDomainAuditService.findRevisions(entity.getId(), new PageRequest(0, 20));

        assertEquals("Excepted four revisions.",
                revisions.getNumberOfElements(), 4);
    }

    @Test
    public void getLastRevisionTest() throws Exception {
        Revision<Integer, ResponseDomain> revision = responseDomainAuditService.findLastChange(entity.getId());

        assertEquals("Excepted initial ResponseDomain Object.",
                revision.getEntity().hashCode(), entity.hashCode());
        assertEquals("Expected Name to be 'Third'", revision.getEntity().getName(), "Third");
    }
}
