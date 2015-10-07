package no.nsd.qddt.domain.commentable;

import no.nsd.qddt.domain.AbstractServiceTest;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.comment.CommentService;
import no.nsd.qddt.domain.study.Study;
import no.nsd.qddt.domain.study.StudyService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@Transactional
public class CommentableServiceTest extends AbstractServiceTest {

    @Autowired
    private CommentableService commentableService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private StudyService studyService;

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
    public void testSaveWithStudy() throws Exception {
        Study owner = new Study();
        owner.setName("This is the owner");
        owner = studyService.save(owner);

        Comment comment = new Comment();
        comment.setComment("This is a parent");
        comment.setOwnerId(owner.getId());
        comment = commentService.save(comment);

        comment = commentableService.saveComment(owner, comment);
        assertEquals("Parent owner UUID should equal owner ID", comment.getOwnerId(), owner.getId());
    }

    @Test
    public void testPopulate() throws Exception {
        UUID id = null; //Any id. Does not matter that it gets overridden.
        for(int i = 0; i < 2; i++) {
            Study owner = new Study();
            owner.setName("This is study " + i);
            owner = studyService.save(owner);
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
                    child.setParent(parent);
                    commentService.save(child);
                }
            }
        }

        Study study = studyService.findOne(id);
        List<Comment> comments = commentableService.populate(study);

        comments.forEach( c -> {
            assertEquals("OwnerId should match", study.getId(), c.getOwnerId());
        });
        assertThat("Should have at least one comment", comments.size(), is(10));

    }
}