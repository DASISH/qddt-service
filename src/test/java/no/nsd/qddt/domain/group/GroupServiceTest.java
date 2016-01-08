package no.nsd.qddt.domain.group;

import no.nsd.qddt.domain.AbstractServiceTest;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * @author Stig Norland
 */
public class GroupServiceTest extends AbstractServiceTest {

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupRepository groupRepository;

    @Before
    public void setup() {
        super.setup();
        super.setBaseRepositories(groupRepository);
    }

    @Test
    @Override
    public void testCount() throws Exception {
        Group group = new Group();
        group.setName("Test Group One");
        groupService.save(group);

        group = new Group();
        group.setName("Test Group Two");
        groupService.save(group);

        group = new Group();
        group.setName("Test Group Three");
        groupService.save(group);

        assertThat("Should be three", groupService.count(), is(3L));
    }

    @Test
    @Override
    public void testExists() throws Exception {
        Group group = new Group();
        group.setName("Existing group");
        group = groupService.save(group);
        assertTrue("Group should exist", groupService.exists(group.getId()));
    }

    @Test
    @Override
    public void testFindOne() throws Exception {
        Group group = new Group();
        group.setName("Existing group");
        group = groupService.save(group);
        assertNotNull("Group should not be null", groupService.findOne(group.getId()));
    }

    @Test
    @Override
    public void testSave() throws Exception {
        Group group = new Group();
        group.setName("Existing group");
        assertNotNull("Group should be saved", groupService.save(group));
    }

    @Test
    @Override
    public void testSaveAll() throws Exception {
        List<Group> agencyList = new ArrayList<>();
        Group group = new Group();
        group.setName("Test Group One");
        agencyList.add(group);

        group = new Group();
        group.setName("Test Group Two");
        agencyList.add(group);

        group = new Group();
        group.setName("Test Group Three");
        agencyList.add(group);

        groupService.save(agencyList);

        assertEquals("Should return 3", groupService.count(), 3L);
    }

    @Test //(expected = ResourceNotFoundException.class)
    @Override
    public void testDelete() throws Exception {
        Group group = new Group();
        group.setName("Existing group");
        group = groupService.save(group);
        groupService.delete(group.getId());

        assertNull("Should return null", groupService.findOne(group.getId()));
    }

    @Test //(expected = ResourceNotFoundException.class)
    @Override
    public void testDeleteAll() throws Exception {
        List<Group> agencyList = new ArrayList<>();
        Group group = new Group();
        group.setName("Test Group One");
        agencyList.add(group);

        group = new Group();
        group.setName("Test Group Two");
        agencyList.add(group);

        group = new Group();
        group.setName("Test Group Three");
        agencyList.add(group);

        agencyList = groupService.save(agencyList);
        groupService.delete(agencyList);

        agencyList.forEach(a -> assertNull("Should return null", groupService.findOne(a.getId())));

    }

}
