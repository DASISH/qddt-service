package no.nsd.qddt.domain.topicgroup;

import no.nsd.qddt.domain.AbstractServiceTest;
import no.nsd.qddt.domain.study.Study;
import no.nsd.qddt.domain.study.StudyService;
import no.nsd.qddt.classes.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

import static org.junit.Assert.*;

/**
 * Created by Dag Østgulen Heradstveit.
 */
public class TopicGroupServiceTest extends AbstractServiceTest {

    @Autowired
    private TopicGroupService topicGroupService;

    @Autowired
    private StudyService studyService;

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

        assertEquals("Should be three",3L, topicGroupService.count());
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


    @Test(expected = ResourceNotFoundException.class)
    @Override
    public void testDelete() throws Exception {
        TopicGroup topicGroup = new TopicGroup();
        topicGroup.setName("Existing topicGroup");
        topicGroup = topicGroupService.save(topicGroup);
        topicGroupService.delete(topicGroup.getId());

        assertNull("Should return null", topicGroupService.findOne(topicGroup.getId()));
    }



    @Test
    public void findByStudyId() throws Exception {
        Study study1 = studyService.save(new Study());
        Study study2 = studyService.save(new Study());
        TopicGroup topic = new TopicGroup();
        topic.setStudy(study1);
        topicGroupService.save(topic);

        assertNotNull("Should contain the belonging topic", topicGroupService.findByStudyId(study1.getId()));
        assertEquals("Should not contain any topic", Collections.emptyList(), topicGroupService.findByStudyId(study2.getId()));
    }

    @Test
    public void findByStudyIdReverse() throws Exception {
        Study study1 = studyService.save(new Study());
        Study study2 = studyService.save(new Study());
        TopicGroup topic = new TopicGroup();
        study1.getTopicGroups().add(topic);
        studyService.save(study1);

        assertNotNull("Should contain the belonging topic", topicGroupService.findByStudyId(study1.getId()));
        assertEquals("Should not contain any topic", Collections.emptyList(), topicGroupService.findByStudyId(study2.getId()));
    }
}
