package no.nsd.qddt.domain.category;

import no.nsd.qddt.domain.AbstractServiceTest;
import no.nsd.qddt.exception.ResourceNotFoundException;
import no.nsd.qddt.utils.builders.CategoryBuilder;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired



    @Before
    public void setup() {
        super.setup();
        super.setBaseRepositories(categoryRepository);
    }

    @Test
    @Override
    public void testCount() throws Exception {
        Category rootCategory = new CategoryBuilder()
                .setHierarchy(HierarchyLevel.GROUP_ENTITY)
                .setType(CategoryType.LIST)
                .setLabel("Kjønn1").createCategory();
        rootCategory.getChildren().add( new CategoryBuilder()
                .setType(CategoryType.CATEGORY)
                .setLabel("Kvinne1").createCategory());
        rootCategory.getChildren().add(new CategoryBuilder()
                .setType(CategoryType.CATEGORY)
                .setLabel("Mann1").createCategory());
        rootCategory.getChildren().add(new CategoryBuilder()
                .setType(CategoryType.CATEGORY)
                .setLabel("Transperson").createCategory());
        categoryService.save(rootCategory);
        assertThat("Should be four", categoryService.count(), is(4L));
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
    public void testSaveAll() throws Exception {
        Category parent = new CategoryBuilder()
                .setHierarchy(HierarchyLevel.GROUP_ENTITY)
                .setType(CategoryType.LIST)
                .setLabel("Kjønn2").createCategory();
        parent.getChildren().add(new CategoryBuilder()
                .setType(CategoryType.CATEGORY)
                .setLabel("Kvinne2").createCategory());
        parent.getChildren().add(new CategoryBuilder()
                .setType(CategoryType.CATEGORY)
                .setLabel("Mann2").createCategory());
        parent.getChildren().add(new CategoryBuilder()
                .setType(CategoryType.CATEGORY)
                .setLabel("Transperson2").createCategory());

        parent = categoryService.save(parent);

        Category category =  categoryRepository.findOne(parent.getId());

        assertEquals("Should be equal", parent.hashCode(),category.hashCode());
        assertEquals("Should return 4",  4L,categoryService.count());

        categoryRepository.delete(category);        // no deletions, have children ( CascadeType.PERSIST)
        assertEquals("Should return 4", 4L, categoryService.count());

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




    @Test
    public void testFindByCategoryType(){
        Category rootCategory = new CategoryBuilder()
                .setHierarchy(HierarchyLevel.GROUP_ENTITY)
                .setType(CategoryType.LIST)
                .setLabel("Kjønn3").createCategory();
        rootCategory.getChildren().add(new CategoryBuilder()
                .setType(CategoryType.CATEGORY)
                .setLabel("Kvinne3").createCategory());
        rootCategory.getChildren().add(new CategoryBuilder()
                .setType(CategoryType.CATEGORY)
                .setLabel("Mann3").createCategory());
        rootCategory.getChildren().add(new CategoryBuilder()
                .setType(CategoryType.CATEGORY)
                .setLabel("Transperson3").createCategory());
        categoryService.save(rootCategory);


        Page<Category> rootList= categoryService.findBy("GROUP_ENTITY","LIST", "%", "%", new PageRequest(0, 20));
        assertEquals("Skal bare være et element i listen",  1L,rootList.getTotalElements());

    }
}