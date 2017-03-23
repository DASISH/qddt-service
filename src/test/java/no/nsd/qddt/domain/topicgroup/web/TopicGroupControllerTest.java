package no.nsd.qddt.domain.topicgroup.web;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.ControllerWebIntegrationTest;
import no.nsd.qddt.domain.study.Study;
import no.nsd.qddt.domain.study.StudyService;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import no.nsd.qddt.domain.topicgroup.TopicGroupService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Stig Norland
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
public class TopicGroupControllerTest extends ControllerWebIntegrationTest {

    @Autowired
    private TopicGroupService entityService;

    @Autowired
    private StudyService studyService;


    private TopicGroup entity;

    @Override
    public void setup() {
        super.setup();
        super.getBeforeSecurityContext().createSecurityContext();
        entity = new TopicGroup();
        entity.setName("A test entity");
        entity = entityService.save(entity);
        super.getBeforeSecurityContext().destroySecurityContext();

    }

    @Test
    public void testGet() throws Exception {
        mvc.perform(get("/topicgroup/"+entity.getId()).header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    //TODO // FIXME: 11.04.2016 IN_DEVELOPMENT blir ikke satt

    @Test
    public void testUpdate() throws Exception {
        entity.setName(entity.getName() + " edited");

        mvc.perform(post("/topicgroup").header("Authorization", "Bearer " + accessToken)
                .contentType(rest.getContentType())
                .content(rest.json(entity)))
                .andExpect(content().contentType(rest.getContentType()))
                .andExpect(jsonPath("$.name", is(entity.getName())))
               // .andExpect(jsonPath("$.changeKind", is(AbstractEntityAudit.ChangeKind.IN_DEVELOPMENT.toString())))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreate() throws Exception {

        Study study =  studyService.save(new Study());

        TopicGroup aEntity = new TopicGroup();
        aEntity.setName("Posted entity");

        mvc.perform(post("/topicgroup/create/"+ study.getId()).header("Authorization", "Bearer " + accessToken)
                .contentType(rest.getContentType())
                .content(rest.json(aEntity)))
                .andExpect(content().contentType(rest.getContentType()))
                .andExpect(jsonPath("$.name", is(aEntity.getName())))
                .andExpect(jsonPath("$.changeKind", is(AbstractEntityAudit.ChangeKind.CREATED.toString())))
                .andExpect(status().isCreated());
    }

    @Test
    public void testDelete() throws Exception {
        mvc.perform(post("/topicgroup/delete/"+entity.getId()).header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());

        assertFalse("Instruction should no longer exist", entityService.exists(entity.getId()));
    }
}
