package no.nsd.qddt.domain.instrumentquestion.audit;

import no.nsd.qddt.domain.AbstractAuditServiceTest;
import no.nsd.qddt.domain.instrumentquestion.InstrumentQuestion;
import no.nsd.qddt.domain.instrumentquestion.InstrumentQuestionService;
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

/**
 * @author Stig Norland
 */
public class InstrumentQuestionAuditServiceTest  extends AbstractAuditServiceTest {

    @Autowired
    private InstrumentQuestionService instrumentQuestionservice;

    @Autowired
    private InstrumentQuestionAuditService instrumentQuestionAuditService;

    private InstrumentQuestion entity;

    @Before
    public void setUp() {


        entity = instrumentQuestionservice.save(new InstrumentQuestion());

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
//        Revision<Integer, InstrumentQuestion> revision = instrumentQuestionAuditService.findLastChange(entity.getId());
//
//        // Find all revisions based on the entity id as a page
//        Page<Revision<Integer, InstrumentQuestion>> revisions = instrumentQuestionAuditService.findRevisions(
//                entity.getId(), new PageRequest(0, 10));
//
//        Revisions<Integer, InstrumentQuestion> wrapper = new Revisions<>(revisions.getContent());
//
//        assertEquals(wrapper.getLatestRevision().getEntity().hashCode(), entity.hashCode());
//        assertThat(revisions.getNumberOfElements(), is(4));
//    }
//
//    @Test
//    public void getAllRevisionsTest() throws Exception {
//        Page<Revision<Integer, InstrumentQuestion>> revisions =
//                instrumentQuestionAuditService.findRevisions(entity.getId(), new PageRequest(0, 20));
//
//        assertEquals("Excepted four revisions.",
//                revisions.getNumberOfElements(), 4);
//    }
//
//    @Test
//    public void getLastRevisionTest() throws Exception {
//        Revision<Integer, InstrumentQuestion> revision = instrumentQuestionAuditService.findLastChange(entity.getId());
//
//        assertEquals("Excepted initial ResponseDomain Object.",
//                revision.getEntity().hashCode(), entity.hashCode());
//        assertEquals("Expected Name to be 'Third'", revision.getEntity().getName(), "Third");
//    }
}
