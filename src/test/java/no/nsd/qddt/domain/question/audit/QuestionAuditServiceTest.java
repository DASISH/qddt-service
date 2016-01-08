package no.nsd.qddt.domain.question.audit;

import no.nsd.qddt.domain.AbstractAuditServiceTest;
import no.nsd.qddt.domain.question.Question;
import no.nsd.qddt.domain.question.QuestionService;
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
public class QuestionAuditServiceTest extends AbstractAuditServiceTest {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionAuditService questionAuditService;

    private Question question;

    @Before
    public void setUp() {


        question = questionService.save(new Question());

        question.setName("First");
        question = questionService.save(question);
        question.setName("Second");
        question = questionService.save(question);
        question.setName("Third");
        question = questionService.save(question);
    }

    @Test
    public void testSaveSurveyWithAudit() throws Exception {
        question = questionService.findOne(question.getId());

        // Find the last revision based on the entity id
        Revision<Integer, Question> revision = questionAuditService.findLastChange(question.getId());

        // Find all revisions based on the entity id as a page
        Page<Revision<Integer, Question>> revisions = questionAuditService.findRevisions(
                question.getId(), new PageRequest(0, 10));

        Revisions<Integer, Question> wrapper = new Revisions<>(revisions.getContent());

        assertEquals(wrapper.getLatestRevision().getEntity().hashCode(), question.hashCode());
        assertThat(revisions.getNumberOfElements(), is(4));
    }

    @Test
    public void getAllRevisionsTest() throws Exception {
        Page<Revision<Integer, Question>> revisions =
                questionAuditService.findRevisions(question.getId(), new PageRequest(0, 20));

        assertEquals("Excepted four revisions.",
                revisions.getNumberOfElements(), 4);
    }

    @Test
    public void getLastRevisionTest() throws Exception {
        Revision<Integer, Question> revision = questionAuditService.findLastChange(question.getId());

        assertEquals("Excepted initial ResponseDomain Object.",
                revision.getEntity().hashCode(), question.hashCode());
        assertEquals("Expected Name to be 'Third'", revision.getEntity().getName(), "Third");
    }
}
