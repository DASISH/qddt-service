package no.nsd.qddt.domain.responsedomaincode;

import no.nsd.qddt.QDDT;
import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.domain.category.CategoryService;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import no.nsd.qddt.domain.responsedomain.ResponseDomainService;
import no.nsd.qddt.utils.builders.CategoryBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QDDT.class)
public class ResponseDomainCategoryHierarchyTest {

    @Autowired
    private ResponseDomainCodeService responseDomainCodeService;

    @Autowired
    private ResponseDomainService responseDomainService;

    @Autowired
    private CategoryService categoryService;

    private ResponseDomain responseDomain;

    private Category category;

    private static final String HASH_TAG_SEX = "#KJØNN";
    private static final String HASH_TAG_CAR = "#BIL";

    @Before
    public void setUp() {

        category = categoryService.save(new CategoryBuilder().setCategory("Opel").setLabel(HASH_TAG_CAR).createCode());
        categoryService.save(new CategoryBuilder().setCategory("KVINNE").setLabel(HASH_TAG_SEX).createCode());
        categoryService.save(new CategoryBuilder().setCategory("MANN").setLabel(HASH_TAG_SEX).createCode());
        categoryService.save(new CategoryBuilder().setCategory("TVEKJØNNET").setLabel(HASH_TAG_SEX).createCode());


        responseDomain = new ResponseDomain();
        responseDomain.setName("response domain Kjønn");
        responseDomain = responseDomainService.save(responseDomain);

    }

    @Test
    public void saveCodeAndResponseDomainToResponseDomainCodeTest() throws Exception {

//        codeService.findByHashTag(HASH_TAG_SEX).forEach(System.out::println);
        int i = 0;
        for (Category category : categoryService.findByHashTag(HASH_TAG_SEX)) {
            ResponseDomainCode responseDomainCode = new ResponseDomainCode();
            responseDomainCode.setCategoryIndex(i++);
            responseDomainCode.setCategory(category);
            responseDomainCode.setResponseDomain(responseDomain);
            responseDomainCodeService.save(responseDomainCode);
        }

        assertEquals(responseDomainCodeService.findByResponseDomainId(responseDomain.getId()).size(), 3);
//        responseDomainCodeService.findByResponseDomainId(responseDomain.getId()).forEach(System.out::println);

        assertThat("responsDomainCode should not contain any items.", responseDomainCodeService.findByCategoryId(category.getId()).size(), is(0));

    }
}
