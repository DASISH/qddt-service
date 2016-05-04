package no.nsd.qddt.domain.responsedomain.web;

import no.nsd.qddt.QDDT;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.ControllerWebIntegrationTest;
import no.nsd.qddt.domain.HierarchyLevel;
import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.domain.category.CategoryService;
import no.nsd.qddt.domain.category.CategoryType;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import no.nsd.qddt.domain.responsedomain.ResponseDomainService;
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

    private ResponseDomain entity;

    @Override
    public void setup() {
        super.setup();

        super.getBeforeSecurityContext().createSecurityContext();

        Category rootCategory = new CategoryBuilder()
                .setHierarchy(HierarchyLevel.ROOT_ENTITY)
                .setType(CategoryType.MIXED)
                .setLabel("Scale 1-5 with labels").createCategory();
        Category group = new CategoryBuilder().setName("SCALE1-5")
                .setHierarchy(HierarchyLevel.GROUP_ENTITY)
                .setType(CategoryType.RANGE)
                .setLabel("Scale 1-5 with labels").createCategory();
        group.addChild(new CategoryBuilder().setName("BEGIN")
                .setType(CategoryType.CODE)
                .setLabel("Very Happy").createCategory());
        group.addChild(new CategoryBuilder().setName("END")
                .setLabel("Very Unhappy").createCategory());
        group.addChild(new CategoryBuilder().setType(CategoryType.LABEL)
                .setLabel("Happy").createCategory());
        group.addChild(new CategoryBuilder().setType(CategoryType.LABEL)
                .setLabel("Inbetween").createCategory());
        group.addChild(new CategoryBuilder().setType(CategoryType.LABEL)
                .setLabel("UnHappy").createCategory());

        rootCategory.addChild(group);

        group = new CategoryBuilder().setName("NO-ANSWER")
                .setHierarchy(HierarchyLevel.GROUP_ENTITY)
                .setType(CategoryType.MULTIPLE_SINGLE)
                .createCategory();
        group.addChild(new CategoryBuilder().setName("NA")
                .setType(CategoryType.CODE)
                .setLabel("N/A").createCategory());
        group.addChild(new CategoryBuilder().setName("DONT_KNOW")
                .setType(CategoryType.CODE)
                .setLabel("Don't know").createCategory());
        group.addChild(new CategoryBuilder().setName("CANNOT")
                .setType(CategoryType.CODE)
                .setLabel("Don't want to").createCategory());
        rootCategory.addChild(group);
        categoryService.save(rootCategory);

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