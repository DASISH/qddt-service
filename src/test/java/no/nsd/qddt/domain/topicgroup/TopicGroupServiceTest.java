package no.nsd.qddt.domain.topicgroup;

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
 * Created by Dag Østgulen Heradstveit.
 */
public class TopicGroupServiceTest extends AbstractServiceTest {

    @Autowired
    private TopicGroupService topicGroupService;

    @Autowired
    private TopicGroupRepository codeRepository;

    @Before
    public void setup() {
        super.setup();
        super.setBaseRepositories(codeRepository);
    }

    @Test
    @Override
    public void testCount() throws Exception {
        TopicGroup topicGroup = new TopicGroup();
        topicGroup.setName("Test TopicGroup One");
        topicGroupService.save(topicGroup);

        topicGroup = new TopicGroup();
        topicGroup.setName("Test TopicGroup Two");
        topicGroupService.save(topicGroup);

        topicGroup = new TopicGroup();
        topicGroup.setName("Test TopicGroup Three");
        topicGroupService.save(topicGroup);

        assertThat("Should be three", topicGroupService.count(), is(3L));
    }

    @Test
    @Override
    public void testExists() throws Exception {
        TopicGroup topicGroup = new TopicGroup();
        topicGroup.setName("Existing topicGroup");
        topicGroup = topicGroupService.save(topicGroup);
        assertTrue("TopicGroup should exist", topicGroupService.exists(topicGroup.getId()));
    }

    @Test
    @Override
    public void testFindOne() throws Exception {
        TopicGroup topicGroup = new TopicGroup();
        topicGroup.setName("Existing topicGroup");
        topicGroup = topicGroupService.save(topicGroup);
        assertNotNull("TopicGroup should not be null", topicGroupService.findOne(topicGroup.getId()));
    }

    @Test
    @Override
    public void testSave() throws Exception {
        TopicGroup topicGroup = new TopicGroup();
        topicGroup.setName("Existing topicGroup");
        assertNotNull("TopicGroup should be saved", topicGroupService.save(topicGroup));
    }

    @Test
    @Override
    public void testSaveAll() throws Exception {
        List<TopicGroup> agencyList = new ArrayList<>();
        TopicGroup topicGroup = new TopicGroup();
        topicGroup.setName("Test TopicGroup One");
        agencyList.add(topicGroup);

        topicGroup = new TopicGroup();
        topicGroup.setName("Test TopicGroup Two");
        agencyList.add(topicGroup);

        topicGroup = new TopicGroup();
        topicGroup.setName("Test TopicGroup Three");
        agencyList.add(topicGroup);

        topicGroupService.save(agencyList);

        assertEquals("Should return 3", topicGroupService.count(), 3L);
    }

    @Test(expected = ResourceNotFoundException.class)
    @Override
    public void testDelete() throws Exception {
        TopicGroup topicGroup = new TopicGroup();
        topicGroup.setName("Existing topicGroup");
        topicGroup = topicGroupService.save(topicGroup);
        topicGroupService.delete(topicGroup.getId());

        assertNull("Should return null", topicGroupService.findOne(topicGroup.getId()));
    }

    @Test(expected = ResourceNotFoundException.class)
    @Override
    public void testDeleteAll() throws Exception {
        List<TopicGroup> agencyList = new ArrayList<>();
        TopicGroup topicGroup = new TopicGroup();
        topicGroup.setName("Test TopicGroup One");
        agencyList.add(topicGroup);

        topicGroup = new TopicGroup();
        topicGroup.setName("Test TopicGroup Two");
        agencyList.add(topicGroup);

        topicGroup = new TopicGroup();
        topicGroup.setName("Test TopicGroup Three");
        agencyList.add(topicGroup);

        agencyList = topicGroupService.save(agencyList);
        topicGroupService.delete(agencyList);

        agencyList.forEach(a -> assertNull("Should return null", topicGroupService.findOne(a.getId())));

    }
}
