package no.nsd.qddt.domain.category;

import no.nsd.qddt.domain.AbstractServiceTest;
import no.nsd.qddt.classes.exception.ResourceNotFoundException;
import no.nsd.qddt.classes.builders.CategoryBuilder;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
//    private CodeService codeService;


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

        Optional<Category> category =  categoryRepository.findById(parent.getId());

        assertEquals("Should be equal", parent.hashCode(),category.hashCode());
        assertEquals("Should return 4",  4L,categoryService.count());

        categoryRepository.delete(category.get());        // no deletions, have children ( CascadeType.PERSIST)
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


//        Page<Category> rootList= categoryService.findBy(CategoryType.LIST, "%", new PageRequest(0, 20));
//        assertEquals("Skal bare være et element i listen",  1L,rootList.getTotalElements());

    }

    @Test
    public void testfindByName(){
        Category rootCategory = new CategoryBuilder()
                .setHierarchy(HierarchyLevel.GROUP_ENTITY)
                .setType(CategoryType.LIST)
                .setLabel("Kjønn4").createCategory();
        rootCategory.getChildren().add(new CategoryBuilder()
                .setType(CategoryType.CATEGORY)
                .setLabel("Kvinne4").createCategory());
        rootCategory.getChildren().add(new CategoryBuilder()
                .setType(CategoryType.CATEGORY)
                .setLabel("Mann4").createCategory());
        rootCategory.getChildren().add(new CategoryBuilder()
                .setType(CategoryType.CATEGORY)
                .setLabel("Transperson4").createCategory());
        categoryService.save(rootCategory);

//        Page<Category> list = categoryService.find( "%nne%", new PageRequest(0, 20));
//        assertEquals("Skal ha to elementer", 2L, list.getTotalElements());

    }

    @Test
    public void testfindGroupByName(){
        Category rootCategory = new CategoryBuilder()
                .setHierarchy(HierarchyLevel.GROUP_ENTITY)
                .setType(CategoryType.LIST)
                .setLabel("Kjønn5").createCategory();
        rootCategory.getChildren().add(new CategoryBuilder()
                .setType(CategoryType.CATEGORY)
                .setLabel("Kvinne5").createCategory());
        rootCategory.getChildren().add(new CategoryBuilder()
                .setType(CategoryType.CATEGORY)
                .setLabel("Mann5").createCategory());
        rootCategory.getChildren().add(new CategoryBuilder()
                .setType(CategoryType.CATEGORY)
                .setLabel("Transperson5").createCategory());
        categoryService.save(rootCategory);

        Page<Category> rootList= categoryService.findBy("GROUP_ENTITY", "LIST", "%", "%","%", new PageRequest(0, 20));
        assertEquals("Skal bare være et element i listen",  1L,rootList.getTotalElements());
    }

    @Transactional
    @Test
    public void testfindRootLevelByName(){
        Category rootCategory = new CategoryBuilder()
                .setHierarchy(HierarchyLevel.GROUP_ENTITY)
                .setType(CategoryType.LIST)
                .setLabel("Kjønn6").createCategory();
        rootCategory.getChildren().add(new CategoryBuilder()
                .setType(CategoryType.CATEGORY)
                .setLabel("Kvinne6").createCategory());
        rootCategory.getChildren().add(new CategoryBuilder()
                .setType(CategoryType.CATEGORY)
                .setLabel("Mann6").createCategory());
        rootCategory.getChildren().add(new CategoryBuilder()
                .setType(CategoryType.CATEGORY)
                .setLabel("Transperson6").createCategory());
        rootCategory = categoryService.save(rootCategory);

        Category group = new CategoryBuilder()
                .setHierarchy(HierarchyLevel.GROUP_ENTITY)
                .setType(CategoryType.MISSING_GROUP)
                .setLabel("NA Svar").createCategory();
        group.getChildren().add(new CategoryBuilder()
                .setType(CategoryType.CATEGORY)
                .setLabel("Vet ikke").createCategory());
        group.getChildren().add(new CategoryBuilder()
                .setType(CategoryType.CATEGORY)
                .setLabel("Vil ikke svare").createCategory());
        group = categoryService.save(group);

        Category root = new CategoryBuilder()
                .setHierarchy(HierarchyLevel.GROUP_ENTITY)
                .setType(CategoryType.LIST)
                .setLabel("Kjønn-liste2").createCategory();

        root.getChildren().add(rootCategory);
        root.getChildren().add(group);
        root = categoryService.save(root);

        Page<Category> rootList= categoryService.findBy("GROUP_ENTITY", "LIST", "Kjønn%", "%", "%",new PageRequest(0, 20));

        assertEquals("Should be 1 element in list",  1L,rootList.getTotalElements());
        assertEquals( root, rootList );
//        assertEquals("Should be 5 Grandchildren elements", 5L, rootList.getContent().get(0).getAllChildrenFlatten().size());

    }

}
