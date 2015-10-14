package no.nsd.qddt.domain.question;

import no.nsd.qddt.domain.AbstractServiceTest;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class QuestionServiceTest  extends AbstractServiceTest {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionRepository questionRepository;

    @Before
    public void setup() {
        super.setBaseRepositories(questionRepository);
    }

    @Test
    @Override
    public void testCount() throws Exception {
        Question question = new Question();
        question.setName("Test Question One");
        questionService.save(question);

        question = new Question();
        question.setName("Test Question Two");
        questionService.save(question);

        question = new Question();
        question.setName("Test Question Three");
        questionService.save(question);

        Assert.assertThat("Should be three", questionService.count(), Is.is(3L));
    }

    @Test
    @Override
    public void testExists() throws Exception {
        Question question = new Question();
        question.setName("Existing question");
        question = questionService.save(question);
        assertTrue("Question should exist", questionService.exists(question.getId()));
    }

    @Test
    @Override
    public void testFindOne() throws Exception {
        Question question = new Question();
        question.setName("Existing question");
        question = questionService.save(question);
        assertNotNull("Question should not be null", questionService.findOne(question.getId()));
    }

    @Test
    @Override
    public void testSave() throws Exception {
        Question question = new Question();
        question.setName("Existing question");
        assertNotNull("Question should be saved", questionService.save(question));
    }

    @Test
    @Override
    public void testSaveAll() throws Exception {
        List<Question> agencyList = new ArrayList<>();
        Question question = new Question();
        question.setName("Test Question One");
        agencyList.add(question);

        question = new Question();
        question.setName("Test Question Two");
        agencyList.add(question);

        question = new Question();
        question.setName("Test Question Three");
        agencyList.add(question);

        questionService.save(agencyList);

        assertEquals("Should return 3", 3L, questionService.count());
    }

    @Test(expected = ResourceNotFoundException.class)
    @Override
    public void testDelete() throws Exception {
        Question question = new Question();
        question.setName("Existing question");
        question = questionService.save(question);
        questionService.delete(question.getId());

        assertNull("Should return null", questionService.findOne(question.getId()));
    }

    @Test(expected = ResourceNotFoundException.class)
    @Override
    public void testDeleteAll() throws Exception {
        List<Question> agencyList = new ArrayList<>();
        Question question = new Question();
        question.setName("Test Question One");
        agencyList.add(question);

        question = new Question();
        question.setName("Test Question Two");
        agencyList.add(question);

        question = new Question();
        question.setName("Test Question Three");
        agencyList.add(question);

        agencyList = questionService.save(agencyList);
        questionService.delete(agencyList);

        agencyList.forEach(a -> assertNull("Should return null", questionService.findOne(a.getId())));

    }
}
