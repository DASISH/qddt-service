package no.nsd.qddt.domain.topicgroup.audit;

import no.nsd.qddt.domain.AbstractAuditServiceTest;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import no.nsd.qddt.domain.topicgroup.TopicGroupService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Stig Norland
 */
public class TopicGroupAuditServiceTest  extends AbstractAuditServiceTest {

    @Autowired
    private TopicGroupService topicGroupService;

    @Autowired
    private TopicGroupAuditService topicGroupAuditService;

    private TopicGroup topicGroup;

    @Before
    public void setUp() {
        topicGroup = topicGroupService.save(new TopicGroup());
        topicGroup.setName("First");
        topicGroup = topicGroupService.save(topicGroup);
        topicGroup.setName("Second");
        topicGroup = topicGroupService.save(topicGroup);
        topicGroup.setName("Third");
        topicGroup = topicGroupService.save(topicGroup);
    }

    @Test
    public void testSaveSurveyWithAudit() throws Exception {
        // Find all revisions based on the topicGroup id as a page
        Page<Revision<Integer, TopicGroup>> revisions = topicGroupAuditService.findRevisions(
                topicGroup.getId(), new PageRequest(0, 10));

        Revisions<Integer, TopicGroup> wrapper = Revisions.of(revisions.getContent());

        assertEquals(wrapper.getLatestRevision().getEntity().hashCode(), topicGroup.hashCode());
        assertThat(revisions.getNumberOfElements(), is(4));
    }

    @Test
    public void getAllRevisionsTest() throws Exception {
        Page<Revision<Integer, TopicGroup>> revisions =
                topicGroupAuditService.findRevisions(topicGroup.getId(), new PageRequest(0, 20));

        assertEquals("Excepted four revisions.",
                4, revisions.getNumberOfElements());
    }

    @Test
    public void getLastRevisionTest() throws Exception {
        Revision<Integer, TopicGroup> revision = topicGroupAuditService.findLastChange(topicGroup.getId());

        assertEquals("Excepted initial TopicGroup object.",
                revision.getEntity().hashCode(), topicGroup.hashCode());
        assertEquals("Expected Name to be 'Third'", "Third", revision.getEntity().getName());
    }
}

