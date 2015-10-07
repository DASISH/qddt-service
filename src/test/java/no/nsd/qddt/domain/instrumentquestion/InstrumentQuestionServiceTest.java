package no.nsd.qddt.domain.instrumentquestion;

import no.nsd.qddt.domain.AbstractServiceTest;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by Dag Østgulen Heradstveit.
 */
public class InstrumentQuestionServiceTest extends AbstractServiceTest {

    @Autowired
    private InstrumentQuestionService instrumentQuestionService;

    @Autowired
    private InstrumentQuestionRepository instrumentRepository;

    @Before
    public void setup() {
        super.setBaseRepositories(instrumentRepository);
    }

    @Test
    @Override
    public void testCount() throws Exception {
        InstrumentQuestion instrumentQuestion = new InstrumentQuestion();
        instrumentQuestion.setName("Test InstrumentQuestion One");
        instrumentQuestionService.save(instrumentQuestion);

        instrumentQuestion = new InstrumentQuestion();
        instrumentQuestion.setName("Test InstrumentQuestion Two");
        instrumentQuestionService.save(instrumentQuestion);

        instrumentQuestion = new InstrumentQuestion();
        instrumentQuestion.setName("Test InstrumentQuestion Three");
        instrumentQuestionService.save(instrumentQuestion);

        assertThat("Should be three", instrumentQuestionService.count(), is(3L));
    }

    @Test
    @Override
    public void testExists() throws Exception {
        InstrumentQuestion instrumentQuestion = new InstrumentQuestion();
        instrumentQuestion.setName("Existing instrumentQuestion");
        instrumentQuestion = instrumentQuestionService.save(instrumentQuestion);
        assertTrue("InstrumentQuestion should exist", instrumentQuestionService.exists(instrumentQuestion.getId()));
    }

    @Test
    @Override
    public void testFindOne() throws Exception {
        InstrumentQuestion instrumentQuestion = new InstrumentQuestion();
        instrumentQuestion.setName("Existing instrumentQuestion");
        instrumentQuestion = instrumentQuestionService.save(instrumentQuestion);
        assertNotNull("InstrumentQuestion should not be null", instrumentQuestionService.findOne(instrumentQuestion.getId()));
    }

    @Test
    @Override
    public void testSave() throws Exception {
        InstrumentQuestion instrumentQuestion = new InstrumentQuestion();
        instrumentQuestion.setName("Existing instrumentQuestion");
        assertNotNull("InstrumentQuestion should be saved", instrumentQuestionService.save(instrumentQuestion));
    }

    @Test
    @Override
    public void testSaveAll() throws Exception {
        List<InstrumentQuestion> agencyList = new ArrayList<>();
        InstrumentQuestion instrumentQuestion = new InstrumentQuestion();
        instrumentQuestion.setName("Test InstrumentQuestion One");
        agencyList.add(instrumentQuestion);

        instrumentQuestion = new InstrumentQuestion();
        instrumentQuestion.setName("Test InstrumentQuestion Two");
        agencyList.add(instrumentQuestion);

        instrumentQuestion = new InstrumentQuestion();
        instrumentQuestion.setName("Test InstrumentQuestion Three");
        agencyList.add(instrumentQuestion);

        instrumentQuestionService.save(agencyList);

        assertEquals("Should return 3", instrumentQuestionService.count(), 3L);
    }

    @Test(expected = ResourceNotFoundException.class)
    @Override
    public void testDelete() throws Exception {
        InstrumentQuestion instrumentQuestion = new InstrumentQuestion();
        instrumentQuestion.setName("Existing instrumentQuestion");
        instrumentQuestion = instrumentQuestionService.save(instrumentQuestion);
        instrumentQuestionService.delete(instrumentQuestion.getId());

        assertNull("Should return null", instrumentQuestionService.findOne(instrumentQuestion.getId()));
    }

    @Test(expected = ResourceNotFoundException.class)
    @Override
    public void testDeleteAll() throws Exception {
        List<InstrumentQuestion> agencyList = new ArrayList<>();
        InstrumentQuestion instrumentQuestion = new InstrumentQuestion();
        instrumentQuestion.setName("Test InstrumentQuestion One");
        agencyList.add(instrumentQuestion);

        instrumentQuestion = new InstrumentQuestion();
        instrumentQuestion.setName("Test InstrumentQuestion Two");
        agencyList.add(instrumentQuestion);

        instrumentQuestion = new InstrumentQuestion();
        instrumentQuestion.setName("Test InstrumentQuestion Three");
        agencyList.add(instrumentQuestion);

        agencyList = instrumentQuestionService.save(agencyList);
        instrumentQuestionService.delete(agencyList);

        agencyList.forEach(a -> assertNull("Should return null", instrumentQuestionService.findOne(a.getId())));

    }
}