package no.nsd.qddt.domain.concept.web;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.ControllerWebIntegrationTest;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.concept.ConceptService;
import no.nsd.qddt.domain.concept.audit.ConceptAuditService;
import no.nsd.qddt.domain.questionitem.QuestionItem;
import no.nsd.qddt.domain.questionitem.QuestionItemService;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import no.nsd.qddt.domain.topicgroup.TopicGroupService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//import no.nsd.qddt.domain.question.Question;

/**
 * @author Stig Norland
 */
public class ConceptControllerTest extends ControllerWebIntegrationTest {

    @Autowired
    private ConceptService entityService;

    @Autowired
    private ConceptAuditService entityAuditService;

    @Autowired
    private TopicGroupService topicGroupService;

    @Autowired
    private QuestionItemService questionService;

    private Concept entity;
    private TopicGroup topicGroup;

    @Override
    public void setup() {
        super.setup();

        super.getBeforeSecurityContext().createSecurityContext();
        entity = new Concept();
        entity.setName("A test entity");
        entity = entityService.save(entity);

        super.getBeforeSecurityContext().destroySecurityContext();

    }

    @Test
    public void testGet() throws Exception {
        mvc.perform(get("/concept/"+entity.getId()+"/listall").header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }


    @Test
    public void testGetAudit() throws Exception {
//        List<> test = entityAuditService.test(entity.getId(),(AbstractEntityAudit.ChangeKind.IN_DEVELOPMENT));
//        mvc.perform(get("/audit/concept/"+entity.getId()).header("Authorization", "Bearer " + accessToken))
//                .andExpect(status().isOk());
    }

    @Test
    public void testUpdate() throws Exception {
        entity.setName(entity.getName() + " edited");

        mvc.perform(post("/concept").header("Authorization", "Bearer " + accessToken)
                .contentType(rest.getContentType())
                .content(rest.json(entity)))
                .andExpect(content().contentType(rest.getContentType()))
                .andExpect(jsonPath("$.name", is(entity.getName())))
                .andExpect(jsonPath("$.changeKind", is( AbstractEntityAudit.ChangeKind.IN_DEVELOPMENT.toString())))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreate() throws Exception {
        TopicGroup topicGroup = new TopicGroup();
        topicGroup.setName("a module");
        topicGroup =  topicGroupService.save(topicGroup);

        Concept aEntity = new Concept();
        aEntity.setName("Posted entity");

        mvc.perform(post("/concept/create/by-topicgroup/" +topicGroup.getId()).header("Authorization", "Bearer " + accessToken)
                .contentType(rest.getContentType())
                .content(rest.json(aEntity)))
                .andExpect(content().contentType(rest.getContentType()))
                .andExpect(jsonPath("$.name", is(aEntity.getName())))
                .andExpect(jsonPath("$.changeKind", is( AbstractEntityAudit.ChangeKind.CREATED.toString())))
                .andExpect(status().isCreated());
    }

    @Test
    public void testDelete() throws Exception {
        mvc.perform(post("/concept/delete/"+entity.getId()).header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());

        assertFalse("Instruction should no longer exist", entityService.exists(entity.getId()));
    }



    @Test
    public void testAddQuestion() throws Exception {

        QuestionItem question = new QuestionItem();
        question.setQuestion("my precious");

        question = questionService.save(question);

        Concept concept = new Concept();
        concept.setName("FIRST");
        concept = entityService.save(concept);


        mvc.perform(get("/concept/combine?concept=" +concept.getId()+ "&question="+ question.getId() ).header("Authorization", "Bearer " + accessToken)
                .contentType(rest.getContentType())
                .content(rest.json(concept))).andReturn();

        concept =  entityService.findOne(concept.getId());
        assertThat("Should be one", concept.getConceptQuestionItems().size(), is(1));
    }
}
