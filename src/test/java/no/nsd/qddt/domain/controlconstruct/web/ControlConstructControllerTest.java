package no.nsd.qddt.domain.controlconstruct.web;

import no.nsd.qddt.domain.classes.AbstractEntityAudit;
import no.nsd.qddt.domain.ControllerWebIntegrationTest;
import no.nsd.qddt.domain.controlconstruct.ControlConstructService;
import no.nsd.qddt.domain.controlconstruct.pojo.QuestionConstruct;
import no.nsd.qddt.domain.classes.elementref.ElementRefQuestionItem;
import no.nsd.qddt.domain.questionitem.QuestionItem;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Stig Norland
 */
public class ControlConstructControllerTest extends ControllerWebIntegrationTest {

    @Autowired
    private ControlConstructService entityService;

    private QuestionConstruct entity;

    @Override
    public void setup() {
        super.setup();

        super.getBeforeSecurityContext().createSecurityContext();

        entity = new QuestionConstruct();
        QuestionItem item = new QuestionItem();
        entity.setQuestionItemRef(new ElementRefQuestionItem( item));
        entity.setName("A test entity");
        entity = entityService.save(entity);

        super.getBeforeSecurityContext().destroySecurityContext();

    }

    @Test
    public void testGet() throws Exception {
        mvc.perform(get("/controlconstruct/"+entity.getId()).header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdate() throws Exception {
        entity.setName(entity.getName() + " edited");

        mvc.perform(post("/controlconstruct").header("Authorization", "Bearer " + accessToken)
                .contentType(rest.getContentType())
                .content(rest.json(entity)))
                .andExpect(content().contentType(rest.getContentType()))
                .andExpect(jsonPath("$.name", is(entity.getName())))
                .andExpect(jsonPath("$.changeKind", is( AbstractEntityAudit.ChangeKind.IN_DEVELOPMENT.toString())))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreate() throws Exception {
        QuestionConstruct aEntity = new QuestionConstruct();
        aEntity.setName("Posted entity");
        aEntity.setQuestionItemRef(new ElementRefQuestionItem( new QuestionItem()));


        mvc.perform(MockMvcRequestBuilders.fileUpload("/controlconstruct/create")
                .file("file", "Test Content".getBytes())
                .header("Authorization", "Bearer " + accessToken)
                .content(rest.json(aEntity))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void testDelete() throws Exception {
        mvc.perform(post("/controlconstruct/delete/"+entity.getId()).header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());

        assertFalse("Instruction should no longer exist", entityService.exists(entity.getId()));
    }
}
