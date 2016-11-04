package no.nsd.qddt.domain.commentable;

import no.nsd.qddt.QDDT;
import no.nsd.qddt.domain.AbstractServiceTest;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.comment.CommentService;
import no.nsd.qddt.utils.BeforeSecurityContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QDDT.class)
public class CommentableServiceTest  {

    @Autowired
    private CommentableService commentableService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private AuthenticationManager authenticationManager;

    private BeforeSecurityContext beforeSecurityContext;


    @Before
    public void setup() {

        this.beforeSecurityContext = new BeforeSecurityContext(authenticationManager);
        this.beforeSecurityContext.createSecurityContext();
    }


    @Test
    public void testSaveWithComment() throws Exception {
        Comment owner = new Comment();
        owner.setComment("This is the owner");
        owner = commentService.save(owner);

        Comment parent = new Comment();
        parent.setComment("This is a parent");
        parent.setOwnerId(owner.getId());
        parent = commentService.save(parent);

        parent = commentableService.saveComment(owner, parent);
        assertEquals("Parent owner UUID should equal owner ID", parent.getOwnerId(), owner.getId());
    }

    @Test
    public void testPopulate() throws Exception {
        UUID id = null; //Any id. Does not matter that it gets overridden.
        for(int i = 0; i < 2; i++) {
            Comment owner = new Comment();
            owner.setComment("This is owner " + i);
            owner = commentService.save(owner);
            id = owner.getId();

            for(int j = 0; j < 2; j++) {
                Comment parent = new Comment();
                parent.setComment("This is parent comment " + j);
                parent.setOwnerId(owner.getId());
                parent = commentService.save(parent);

                for(int k = 0; k < 4; k++) {
                    Comment child = new Comment();
                    child.setComment("This is child " + k + " form parent " + j);
                    child.setOwnerId(owner.getId());
//                    child.setParent(parent);
                    commentService.save(child);
                }
            }
        }

        Comment comment= commentService.findOne(id);
        List<Comment> comments = commentableService.populate(comment);

        comments.forEach( c -> {
            assertEquals("OwnerId should match", comment.getId(), c.getOwnerId());
        });
        assertThat("Should have at least one comment", comments.size(), is(10));

    }
}