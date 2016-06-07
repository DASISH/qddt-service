package no.nsd.qddt.domain.responsedomain.web;

import no.nsd.qddt.QDDT;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.ControllerWebIntegrationTest;
import no.nsd.qddt.domain.HierarchyLevel;
import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.domain.category.CategoryService;
import no.nsd.qddt.domain.category.CategoryType;
import no.nsd.qddt.domain.code.Code;
import no.nsd.qddt.domain.embedded.ResponseCardinality;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import no.nsd.qddt.domain.responsedomain.ResponseDomainService;
import no.nsd.qddt.domain.responsedomain.ResponseKind;
import no.nsd.qddt.utils.builders.CategoryBuilder;
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

    @Autowired
    private CategoryService categoryService;

    private ResponseDomain rd;

    private String rootId;

    private Category saved;

    @Override
    public void setup() {
        super.setup();

        super.getBeforeSecurityContext().createSecurityContext();

        Category rootCategory = new CategoryBuilder()
                .setHierarchy(HierarchyLevel.GROUP_ENTITY)
                .setType(CategoryType.MIXED)
                .setLabel("Scale 1-5 with labels").createCategory();
        Category group = new CategoryBuilder().setName("SCALE1-5")
                .setHierarchy(HierarchyLevel.GROUP_ENTITY)
                .setType(CategoryType.SCALE)
                .setLabel("Scale 1-5 with labels").createCategory();
                group.setInputLimit("1","5");
        group.addChild(new CategoryBuilder()
                .setLabel("Very Happy").createCategory());
        group.addChild(new CategoryBuilder()
                .setLabel("Very Unhappy").createCategory());
        group.addChild(new CategoryBuilder()
                .setLabel("Happy").createCategory());
        group.addChild(new CategoryBuilder()
                .setLabel("Inbetween").createCategory());
        group.addChild(new CategoryBuilder()
                .setLabel("UnHappy").createCategory());

        rootCategory.addChild(group);

        group = new CategoryBuilder().setName("NO-ANSWER")
                .setHierarchy(HierarchyLevel.GROUP_ENTITY)
                .setType(CategoryType.MISSING_GROUP)
                .createCategory();
        group.addChild(new CategoryBuilder().setName("NA")
                .setType(CategoryType.CATEGORY)
                .setLabel("N/A").createCategory());
        group.addChild(new CategoryBuilder().setName("DONT_KNOW")
                .setType(CategoryType.CATEGORY)
                .setLabel("Don't know").createCategory());
        group.addChild(new CategoryBuilder().setName("CANNOT")
                .setType(CategoryType.CATEGORY)
                .setLabel("Don't want to").createCategory());
        rootCategory.addChild(group);
        saved = categoryService.save(rootCategory);

        rootId = rootCategory.getId().toString();
        super.getBeforeSecurityContext().destroySecurityContext();

    }

    @Test
    public void testGet() throws Exception {
        mvc.perform(get("/responsedomain/"+rd.getId()).header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    // FIXME: 11.04.2016
    @Test
    public void testUpdate() throws Exception {

        String retval = mvc.perform(get("/category/"+ rootId).header("Authorization", "Bearer " + accessToken)).andReturn().getResponse().getContentAsString();

        rd = new ResponseDomain();
        rd.setManagedRepresentation(saved);
        rd.setResponseKind(ResponseKind.MIXED);
        rd.getManagedRepresentation().getAllChildrenFlatten().forEach( category -> category.setCode(new Code(rd,category,"test")));


        mvc.perform(post("/responsedomain").header("Authorization", "Bearer " + accessToken)
                .contentType(rest.getContentType())
                .content(rest.json(rd)))
                .andExpect(content().contentType(rest.getContentType()))
                .andExpect(jsonPath("$.name", is(rd.getName())))
                .andExpect(jsonPath("$.changeKind", is(AbstractEntityAudit.ChangeKind.IN_DEVELOPMENT.toString())))
                .andExpect(status().isOk());
    }

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

    @Test
    public void testFind() throws Exception {
        mvc.perform(get("/responsedomain/page/search?ResponseKind=Code").header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());

    }

}