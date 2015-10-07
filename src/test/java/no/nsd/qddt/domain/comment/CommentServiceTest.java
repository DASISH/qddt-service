package no.nsd.qddt.domain.comment;

import no.nsd.qddt.domain.AbstractServiceTest;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class CommentServiceTest  extends AbstractServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository codeRepository;

    @Before
    public void setup() {
        super.setBaseRepositories(codeRepository);
    }

    @Test
    @Override
    public void testCount() throws Exception {
        Comment comment = new Comment();
        comment.setComment("Test Comment One");
        commentService.save(comment);

        comment = new Comment();
        comment.setComment("Test Comment Two");
        commentService.save(comment);

        comment = new Comment();
        comment.setComment("Test Comment Three");
        commentService.save(comment);

        assertThat("Should be three", commentService.count(), is(3L));
    }

    @Test
    @Override
    public void testExists() throws Exception {
        Comment comment = new Comment();
        comment.setComment("Existing comment");
        comment = commentService.save(comment);
        assertTrue("Comment should exist", commentService.exists(comment.getId()));
    }

    @Test
    @Override
    public void testFindOne() throws Exception {
        Comment comment = new Comment();
        comment.setComment("Existing comment");
        comment = commentService.save(comment);
        assertNotNull("Comment should not be null", commentService.findOne(comment.getId()));
    }

    @Test
    @Override
    public void testSave() throws Exception {
        Comment comment = new Comment();
        comment.setComment("Existing comment");
        assertNotNull("Comment should be saved", commentService.save(comment));
    }

    @Test
    @Override
    public void testSaveAll() throws Exception {
        List<Comment> agencyList = new ArrayList<>();
        Comment comment = new Comment();
        comment.setComment("Test Comment One");
        agencyList.add(comment);

        comment = new Comment();
        comment.setComment("Test Comment Two");
        agencyList.add(comment);

        comment = new Comment();
        comment.setComment("Test Comment Three");
        agencyList.add(comment);

        commentService.save(agencyList);

        assertEquals("Should return 3", commentService.count(), 3L);
    }

    @Test(expected = ResourceNotFoundException.class)
    @Override
    public void testDelete() throws Exception {
        Comment comment = new Comment();
        comment.setComment("Existing comment");
        comment = commentService.save(comment);
        commentService.delete(comment.getId());

        assertNull("Should return null", commentService.findOne(comment.getId()));
    }

    @Test(expected = ResourceNotFoundException.class)
    @Override
    public void testDeleteAll() throws Exception {
        List<Comment> agencyList = new ArrayList<>();
        Comment comment = new Comment();
        comment.setComment("Test Comment One");
        agencyList.add(comment);

        comment = new Comment();
        comment.setComment("Test Comment Two");
        agencyList.add(comment);

        comment = new Comment();
        comment.setComment("Test Comment Three");
        agencyList.add(comment);

        agencyList = commentService.save(agencyList);
        commentService.delete(agencyList);

        agencyList.forEach(a -> assertNull("Should return null", commentService.findOne(a.getId())));

    }
}