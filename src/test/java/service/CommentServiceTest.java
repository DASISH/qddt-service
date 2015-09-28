package service;

import no.nsd.qddt.QDDT;
import no.nsd.qddt.domain.Comment;
import no.nsd.qddt.service.CommentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


/**
 * @author Dag Østgulen Heradstveit
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QDDT.class)
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Test
    public void saveCommentTest() throws Exception {

        Comment parent = commentService.save(new Comment("PARENT"));
        Comment saved = commentService.save(new Comment("CHILD-ONE"));
        parent.addComment(saved);
        saved.addComment(commentService.save(new Comment("CHILD OF CHILD-ONE")));
        parent.addComment(commentService.save(new Comment("CHILD-TWO")));
        parent.addComment(commentService.save(new Comment("CHILD-THREE")));
        commentService.save(parent);

        Comment root  = commentService.findOne(parent.getGuid());

        assertTrue(5L == parent.treeSize());
        assertThat(parent.getChildren().size(), is(3));
        assertThat(parent, is(root));
    }


    @Test
    public void saveCommentTest2() throws Exception {

        Comment parent = new Comment("PARENT");
        Comment to  = new Comment("CHILD-ONE");
            to.addComment(new Comment("CHILD OF CHILD-ONE"));
            parent.addComment(to);
        parent.addComment(new Comment("CHILD-TWO"));
        parent.addComment(new Comment("CHILD-THREE"));
        commentService.save(parent);

        assertThat(parent.treeSize(), is(5));
        assertThat(commentService.save(parent).getChildren().size(), is(3));
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
