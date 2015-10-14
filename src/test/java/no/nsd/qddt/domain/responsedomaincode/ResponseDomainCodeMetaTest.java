package no.nsd.qddt.domain.responsedomaincode;

import no.nsd.qddt.QDDT;
import no.nsd.qddt.domain.code.Code;
import no.nsd.qddt.domain.code.CodeService;
import no.nsd.qddt.domain.instrument.Instrument;
import no.nsd.qddt.domain.instrument.InstrumentService;
import no.nsd.qddt.domain.instrumentquestion.InstrumentQuestion;
import no.nsd.qddt.domain.instrumentquestion.InstrumentQuestionService;
import no.nsd.qddt.domain.question.Question;
import no.nsd.qddt.domain.question.QuestionService;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import no.nsd.qddt.domain.responsedomain.ResponseDomainService;
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
 * Created by Dag Østgulen Heradstveit.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QDDT.class)
public class ResponseDomainCodeMetaTest {

    @Autowired
    private ResponseDomainService responseDomainService;

    @Autowired
    private CodeService codeService;

    @Autowired
    private ResponseDomainCodeService responseDomainCodeService;

    private ResponseDomain r1,r2;
    private Code c1,c2;

    @Before
    public void setUp() {

        r1 = responseDomainService.save(new ResponseDomain());
        r2 = responseDomainService.save(new ResponseDomain());

        c1 = codeService.save(new Code());
        c2 = codeService.save(new Code());

        responseDomainCodeService.save(new ResponseDomainCode(0, r1, c1));
        responseDomainCodeService.save(new ResponseDomainCode(0, r2, c2));
        responseDomainCodeService.save(new ResponseDomainCode(0, r1, c2));
    }

    @Test
    public void findByInstrumentTest() throws Exception {
        List<ResponseDomainCode> rdcs = responseDomainCodeService.findByCodeId(c1.getId());
        assertEquals("Expected one element!", rdcs.size(), 1);
    }

    /**
     * Make sure only 1 of 3 possible questions are giving us htis here.
     * @throws Exception
     */
    @Test
    public void findByQuestionTest() throws Exception {
        List<ResponseDomainCode> rdcs = responseDomainCodeService.findByResponseDomainId(r1.getId());
        assertEquals("Expected two elements!", rdcs.size(), 2);
    }
}
