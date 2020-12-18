package no.nsd.qddt.domain.responsedomain.audit;


import no.nsd.qddt.domain.AbstractAuditServiceTest;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import no.nsd.qddt.domain.responsedomain.ResponseDomainService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        // Find the last revision based on the entity id
        Revision<Integer, ResponseDomain> revision = responseDomainAuditService.findLastChange(entity.getId());

        // Find all revisions based on the entity id as a page
        Page<Revision<Integer, ResponseDomain>> revisions = responseDomainAuditService.findRevisions(
                entity.getId(), PageRequest.of(0, 10));

        Revisions<Integer, ResponseDomain> wrapper = Revisions.of(revisions.getContent());

        assertEquals(wrapper.getLatestRevision().getEntity().hashCode(), entity.hashCode());
//        assertThat(revisions.getNumberOfElements(), is(4));
    }

    @Test
    public void getAllRevisionsTest() {
        Page<Revision<Integer, ResponseDomain>> revisions =
                responseDomainAuditService.findRevisions(entity.getId(), PageRequest.of(0, 20));

        assertEquals(4, revisions.getNumberOfElements(),"Excepted four revisions.");
    }

    @Test
    public void getLastRevisionTest() {
        Revision<Integer, ResponseDomain> revision = responseDomainAuditService.findLastChange(entity.getId());

        assertEquals(revision.getEntity().hashCode(), entity.hashCode(),"Excepted initial ResponseDomain Object.");

        assertEquals( revision.getEntity().getName(), "Third","Expected Name to be 'Third'");
    }
}
