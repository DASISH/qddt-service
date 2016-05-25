package no.nsd.qddt.domain.code.web;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.ControllerWebIntegrationTest;
import no.nsd.qddt.domain.code.Code;
import no.nsd.qddt.domain.code.CodeService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Stig Norland
 */
public class ResponseDomainCategoryControllerTest extends ControllerWebIntegrationTest {

    @Autowired
    private CodeService entityService;

    private Code entity;

    @Override
    public void setup() {
        super.setup();

        super.getBeforeSecurityContext().createSecurityContext();

        entity = new Code();
        entity.setCodeValue("1");
        entity = entityService.save(entity);

        super.getBeforeSecurityContext().destroySecurityContext();

    }

    @Test
    public void testGet() throws Exception {
        mvc.perform(get("/code/"+entity.getId()).header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdate() throws Exception {
        entity.setCodeValue(entity.getCodeValue() + " edited");

        mvc.perform(post("/code").header("Authorization", "Bearer " + accessToken)
                .contentType(rest.getContentType())
                .content(rest.json(entity)))
                .andExpect(content().contentType(rest.getContentType()))
                .andExpect(jsonPath("$.codeValue", is(entity.getCodeValue())))
                .andExpect(jsonPath("$.changeKind", is(AbstractEntityAudit.ChangeKind.IN_DEVELOPMENT.toString())))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreate() throws Exception {
        Code aEntity = new Code();
        aEntity.setCodeValue("Posted entity");

        mvc.perform(post("/responsedomaincode/create").header("Authorization", "Bearer " + accessToken)
                .contentType(rest.getContentType())
                .content(rest.json(aEntity)))
                .andExpect(content().contentType(rest.getContentType()))
                .andExpect(jsonPath("$.codeValue", is(aEntity.getCodeValue())))
                .andExpect(jsonPath("$.changeKind", is(AbstractEntityAudit.ChangeKind.CREATED.toString())))
                .andExpect(status().isCreated());
    }

    @Test
    public void testDelete() throws Exception {
        mvc.perform(post("/responsedomaincode/delete/"+entity.getId()).header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());

        assertFalse("Instruction should no longer exist", entityService.exists(entity.getId()));
    }
}