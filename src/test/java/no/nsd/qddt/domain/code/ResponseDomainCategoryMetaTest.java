package no.nsd.qddt.domain.code;

import no.nsd.qddt.QDDT;
import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.domain.category.CategoryService;
import no.nsd.qddt.domain.category.CategoryType;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import no.nsd.qddt.domain.responsedomain.ResponseDomainService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.SortedSet;

import static org.junit.Assert.assertEquals;

/**
 * Created by Dag Østgulen Heradstveit.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QDDT.class)
public class ResponseDomainCategoryMetaTest {

    @Autowired
    private ResponseDomainService responseDomainService;

    @Autowired
    private CategoryService categoryService;

    private ResponseDomain r1,r2;
    private Category c1,c2;

    @Before
    public void setUp() throws Exception {

        // Setting Code before ManagedRepresentation is assigned.
        r1 = new ResponseDomain();
        r1.setName("R1");

        c1 = categoryService.save(new Category("C1","c1"));
        c1.setCode(new Code(r1,"1"));
        r1.setManagedRepresentation(c1);
        r1 =responseDomainService.save(r1);


        //Setting ManagedRepresentation before Codes are assigned and Category/ManagedRepresentation is saved after it is assigned a responsedomain
        r2 = new ResponseDomain();
        r2.setName("R2");

        c2 = new Category("PARENT","parent");
        c2.setCategoryType(CategoryType.LIST);
        r2.setManagedRepresentation(c2);
        c2.getChildren().addAll(Arrays.asList(new Category("child1","Child1"),new Category("child2","Child2"),new Category("child3","Child3")));
        c2 = categoryService.save(c2);
        c2.getChildren().forEach(c-> c.setCode(new Code(r2,c.getName().substring(5))));

        r2 = responseDomainService.save(r2);

    }

    /**
     * Make sure only 1 of 3 possible questions are giving us htis here.
     * @throws Exception
     */
    @Test
    public void findByQuestionTest() throws Exception {
        ResponseDomain R2 = responseDomainService.findOne(r2.getId());
        Category root = R2.getManagedRepresentation();


        assertEquals("Expected 1 elements!", root.getChildren().get(0).getCode().getCodeValue(), "2");
    }
}
