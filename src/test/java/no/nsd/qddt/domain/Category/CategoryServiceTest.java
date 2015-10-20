package no.nsd.qddt.domain.Category;

import no.nsd.qddt.QDDT;
import no.nsd.qddt.domain.code.Code;
import no.nsd.qddt.domain.code.CodeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

/**
 * @author Stig Norland
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QDDT.class)
public class CategoryServiceTest  {

    @Autowired
    private CodeService codeService;


    @Autowired
    private  CategoryService categoryService;


    @Test
    public void testCategories() throws Exception {
        List<Code> codelist = new ArrayList<>();
        codelist.add(new Code("#TAG1","Cat1"));
        codelist.add(new Code("#TAG1", "Cat2"));
        codelist.add(new Code("#TAG1", "Cat3"));
        codelist = codeService.save(codelist);

        List<String> list1 = new ArrayList<>();
        codelist.forEach(c -> list1.add(c.getCategory()));

        List<String> list2 = categoryService.findAll().stream().map(Category::getName).collect(Collectors.toCollection(ArrayList<String>::new));

        assertEquals(list1, list2);

    }

}
