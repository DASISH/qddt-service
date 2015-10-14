package no.nsd.qddt.domain.comment.audit;

import no.nsd.qddt.domain.AbstractAuditServiceTest;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.comment.CommentService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * @author Stig Norland
 */
public class CommentAuditServiceTest extends AbstractAuditServiceTest {

    @Autowired
    private CommentService codeService;

    @Autowired
    private CommentAuditService codeAuditService;

    private Comment entity;

    @Before
    public void setUp() {


        entity = codeService.save(new Comment());

        entity.setComment("First");
        entity = codeService.save(entity);
        entity.setComment("Second");
        entity = codeService.save(entity);
        entity.setComment("Third");
        entity = codeService.save(entity);
    }

    @Test
    public void testSaveSurveyWithAudit() throws Exception {
        entity = codeService.findOne(entity.getId());

        // Find the last revision based on the entity id
        Revision<Integer, Comment> revision = codeAuditService.findLastChange(entity.getId());

        // Find all revisions based on the entity id as a page
        Page<Revision<Integer, Comment>> revisions = codeAuditService.findRevisions(
                entity.getId(), new PageRequest(0, 10));

        Revisions<Integer, Comment> wrapper = new Revisions<>(revisions.getContent());

        assertEquals(wrapper.getLatestRevision().getEntity(), entity);
        assertThat(revisions.getNumberOfElements(), is(4));
    }

    @Test
    public void getAllRevisionsTest() throws Exception {
        Page<Revision<Integer, Comment>> revisions =
                codeAuditService.findRevisions(entity.getId(), new PageRequest(0, 20));

        assertEquals("Excepted four revisions.",
                revisions.getNumberOfElements(), 4);
    }

    @Test
    public void getLastRevisionTest() throws Exception {
        Revision<Integer, Comment> revision = codeAuditService.findLastChange(entity.getId());

        assertEquals("Excepted initial ResponseDomain Object.",
                revision.getEntity(), entity);
        assertEquals("Expected Name to be 'Third'", revision.getEntity().getComment(), "Third");
    }
}