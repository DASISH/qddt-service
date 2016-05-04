package no.nsd.qddt.domain.responsedomaincode;

import no.nsd.qddt.QDDT;
import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.domain.category.CategoryService;
import no.nsd.qddt.domain.question.Question;
import no.nsd.qddt.domain.question.QuestionService;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import no.nsd.qddt.domain.responsedomain.ResponseDomainService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Dag Østgulen Heradstveit.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QDDT.class)
public class ResponseDomainCategoryMetaTest {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ResponseDomainCodeService responseDomainCodeService;

    private Question r1,r2;
    private Category c1,c2;

    @Before
    public void setUp() {

        r1 = questionService.save(new Question());
        r2 = questionService.save(new Question());

        c1 = categoryService.save(new Category());
        c2 = categoryService.save(new Category());

        responseDomainCodeService.save(new ResponseDomainCode(0, r1, c1));
        responseDomainCodeService.save(new ResponseDomainCode(0, r2, c2));
        responseDomainCodeService.save(new ResponseDomainCode(0, r1, c2));
    }

    @Test
    public void findByInstrumentTest() throws Exception {
        List<ResponseDomainCode> rdcs = responseDomainCodeService.findByCategoryId(c1.getId());
        assertEquals("Expected one element!", rdcs.size(), 1);
    }

    /**
     * Make sure only 1 of 3 possible questions are giving us htis here.
     * @throws Exception
     */
    @Test
    public void findByQuestionTest() throws Exception {
        List<ResponseDomainCode> rdcs = responseDomainCodeService.findByQuestionId(r1.getId());
        assertEquals("Expected two elements!", rdcs.size(), 2);
    }
}
