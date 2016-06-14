package no.nsd.qddt.domain.code;

import no.nsd.qddt.QDDT;
import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.domain.category.CategoryService;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import no.nsd.qddt.domain.responsedomain.ResponseDomainService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

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

    @Autowired
    private CodeService codeService;

    private ResponseDomain r1,r2;
    private Category c1,c2;

    @Before
    public void setUp() throws Exception {

        r1 = new ResponseDomain();
        c1 = categoryService.save(new Category());
        r1.setManagedRepresentation(c1);
        r1 =responseDomainService.save(r1);
        codeService.save(new Code(c1,r1,"1"));


        r2 = new ResponseDomain();
        c2 = new Category();
        c2.setChildren(Arrays.asList(new Category("child","Child")));
        c2 = categoryService.save(c2);

        r2.setManagedRepresentation(c2);
        r2 = responseDomainService.save(r2);
        codeService.save(new Code(c1,r2,"2"));
        codeService.save(new Code(c2.getChildren().get(0),r1,"3"));

    }

    @Test
    public void findByInstrumentTest() throws Exception {
        List<Code> rdcs = codeService.findByCategoryId(c1.getId());
        assertEquals("Expected one element!", rdcs.size(), 1);
    }

    /**
     * Make sure only 1 of 3 possible questions are giving us htis here.
     * @throws Exception
     */
    @Test
    public void findByQuestionTest() throws Exception {
        Category root = responseDomainService.findOne(r1.getId()).getManagedRepresentation();

        assertEquals("Expected 1 elements!", root.getCode().getCodeValue(), "1");
    }
}
