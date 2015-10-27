package no.nsd.qddt.domain.category;

import no.nsd.qddt.domain.AbstractServiceTest;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by Dag Østgulen Heradstveit.
 */
public class CategoryServiceTest extends AbstractServiceTest {
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired    
    private CategoryRepository categoryRepository;

    @Before
    public void setup() {
        super.setBaseRepositories(categoryRepository);
    }

    @Test
    @Override
    public void testCount() throws Exception {
        Category category = new Category();
        category.setName("Test Code One");
        categoryService.save(category);

        category = new Category();
        category.setName("Test Code Two");
        categoryService.save(category);

        category = new Category();
        category.setName("Test Code Three");
        categoryService.save(category);


        assertThat("Should be three", categoryService.count(), is(3L));
    }

    @Test
    @Override
    public void testExists() throws Exception {
        Category category = new Category();
        category.setName("Existing category");
        category = categoryService.save(category);
        assertTrue("Code should exist", categoryService.exists(category.getId()));
    }

    @Test
    @Override
    public void testFindOne() throws Exception {
        Category category = new Category();
        category.setName("Existing category");
        category = categoryService.save(category);
        assertNotNull("Code should not be null", categoryService.findOne(category.getId()));
    }

    @Test
    @Override
    public void testSave() throws Exception {
        Category category = new Category();
        category.setName("Existing category");
        assertNotNull("Code should be saved", categoryService.save(category));
    }

    @Test
    @Override
    public void testSaveAll() throws Exception {
        Category parent = new Category();
        parent.setCategorySchemaType(CategorySchemaType.CATEGORY_GROUP);
        parent.setName("test category");

        Category category = new Category();
        category.setCategorySchemaType(CategorySchemaType.CATEGORY);
        category.setName("Few");
        parent.addChild(category);

        category = new Category();
        category.setCategorySchemaType(CategorySchemaType.CATEGORY);
        category.setName("More");
        parent.addChild(category);

        category = new Category();
        category.setCategorySchemaType(CategorySchemaType.CATEGORY);
        category.setName("Many");
        parent.addChild(category);

        categoryService.save(parent);

        category =  categoryRepository.findOne(parent.getId());

        assertEquals("Should be equal", parent,category);
        assertEquals("Should return 4",  4L,categoryService.count());

        categoryRepository.delete(category);
        assertEquals("Should return 3", 3L, categoryService.count());

    }

    @Test(expected = ResourceNotFoundException.class)
    @Override
    public void testDelete() throws Exception {
        Category category = new Category();
        category.setName("Existing category");
        category = categoryService.save(category);
        categoryService.delete(category.getId());

        assertNull("Should return null", categoryService.findOne(category.getId()));
    }

    @Test(expected = ResourceNotFoundException.class)
    @Override
    public void testDeleteAll() throws Exception {
        List<Category> categoryList = new ArrayList<>();
        Category category = new Category();
        category.setName("Test Code One");
        categoryList.add(category);

        category = new Category();
        category.setName("Test Code Two");
        categoryList.add(category);

        category = new Category();
        category.setName("Test Code Three");
        categoryList.add(category);

        categoryList = categoryService.save(categoryList);
        categoryService.delete(categoryList);

        categoryList.forEach(a -> assertNull("Should return null", categoryService.findOne(a.getId())));

    }

}
