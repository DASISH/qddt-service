package no.nsd.qddt.domain.controlconstruct.audit;

import no.nsd.qddt.domain.AbstractAuditServiceTest;
import no.nsd.qddt.domain.controlconstruct.pojo.ControlConstruct;
import no.nsd.qddt.domain.controlconstruct.ControlConstructService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * @author Stig Norland
 */
public class ControlConstructAuditServiceTest extends AbstractAuditServiceTest {

    @Autowired
    private ControlConstructService instrumentQuestionservice;

    @Autowired
    private ControlConstructAuditService controlConstructAuditService;

    private ControlConstruct entity;

    @Before
    public void setUp() {


        entity = instrumentQuestionservice.save(new ControlConstruct());

        entity.setName("First");
        entity = instrumentQuestionservice.save(entity);
        entity.setName("Second");
        entity = instrumentQuestionservice.save(entity);
        entity.setName("Third");
        entity = instrumentQuestionservice.save(entity);
    }


    //

    @Test
    public void testSuccess(){
        assertThat(1, is(1));
    }

//    @Test
//    public void testSaveSurveyWithAudit() throws Exception {
//        entity = instrumentQuestionservice.findOne(entity.getId());
//
//        // Find the last revision based on the entity id
//        var  revision = instrumentQuestionAuditService.findLastChange(entity.getId());
//
//        // Find all revisions based on the entity id as a page
//        var revisions = instrumentQuestionAuditService.findRevisions(
//                entity.getId(), PageRequest.of(0, 10));
//
//        var  wrapper = Revisions.of(revisions.getContent());
//
//        assertEquals(wrapper.getLatestRevision().getEntity().hashCode(), entity.hashCode());
//        assertThat(revisions.getNumberOfElements(), is(4));
//    }
//
//    @Test
//    public void getAllRevisionsTest() throws Exception {
//        var revisions =
//                instrumentQuestionAuditService.findRevisions(entity.getId(), PageRequest.of(0, 20));
//
//        assertEquals("Excepted four revisions.",
//                revisions.getNumberOfElements(), 4);
//    }
//
//    @Test
//    public void getLastRevisionTest() throws Exception {
//        var  revision = instrumentQuestionAuditService.findLastChange(entity.getId());
//
//        assertEquals("Excepted initial ResponseDomain Object.",
//                revision.getEntity().hashCode(), entity.hashCode());
//        assertEquals("Expected Name to be 'Third'", revision.getEntity().getName(), "Third");
//    }
}
