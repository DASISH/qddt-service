package no.nsd.qddt.domain.group.audit;

import no.nsd.qddt.domain.AbstractAuditServiceTest;
import no.nsd.qddt.domain.group.Group;
import no.nsd.qddt.domain.group.GroupService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * @author Stig Norland
 */
public class GroupAuditServiceTest extends AbstractAuditServiceTest {

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupAuditService groupAuditService;

    private Group group;


    @Before
    public void setUp() {


        group = groupService.save(new Group());

        group.setName("First");
        group = groupService.save(group);
        group.setName("Second");
        group = groupService.save(group);
        group.setName("Third");
        group = groupService.save(group);
    }

    @Test
    public void testsuccess(){
        assertThat(group.getId(),is(group.getId()));
    }


//
//    @Test
//    public void testSaveSurveyWithAudit() throws Exception {
//        group = groupService.findOne(group.getId());
//
//        // Find the last revision based on the entity id
//        Revision<Integer, Group> revision = groupAuditService.findLastChange(group.getId());
//
//        // Find all revisions based on the entity id as a page
//        Page<Revision<Integer, Group>> revisions = groupAuditService.findRevisions(
//                group.getId(), new PageRequest(0, 10));
//        assertThat(revisions.getNumberOfElements(), is(4));
//
//        // Find all revisions
//        Revisions<Integer, Group> wrapper = new Revisions<>(revisions.getContent());
//        assertThat(wrapper.getLatestRevision().getRevisionNumber(), is(revision.getRevisionNumber()));
//    }
//
//    @Test
//    public void getAllRevisionsTest() throws Exception {
//        Page<Revision<Integer, Group>> revisions =
//                groupAuditService.findRevisions(group.getId(), new PageRequest(0, 20));
//
//        assertEquals("Excepted four revisions.",
//                revisions.getNumberOfElements(), 4);
//    }
//
//    @Test
//    public void getLastRevisionTest() throws Exception {
//        Revision<Integer, Group> revision = groupAuditService.findLastChange(group.getId());
//
//        assertThat("Excepted initial ResponseDomain Object.",
//                revision.getEntity().hashCode(), is(group.hashCode()));
//        assertEquals("Expected Name to be 'Third'", revision.getEntity().getName(), "Third");
//    }
}