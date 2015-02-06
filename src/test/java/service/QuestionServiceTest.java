package service;

import no.nsd.qddt.QDDT;
import no.nsd.qddt.domain.Question;
import no.nsd.qddt.exception.ResourceNotFoundException;
import no.nsd.qddt.service.QuestionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Stig Norland
 */

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QDDT.class)
public class QuestionServiceTest {
    @Autowired
    private QuestionService questionService;

    @Test
    public void saveQuestionTest() throws Exception {
        Question question = new Question();
        question.setName("PARENT - FØRSTE SPØRSMÅL");

        Question q1 = new Question();
        q1.setName("1 CHILD");

        Question q2 = new Question();
        q2.setName("2 CHILD");

        Question q3 = new Question();
        q3.setName("3 CHILD");

        question.addChild(q1);
        question.addChild(q2);
        question.addChild(q3);



        assertThat(questionService.save(question).getChildren().size(), is(3));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void fail() throws Exception {
        Question q = questionService.findById(1000L);
    }

}