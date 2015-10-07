package domain.comment;

import domain.AbstractServiceTest;
import no.nsd.qddt.QDDT;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.comment.CommentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class CommentServiceTest extends AbstractServiceTest {

    @Autowired
    private CommentService commentService;

    @Test
    public void saveCommentTest() throws Exception {
        UUID ownerId = UUID.randomUUID();

        Comment parent = commentService.save(new Comment("PARENT"));
        parent.setOwnerId(ownerId);

        Comment saved = commentService.save(new Comment("CHILD-ONE"));
        parent.addComment(saved);
        saved.addComment(commentService.save(new Comment("CHILD OF CHILD-ONE")));
        parent.addComment(commentService.save(new Comment("CHILD-TWO")));
        parent.addComment(commentService.save(new Comment("CHILD-THREE")));
        parent = commentService.save(parent);

        Comment root  = commentService.findOne(parent.getId());

        assertThat("Parent tree should hold 5 elements.",  parent.treeSize(), is(5));
        assertThat("Parent should have 3 children.", parent.getChildren().size(), is(3));
        assertEquals("Parent and root should be equal.", parent, root);
    }
}