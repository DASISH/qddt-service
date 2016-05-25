package no.nsd.qddt.domain.controlconstruct;

import no.nsd.qddt.QDDT;
import no.nsd.qddt.domain.instrument.Instrument;
import no.nsd.qddt.domain.instrument.InstrumentService;
import no.nsd.qddt.domain.question.Question;
import no.nsd.qddt.domain.question.QuestionService;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.questionItem.QuestionItemService;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import no.nsd.qddt.domain.responsedomain.ResponseDomainService;
import no.nsd.qddt.utils.builders.InstrumentBuilder;
import no.nsd.qddt.utils.builders.InstrumentQuestionItemBuilder;
import no.nsd.qddt.utils.builders.QuestionBuilder;
import no.nsd.qddt.utils.builders.QuestionItemBuilder;
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
public class ControlConstructMetaTest {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionItemService questionItemService;

    @Autowired
    private ResponseDomainService responseDomainService;

    @Autowired
    private InstrumentService instrumentService;

    @Autowired
    private ControlConstructService controlConstructService;

    private Instrument i1,i2;
    private Question q1,q2;
    private QuestionItem qi1, qi2;
    private ResponseDomain r1;

    @Before
    public void setUp() {

        i1 = instrumentService.save(new InstrumentBuilder().setName("Test Instrument 1").setChangeComment("Changed for tests.").createInstrument());
        i2 = instrumentService.save(new InstrumentBuilder().setName("Test Instrument 2").setChangeComment("Changed for more tests.").createInstrument());

        q1 = questionService.save(new QuestionBuilder().setName("Male").setChangeComment("Changed typo").createQuestion());
        q2 = questionService.save(new QuestionBuilder().setName("Female").setChangeComment("Changed changed another typo").createQuestion());

        r1 = responseDomainService.save(new ResponseDomain());

        qi1 = questionItemService.save(new QuestionItemBuilder().setName("item1").setQuestion(q1).setResponseDomain(r1).createQuestionItem());
        qi2 = questionItemService.save(new QuestionItemBuilder().setName("item2").setQuestion(q2).setResponseDomain(r1).createQuestionItem());

        controlConstructService.save(new InstrumentQuestionItemBuilder().setName("A instrumentQuestion").setChangeComment("Changed because.")
                        .setQuestion(qi1).setInstrument(i1).createInstrument());

        controlConstructService.save(new InstrumentQuestionItemBuilder().setName("Different instrumentQuestion").setChangeComment("It had to be done at some point!")
                .setQuestion(qi1).setInstrument(i2).createInstrument());

        controlConstructService.save(new InstrumentQuestionItemBuilder().setName("Totally different instrumentQuestion").setChangeComment("Again, something had to be done!")
                .setQuestion(qi2).setInstrument(i2).createInstrument());
    }

    @Test
    public void findByInstrumentTest() throws Exception {
        List<ControlConstruct> isq = controlConstructService.findByInstrumentId(i1.getId());
        assertEquals("Expected two elements!", isq.size(), 1);
    }

    /**
     * Make sure only 1 of 3 possible questions are giving us htis here.
     * @throws Exception
     */
    @Test
    public void findByQuestionTest() throws Exception {
        List<ControlConstruct> iqs = controlConstructService.findByQuestionItemId(q1.getId());
        assertEquals("Expected two elements!", iqs.size(), 2);
    }
}
