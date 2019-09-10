package no.nsd.qddt.domain.responsedomain.audit;

import no.nsd.qddt.domain.AbstractAuditServiceTest;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import no.nsd.qddt.domain.responsedomain.ResponseDomainService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
    public void testSaveSurveyWithAudit() {
        entity = responseDomainService.findOne(entity.getId());

        // Find all revisions based on the entity id as a page
        var revisions = responseDomainAuditService.findRevisions(entity.getId(), PageRequest.of(0, 10));

        var wrapper =  Revisions.of( revisions.getContent());

        assertEquals(wrapper.getLatestRevision().getEntity().hashCode(), entity.hashCode());
        assertThat(revisions.getNumberOfElements(), is(4));
    }

    @Test
    public void getAllRevisionsTest() {
        var revisions = responseDomainAuditService.findRevisions(entity.getId(), PageRequest.of(0, 20));
        assertEquals("Excepted four revisions.", revisions.getNumberOfElements(), 4);
    }

    @Test
    public void getLastRevisionTest() {
        var revision = responseDomainAuditService.findLastChange(entity.getId());
        assertEquals("Excepted initial ResponseDomain Object.", revision.getEntity().hashCode(), entity.hashCode());
        assertEquals("Expected Name to be 'Third'", revision.getEntity().getName(), "Third");
    }
}
