package no.nsd.qddt.domain.aategory.web;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.ControllerWebIntegrationTest;
import no.nsd.qddt.domain.aategory.Category;
import no.nsd.qddt.domain.aategory.CategoryService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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

        category = new Category();
        category.setLabel("Test category");
        category.setName("A test category");
        category = categoryService.save(category);

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
}
