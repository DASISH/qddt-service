package service;

import no.nsd.qddt.QDDT;
import no.nsd.qddt.domain.Comment;
import no.nsd.qddt.service.CommentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Dag Østgulen Heradstveit
 */
@Transactional
@IntegrationTest("server.port:0")
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QDDT.class)
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Test
    public void saveCommentTest() throws Exception {
        Comment comment = new Comment();
        comment.setComment("PARENT");

        Comment commentChildOne = new Comment();
        commentChildOne.setComment("CHILD-ONE");

        Comment commentChildTwo = new Comment();
        commentChildTwo.setComment("CHILD-TWO");

        Comment commentChildThree = new Comment();
        commentChildThree.setComment("CHILD-THREE");

        comment.addComment(commentChildOne);
        comment.addComment(commentChildTwo);
        comment.addComment(commentChildThree);

        assertThat(commentService.save(comment).getChildren().size(), is(3));
    }

    /**
     * Tests for the child -> parent relation are not
     * neccessary, as a child will never be populated
     * without the context of its parent.
     *
     * Children will always be added to the parent.
     * When listing comments, they do not need to have an
     * explicit relation to parents.
     *
     * Children will be added to the parent, or a child
     * will have a child added to it, making it a parent
     * and a child at the same time.
     */
}
