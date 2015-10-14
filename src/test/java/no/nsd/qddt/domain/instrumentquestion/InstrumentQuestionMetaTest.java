package no.nsd.qddt.domain.instrumentquestion;

import no.nsd.qddt.QDDT;
import no.nsd.qddt.domain.instrument.Instrument;
import no.nsd.qddt.domain.instrument.InstrumentService;
import no.nsd.qddt.domain.question.Question;
import no.nsd.qddt.domain.question.QuestionService;
import no.nsd.qddt.utils.builders.InstrumentBuilder;
import no.nsd.qddt.utils.builders.InstrumentQuestionBuilder;
import no.nsd.qddt.utils.builders.QuestionBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QDDT.class)
public class InstrumentQuestionMetaTest {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private InstrumentService instrumentService;

    @Autowired
    private InstrumentQuestionService instrumentQuestionService;

    private Instrument i1,i2;
    private Question q1,q2;

    @Before
    public void setUp() {
        i1 = instrumentService.save(new InstrumentBuilder().setName("Test Instrument 1").setChangeComment("Changed for tests.").createInstrument());
        i2 = instrumentService.save(new InstrumentBuilder().setName("Test Instrument 2").setChangeComment("Changed for more tests.").createInstrument());

        q1 = questionService.save(new QuestionBuilder().setName("Male").setChangeComment("Changed typo").setInstructions("Used to find a man").createInstrument());
        q2 = questionService.save(new QuestionBuilder().setName("Female").setChangeComment("Changed changed another typo").setInstructions("Used to find a woman").createInstrument());

        instrumentQuestionService.save(new InstrumentQuestionBuilder().setName("A instrumentQuestion").setChangeComment("Changed because.")
                        .setQuestion(q1).setInstrument(i1).createInstrument());

        instrumentQuestionService.save(new InstrumentQuestionBuilder().setName("Different instrumentQuestion").setChangeComment("It had to be done at some point!")
                .setQuestion(q1).setInstrument(i2).createInstrument());

        instrumentQuestionService.save(new InstrumentQuestionBuilder().setName("Totally different instrumentQuestion").setChangeComment("Again, something had to be done!")
                .setQuestion(q2).setInstrument(i2).createInstrument());
    }

    @Test
    public void findByInstrumentTest() throws Exception {
        List<InstrumentQuestion> isq = instrumentQuestionService.findByInstrumentId(i1.getId());
        assertEquals("Expected two elements!", isq.size(), 1);
    }

    /**
     * Make sure only 1 of 3 possible questions are giving us htis here.
     * @throws Exception
     */
    @Test
    public void findByQuestionTest() throws Exception {
        List<InstrumentQuestion> iqs = instrumentQuestionService.findByQuestionId(q1.getId());
        assertEquals("Expected two elements!", iqs.size(), 2);
    }
}
