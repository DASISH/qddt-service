package no.nsd.qddt.domain.category;

import no.nsd.qddt.domain.AbstractServiceTest;
import no.nsd.qddt.domain.HierarchyLevel;
import no.nsd.qddt.domain.responsedomaincode.ResponseDomainCodeService;
import no.nsd.qddt.exception.ResourceNotFoundException;
import no.nsd.qddt.utils.builders.CategoryBuilder;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private ResponseDomainCodeService responseDomainCodeService;


    @Before
    public void setup() {
        super.setBaseRepositories(categoryRepository);
    }

    @Test
    @Override
    public void testCount() throws Exception {
        Category rootCategory = new CategoryBuilder().setName("KJØNN")
                .setHierarchy(HierarchyLevel.GROUP_ENTITY)
                .setType(CategoryType.MULTIPLE_SINGLE)
                .setLabel("Kjønn").createCategory();
        rootCategory.addChild(new CategoryBuilder().setName("KVINNE")
                .setLabel("Kvinne").createCategory());
        rootCategory.addChild(new CategoryBuilder().setName("MANN")
                .setLabel("Mann").createCategory());
        rootCategory.addChild(new CategoryBuilder().setName("TVEKJØNNET")
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
    @Override
    public void testSaveAll() throws Exception {
        Category parent = new CategoryBuilder().setName("KJØNN")
                .setHierarchy(HierarchyLevel.GROUP_ENTITY)
                .setType(CategoryType.MULTIPLE_SINGLE)
                .setLabel("Kjønn").createCategory();
        parent.addChild(new CategoryBuilder().setName("KVINNE")
                .setLabel("Kvinne").createCategory());
        parent.addChild(new CategoryBuilder().setName("MANN")
                .setLabel("Mann").createCategory());
        parent.addChild(new CategoryBuilder().setName("TVEKJØNNET")
                .setLabel("Transperson").createCategory());

        categoryService.save(parent);

        Category category =  categoryRepository.findOne(parent.getId());

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


    @Test
    public void testFindByCategoryType(){
        Category rootCategory = new CategoryBuilder().setName("KJØNN")
                .setHierarchy(HierarchyLevel.GROUP_ENTITY)
                .setType(CategoryType.MULTIPLE_SINGLE)
                .setLabel("Kjønn").createCategory();
        rootCategory.addChild(new CategoryBuilder().setName("KVINNE")
                .setLabel("Kvinne").createCategory());
        rootCategory.addChild(new CategoryBuilder().setName("MANN")
                .setLabel("Mann").createCategory());
        rootCategory.addChild(new CategoryBuilder().setName("TVEKJØNNET")
                .setLabel("Transperson").createCategory());
        categoryService.save(rootCategory);


        Page<Category> rootList= categoryService.findByCategoryTypeAndNameLike(CategoryType.MULTIPLE_SINGLE, "%", new PageRequest(0, 20));
        assertEquals("Skal bare være et element i listen",  1L,rootList.getTotalElements());

    }

    @Test
    public void testfindByName(){
        Category rootCategory = new CategoryBuilder().setName("KJØNN")
                .setHierarchy(HierarchyLevel.GROUP_ENTITY)
                .setType(CategoryType.MULTIPLE_SINGLE)
                .setLabel("Kjønn").createCategory();
        rootCategory.addChild(new CategoryBuilder().setName("KVINNE")
                .setLabel("Kvinne").createCategory());
        rootCategory.addChild(new CategoryBuilder().setName("MANN")
                .setLabel("Mann").createCategory());
        rootCategory.addChild(new CategoryBuilder().setName("TVEKJØNNET")
                .setLabel("Transperson").createCategory());
        categoryService.save(rootCategory);

        List<Category> list = categoryService.findByNameLike("%nne%");
        assertEquals("Skal ha to elementer", 2L, list.size());

    }

    @Test
    public void testfindGroupByName(){
        Category rootCategory = new CategoryBuilder().setName("KJØNN")
                .setHierarchy(HierarchyLevel.GROUP_ENTITY)
                .setType(CategoryType.MULTIPLE_SINGLE)
                .setLabel("Kjønn").createCategory();
        rootCategory.addChild(new CategoryBuilder().setName("KVINNE")
                .setLabel("Kvinne").createCategory());
        rootCategory.addChild(new CategoryBuilder().setName("MANN")
                .setLabel("Mann").createCategory());
        rootCategory.addChild(new CategoryBuilder().setName("TVEKJØNNET")
                .setLabel("Transperson").createCategory());
        categoryService.save(rootCategory);

        Page<Category> rootList= categoryService.findGroupByName("%", new PageRequest(0, 20));
        assertEquals("Skal bare være et element i listen",  1L,rootList.getTotalElements());
    }

    @Transactional
    @Test
    public void testfindRootLevelByName(){
        Category rootCategory = new CategoryBuilder().setName("KJØNN")
                .setHierarchy(HierarchyLevel.GROUP_ENTITY)
                .setType(CategoryType.MULTIPLE_SINGLE)
                .setLabel("Kjønn").createCategory();
        rootCategory.addChild(new CategoryBuilder().setName("KVINNE")
                .setLabel("Kvinne").createCategory());
        rootCategory.addChild(new CategoryBuilder().setName("MANN")
                .setLabel("Mann").createCategory());
        rootCategory.addChild(new CategoryBuilder().setName("TVEKJØNNET")
                .setLabel("Transperson").createCategory());
        rootCategory = categoryService.save(rootCategory);

        Category group = new CategoryBuilder().setName("IKKE SVAR")
                .setHierarchy(HierarchyLevel.GROUP_ENTITY)
                .setType(CategoryType.MULTIPLE_SINGLE)
                .setLabel("NA Svar").createCategory();
        group.addChild(new CategoryBuilder().setName("VET IKKE")
                .setLabel("Vet ikke").createCategory());
        group.addChild(new CategoryBuilder().setName("VIL IKKE SVARE")
                .setLabel("Vil ikke svare").createCategory());
        group = categoryService.save(group);

        Category root = new CategoryBuilder().setName("SVAR SETT KJØNN")
                .setHierarchy(HierarchyLevel.ROOT_ENTITY)
                .setType(CategoryType.MULTIPLE_SINGLE)
                .setLabel("Kjønn").createCategory();
        root.addChild(rootCategory);
        root.addChild(group);
        categoryService.save(root);

        Page<Category> rootList= categoryService.findRootLevelByName( "%", new PageRequest(0, 20));
        assertEquals("Should be 1 element in list",  1L,rootList.getTotalElements());
        assertEquals("Should be 5 Grandchildren elements", 5L, rootList.getContent().get(0).getGrandChildren().size());

    }

    @Transactional
    @Test
    public void testHierarchAndCategory(){

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

        Page<Category> page =categoryService.findByHierarchyAndCategoryAndName(
                HierarchyLevel.ENTITY,
                CategoryType.TEXT,
                "%", new PageRequest(0, 20));

        assertEquals("Should be 2 element in list",  2L,page.getTotalElements());
        assertEquals("Should be 0 Grandchildren elements", 0L, page.getContent().get(0).getGrandChildren().size());

        page =categoryService.findByHierarchyAndCategoryAndName(
                HierarchyLevel.GROUP_ENTITY,
                CategoryType.MULTIPLE_MULTIPLE,
                "%", new PageRequest(0, 20));

        assertEquals("Should be 1 element in list",  1L,page.getTotalElements());
        assertEquals("Should be 3 Grandchildren elements", 3L, page.getContent().get(0).getGrandChildren().size());
    }
}
