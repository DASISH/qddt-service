package no.nsd.qddt.domain.controlconstruct;

import no.nsd.qddt.domain.controlconstruct.json.ConstructQuestionJson;
import no.nsd.qddt.domain.instrument.pojo.Instrument;
import no.nsd.qddt.domain.instrument.InstrumentService;
import no.nsd.qddt.domain.questionitem.QuestionItem;
import no.nsd.qddt.domain.questionitem.QuestionItemService;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import no.nsd.qddt.domain.responsedomain.ResponseDomainService;
import no.nsd.qddt.utils.builders.InstrumentBuilder;
import no.nsd.qddt.utils.builders.QuestionItemBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

//import no.nsd.qddt.domain.question.Question;
//import no.nsd.qddt.utils.builders.QuestionBuilder;
;

@RunWith(SpringJUnit4ClassRunner.class)
public class ControlConstructMetaTest {

//    @Autowired
//    private QuestionService questionService;

    @Autowired
    private QuestionItemService questionItemService;

    @Autowired
    private ResponseDomainService responseDomainService;

    @Autowired
    private InstrumentService instrumentService;

    @Autowired
    private ControlConstructService controlConstructService;

    private Instrument i1,i2;
    private QuestionItem qi1, qi2;
    private ResponseDomain r1;

    @Before
    public void setUp() {

        i1 = instrumentService.save(new InstrumentBuilder().setName("Test Instrument 1").setChangeComment("Changed for tests.").createInstrument());
        i2 = instrumentService.save(new InstrumentBuilder().setName("Test Instrument 2").setChangeComment("Changed for more tests.").createInstrument());

        r1 = responseDomainService.save(new ResponseDomain());

        qi1 = questionItemService.save(new QuestionItemBuilder().setName("item1").setQuestion("Changed typo").setResponseDomain(r1).createQuestionItem());
        qi2 = questionItemService.save(new QuestionItemBuilder().setName("item2").setQuestion("Changed changed another typo").setResponseDomain(r1).createQuestionItem());

    }

    /**
     * Make sure only 1 of 3 possible questions are giving us htis here.
     * @throws Exception
     */
    @Test
    public void findByQuestionTest()  {
        List<ConstructQuestionJson> iqs = controlConstructService.findByQuestionItems(Arrays.asList(qi1.getId()));
        assertEquals("Expected two elements!", iqs.size(), 2);
    }
}
