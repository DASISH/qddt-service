package no.nsd.qddt.domain.responsedomain.web;

import no.nsd.qddt.QDDT;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.ControllerWebIntegrationTest;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import no.nsd.qddt.domain.responsedomain.ResponseDomainService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
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
@WebIntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QDDT.class)
public class ResponseDomainControllerTest extends ControllerWebIntegrationTest {

    @Autowired
    private ResponseDomainService entityService;

    private ResponseDomain entity;

    @Override
    public void setup() {
        super.setup();

        super.getBeforeSecurityContext().createSecurityContext();

        entity = new ResponseDomain();
        entity.setName("A test entity");
        entity = entityService.save(entity);

        super.getBeforeSecurityContext().destroySecurityContext();

    }

    @Test
    public void testGet() throws Exception {
        mvc.perform(get("/responsedomain/"+entity.getId()).header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    // FIXME: 11.04.2016
//    @Test
//    public void testUpdate() throws Exception {
//        entity.setName(entity.getName() + " edited");
//
//        mvc.perform(post("/responsedomain").header("Authorization", "Bearer " + accessToken)
//                .contentType(rest.getContentType())
//                .content(rest.json(entity)))
//                .andExpect(content().contentType(rest.getContentType()))
//                .andExpect(jsonPath("$.name", is(entity.getName())))
//                .andExpect(jsonPath("$.changeKind", is(AbstractEntityAudit.ChangeKind.IN_DEVELOPMENT.toString())))
//                .andExpect(status().isOk());
//    }

    @Test
    public void testCreate() throws Exception {
        ResponseDomain aEntity = new ResponseDomain();
        aEntity.setName("Posted entity");

        mvc.perform(post("/responsedomain/create").header("Authorization", "Bearer " + accessToken)
                .contentType(rest.getContentType())
                .content(rest.json(aEntity)))
                .andExpect(content().contentType(rest.getContentType()))
                .andExpect(jsonPath("$.name", is(aEntity.getName())))
                .andExpect(jsonPath("$.changeKind", is(AbstractEntityAudit.ChangeKind.CREATED.toString())))
                .andExpect(status().isCreated());
    }

    @Test
    public void testDelete() throws Exception {
        mvc.perform(post("/responsedomain/delete/"+entity.getId()).header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());

        assertFalse("Instruction should no longer exist", entityService.exists(entity.getId()));
    }
}