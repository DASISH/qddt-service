package no.nsd.qddt.domain.category.web;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.ControllerWebIntegrationTest;
import no.nsd.qddt.domain.HierarchyLevel;
import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.domain.category.CategoryService;
import no.nsd.qddt.domain.category.CategoryType;
import no.nsd.qddt.utils.builders.CategoryBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Dag Heradstveit
 */
public class CategoryControllerTest extends ControllerWebIntegrationTest {

    @Autowired
    private CategoryService categoryService;

    private Category category;

    @Override
    public void setup() {
        super.setup();
        super.getBeforeSecurityContext().createSecurityContext();
        category = new Category();
        category.setLabel("Test category");
        category.setName("A test category");
        category = categoryService.save(category);


        Category rootCategory = new CategoryBuilder().setName("ROOT")
                .setHierarchy(HierarchyLevel.ROOT_ENTITY)
                .setType(CategoryType.MIXED)
                .setLabel("Root").createCategory();
        Category group = new CategoryBuilder().setName("GROUP1")
                .setHierarchy(HierarchyLevel.GROUP_ENTITY)
                .setLabel("Gruppe1").createCategory();
        group.addChild(new CategoryBuilder().setName("ENTITY1")
                .setLabel("ent1").createCategory());
        group.addChild(new CategoryBuilder().setName("ENTITY2")
                .setLabel("ent2").createCategory());
        rootCategory.addChild(group);

        group = new CategoryBuilder().setName("GROUP2")
                .setHierarchy(HierarchyLevel.GROUP_ENTITY)
                .setType(CategoryType.MULTIPLE_MULTIPLE)
                .setLabel("Grupp2").createCategory();
        group.addChild(new CategoryBuilder().setName("ENTITY3")
                .setType(CategoryType.MULTIPLE_MULTIPLE)
                .setLabel("1").createCategory());
        group.addChild(new CategoryBuilder().setName("ENTITY4")
                .setType(CategoryType.MULTIPLE_MULTIPLE)
                .setLabel("2").createCategory());
        group.addChild(new CategoryBuilder().setName("ENTITY5")
                .setType(CategoryType.MULTIPLE_MULTIPLE)
                .setLabel("3").createCategory());
        rootCategory.addChild(group);
        categoryService.save(rootCategory);

        super.getBeforeSecurityContext().destroySecurityContext();
    }

    @Test
    public void testGet() throws Exception {
        mvc.perform(get("/category/"+ category.getId()).header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdate() throws Exception {
        category.setName(category.getName() + " edited");

        mvc.perform(post("/category").header("Authorization", "Bearer " + accessToken)
                .contentType(rest.getContentType())
                .content(rest.json(category)))
                .andExpect(content().contentType(rest.getContentType()))
                .andExpect(jsonPath("$.name", is(category.getName())))
                .andExpect(jsonPath("$.changeKind", is(AbstractEntityAudit.ChangeKind.IN_DEVELOPMENT.toString())))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreate() throws Exception {
        Category aCategory = new Category();
        aCategory.setName("Posted category");

        mvc.perform(post("/category/create").header("Authorization", "Bearer " + accessToken)
                .contentType(rest.getContentType())
                .content(rest.json(aCategory)))
                .andExpect(content().contentType(rest.getContentType()))
                .andExpect(jsonPath("$.name", is(aCategory.getName())))
                .andExpect(jsonPath("$.changeKind", is(AbstractEntityAudit.ChangeKind.CREATED.toString())))
                .andExpect(status().isCreated());
    }

    @Test
    public void testDelete() throws Exception {
        mvc.perform(post("/category/delete/"+ category.getId()).header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());

        assertFalse("Code should no longer exist", categoryService.exists(category.getId()));
    }

    @Test
    public void getByLeaf() throws Exception {
        ResultActions action =mvc.perform(get("/category/page/by-leaf/%").header("Authorization", "Bearer " + accessToken));

        MvcResult result = action.andReturn();


        assertFalse("OK", result.getResolvedException() != null);
    }

    @Test
    public void getByGroup() throws Exception {
        ResultActions action =mvc.perform(get("/category/page/by-group/%").header("Authorization", "Bearer " + accessToken));

        MvcResult result = action.andReturn();


        assertFalse("OK", result.getResolvedException() != null);
    }

    @Test
    public void getByRoot() throws Exception {
        ResultActions action =mvc.perform(get("/category/page/by-root/%").header("Authorization", "Bearer " + accessToken));

        MvcResult result = action.andReturn();


        assertFalse("OK", result.getResolvedException() != null);
    }

}
