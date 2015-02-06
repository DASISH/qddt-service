package service;

import no.nsd.qddt.QDDT;
import no.nsd.qddt.domain.instrument.InstrumentQuestion;
import no.nsd.qddt.service.InstrumentQuestionService;
import no.nsd.qddt.service.InstrumentService;
import no.nsd.qddt.service.QuestionService;
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

/**
 * @author Dag Østgulen Heradstveit
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QDDT.class)
public class InstrumentQuestionTest {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private InstrumentService instrumentService;

    @Autowired
    private InstrumentQuestionService instrumentQuestionService;

    @Before
    public void setUp() {
        //User user, String changeComment, String name
        instrumentService.save(new InstrumentBuilder().setName("Test Instrument 1").setChangeComment("Changed for tests.").createInstrument());
        instrumentService.save(new InstrumentBuilder().setName("Test Instrument 2").setChangeComment("Changed for more tests.").createInstrument());

        questionService.save(new QuestionBuilder().setName("Male").setChangeComment("Changed typo").setInstructions("Used to find a man").createInstrument());
        questionService.save(new QuestionBuilder().setName("Female").setChangeComment("Changed changed another typo").setInstructions("Used to find a woman").createInstrument());

        instrumentQuestionService.save(new InstrumentQuestionBuilder().setName("A instrumentQuestion").setChangeComment("Changed because.")
            .setQuestion(questionService.findById(1L)).setInstrument(instrumentService.findById(1L)).createInstrument());

        instrumentQuestionService.save(new InstrumentQuestionBuilder().setName("Different instrumentQuestion").setChangeComment("It had to be done at some point!")
                .setQuestion(questionService.findById(2L)).setInstrument(instrumentService.findById(1L)).createInstrument());

        instrumentQuestionService.save(new InstrumentQuestionBuilder().setName("Totally different instrumentQuestion").setChangeComment("Again, something had to be done!")
                .setQuestion(questionService.findById(2L)).setInstrument(instrumentService.findById(2L)).createInstrument());
    }

    @Test
    public void findByInstrumentTest() throws Exception {
        List<InstrumentQuestion> isq = instrumentQuestionService.findByInstrumentId(instrumentService.findById(2L).getId());
        assertEquals("Expected two elements!", isq.size(), 2);
    }

    /**
     * Make sure only 1 of 3 possible questions are giving us htis here.
     * @throws Exception
     */
    @Test
    public void findByQuestionTest() throws Exception {
        List<InstrumentQuestion> iqs = instrumentQuestionService.findByQuestionId(questionService.findById(2L).getId());
        assertEquals("Expected two elements!", iqs.size(), 2);
    }
}
