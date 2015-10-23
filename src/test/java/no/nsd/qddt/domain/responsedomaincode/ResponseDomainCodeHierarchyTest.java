package no.nsd.qddt.domain.responsedomaincode;

import no.nsd.qddt.QDDT;
import no.nsd.qddt.domain.bcategory.Category;
import no.nsd.qddt.domain.bcategory.CategorySchemaType;
import no.nsd.qddt.domain.bcategory.CategoryService;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import no.nsd.qddt.domain.responsedomain.ResponseDomainService;
import no.nsd.qddt.domain.responsedomain.ResponseKind;
import no.nsd.qddt.utils.builders.CategoryBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QDDT.class)
public class ResponseDomainCodeHierarchyTest {

    @Autowired
    private ResponseDomainCodeService responseDomainCodeService;

    @Autowired
    private ResponseDomainService responseDomainService;

    @Autowired
    private CategoryService categoryService;

    private ResponseDomain responseDomain;

    private Category rootCategory;

    private static final String HASH_TAG_SEX = "#KJØNN";

    @Before
    public void setUp() {

        rootCategory = new CategoryBuilder().setCategory("Kjønn")
                .setType(CategorySchemaType.CATEGORY_GROUP)
                .setLabel(HASH_TAG_SEX).createCode();
        rootCategory.addChild(new CategoryBuilder().setCategory("KVINNE")
                .setType(CategorySchemaType.CATEGORY)
                .setLabel(HASH_TAG_SEX).createCode());
        rootCategory.addChild(new CategoryBuilder().setCategory("MANN")
                .setType(CategorySchemaType.CATEGORY)
                .setLabel(HASH_TAG_SEX).createCode());
        rootCategory.addChild(new CategoryBuilder().setCategory("TVEKJØNNET")
                .setType(CategorySchemaType.CATEGORY)
                .setLabel(HASH_TAG_SEX).createCode());

        categoryService.save(rootCategory);

        responseDomain = new ResponseDomain();
        responseDomain.setName("response domain Kjønn");
        responseDomain.setResponseKind(ResponseKind.Category);
        responseDomain.setCategory(rootCategory);
        responseDomain = responseDomainService.save(responseDomain);


        Integer i = 0;
        for(Category cat:rootCategory.getChildren()){
            ResponseDomainCode responseDomainCode = new ResponseDomainCode();
            responseDomainCode.setCategory(cat);
            responseDomainCode.setCategoryIndex(i++);
            responseDomainCode.setCodeValue(i.toString());
            responseDomainCode.setResponseDomain(responseDomain);
            responseDomainCodeService.save(responseDomainCode);
        }

    }

    @Test
    public void saveCodeAndResponseDomainToResponseDomainCodeTest() throws Exception {

//        codeService.findByHashTag(HASH_TAG_SEX).forEach(System.out::println);
//        int i = 0;
//        for (Category rootCategory : categoryService.findByTag(HASH_TAG_SEX)) {
//            ResponseDomainCode responseDomainCode = new ResponseDomainCode();
//            responseDomainCode.setCategoryIndex(i++);
//            responseDomainCode.setCategory(rootCategory);
//            responseDomainCode.setResponseDomain(responseDomain);
//            responseDomainCodeService.save(responseDomainCode);
//        }

        List<ResponseDomainCode> responseDomainCodeList = responseDomainCodeService.findByResponseDomainId(responseDomain.getId());
        assertEquals(3, responseDomainCodeList.size());
//        responseDomainCodeService.findByResponseDomainId(responseDomain.getId()).forEach(System.out::println);

        assertThat("responsDomainCode should not contain any items.", responseDomainCodeService.findByCategoryId(rootCategory.getId()).size(), is(0));

    }
}
