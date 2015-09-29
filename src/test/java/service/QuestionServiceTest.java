package service;

import no.nsd.qddt.QDDT;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.question.Question;
import no.nsd.qddt.exception.ResourceNotFoundException;
import no.nsd.qddt.domain.question.QuestionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

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
        Comment c1 = new Comment("første test kommentar");
        q1.addComment(c1);
        c1.addComment(new Comment("barn kommentar, nivå 2"));
        q1.addComment(new Comment("neste kommentar nivå 1"));

        Question q2 = new Question();
        q2.setName("2 CHILD");

        Question q3 = new Question();
        q3.setName("3 CHILD");

        question.addChild(q1);
        question.addChild(q2);
        question.addChild(q3);

        assertThat(questionService.save(question).getChildren().size(), is(3));
        assertThat(q1.getComments().size(), is(2));

    }




    @Test(expected = ResourceNotFoundException.class)
    public void fail() throws Exception {
        Question q = questionService.findOne(UUID.randomUUID());
    }

}