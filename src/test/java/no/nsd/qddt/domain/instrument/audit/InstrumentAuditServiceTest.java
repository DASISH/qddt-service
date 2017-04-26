package no.nsd.qddt.domain.instrument.audit;

import no.nsd.qddt.domain.AbstractAuditServiceTest;
import no.nsd.qddt.domain.instrument.Instrument;
import no.nsd.qddt.domain.instrument.InstrumentService;
import no.nsd.qddt.domain.controlconstruct.ControlConstructService;
//import no.nsd.qddt.domain.question.QuestionService;
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
public class InstrumentAuditServiceTest extends AbstractAuditServiceTest {

    @Autowired
    private InstrumentService instrumentService;

    @Autowired
    private InstrumentAuditService instrumentAuditService;

//    @Autowired
//    private QuestionService questionService;

    @Autowired
    private ControlConstructService controlConstructService;



    private Instrument instrument;

    @Before
    public void setUp() {


        instrument = instrumentService.save(new Instrument());

//        Question question = new Question();
//        question.setQuestion("What does the fox say");
//        question = questionService.save(question);
//
//        InstrumentQuestion instrumentQuestion =  new InstrumentQuestion();
//        instrumentQuestion.setInstrument(instrument);
//        instrumentQuestion.setQuestion(question);
//        instrumentQuestionService.save(instrumentQuestion);

        instrument = instrumentService.findOne(instrument.getId());

        instrument.setName("First");
        instrument = instrumentService.save(instrument);
        instrument.setName("Second");
        instrument = instrumentService.save(instrument);
        instrument.setName("Third");
        instrument = instrumentService.save(instrument);
    }

    @Test
    public void testSaveSurveyWithAudit() throws Exception {
        instrument = instrumentService.findOne(instrument.getId());

        // Find the last revision based on the entity id
        Revision<Integer, Instrument> revision = instrumentAuditService.findLastChange(instrument.getId());

        // Find all revisions based on the entity id as a page
        Page<Revision<Integer, Instrument>> revisions = instrumentAuditService.findRevisions(
                instrument.getId(), new PageRequest(0, 10));

        Revisions<Integer, Instrument> wrapper = new Revisions<>(revisions.getContent());

        assertEquals(wrapper.getLatestRevision().getEntity().hashCode(), instrument.hashCode());
        assertThat(revisions.getNumberOfElements(), is(4));
    }

    @Test
    public void getAllRevisionsTest() throws Exception {
        Page<Revision<Integer, Instrument>> revisions =
                instrumentAuditService.findRevisions(instrument.getId(), new PageRequest(0, 20));

        assertEquals("Excepted four revisions.",
                revisions.getNumberOfElements(), 4);
    }

    @Test
    public void getLastRevisionTest() throws Exception {
        Revision<Integer, Instrument> revision = instrumentAuditService.findLastChange(instrument.getId());

        assertEquals("Excepted initial ResponseDomain Object.",
                revision.getEntity().hashCode(), instrument.hashCode());
        assertEquals("Expected Name to be 'Third'", revision.getEntity().getName(), "Third");
    }
}
