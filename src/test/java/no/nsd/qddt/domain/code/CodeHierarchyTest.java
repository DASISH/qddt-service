package no.nsd.qddt.domain.code;

import no.nsd.qddt.QDDT;
import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.domain.HierarchyLevel;
import no.nsd.qddt.domain.category.CategoryService;
import no.nsd.qddt.domain.category.CategoryType;
import no.nsd.qddt.domain.question.Question;
import no.nsd.qddt.domain.question.QuestionService;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import no.nsd.qddt.domain.responsedomain.ResponseDomainService;
import no.nsd.qddt.domain.responsedomain.ResponseKind;
import no.nsd.qddt.utils.BeforeSecurityContext;
import no.nsd.qddt.utils.builders.CategoryBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QDDT.class)
public class CodeHierarchyTest {

    @Autowired
    private CodeService codeService;

    @Autowired
    private ResponseDomainService responseDomainService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private QuestionService questionService;

    private ResponseDomain responseDomain;

    private Category rootCategory;

    private Question question;

    private BeforeSecurityContext beforeSecurityContext;

    @Autowired
    private AuthenticationManager authenticationManager;


    @Before
    public void setUp() {

        this.beforeSecurityContext = new BeforeSecurityContext(authenticationManager);
        this.beforeSecurityContext.createSecurityContext();

        question = new Question();
        question.setName("what is your gender");
        question = questionService.save(question);

                //Create a categorySchema
        rootCategory = new CategoryBuilder().setName("GENDER")
                .setHierarchy(HierarchyLevel.GROUP_ENTITY)
                .setType(CategoryType.LIST)
                .setLabel("Gender").createCategory();
        rootCategory.addChild(new CategoryBuilder().setName("FEMALE")
                .setLabel("Female").createCategory());
        rootCategory.addChild(new CategoryBuilder().setName("MAN")
                .setLabel("Man").createCategory());
        rootCategory.addChild(new CategoryBuilder().setName("TRANSGENDER")
                .setLabel("Transgender").createCategory());
        categoryService.save(rootCategory);


        // Create a responsdomain with a categoryschema.
        responseDomain = new ResponseDomain();
        responseDomain.setName("Sex mandatory answer");
        responseDomain.setResponseKind(ResponseKind.List);
        responseDomain.setCategory(rootCategory);
        responseDomain = responseDomainService.save(responseDomain);


        // add codevalues to the responsdomain/categoryschema instanse
        Integer i = 0;
//        for(Category cat:rootCategory.getAllChildrenFlatten()){
//            Code code = new Code();
//            code.setCategory(cat);
//            code.setCodeValue(i.toString());
//            codeService.save(code);
//        }

    }

    @Test
    public void findByResponseDomainAndCategoryTest() throws Exception {

        List<Code> codeList = codeService.findByResponseDomainId(question.getId());
        assertEquals(3, codeList.size());
        assertThat("responsDomainCode should not contain any items.", codeService.findByCategoryId(rootCategory.getId()).size(), is(0));

    }


}