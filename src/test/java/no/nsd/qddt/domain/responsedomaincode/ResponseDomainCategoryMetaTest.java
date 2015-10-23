package no.nsd.qddt.domain.responsedomaincode;

import no.nsd.qddt.QDDT;
import no.nsd.qddt.domain.bcategory.Category;
import no.nsd.qddt.domain.bcategory.CategoryService;
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
    private ResponseDomainService responseDomainService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ResponseDomainCodeService responseDomainCodeService;

    private ResponseDomain r1,r2;
    private Category c1,c2;

    @Before
    public void setUp() {

        r1 = responseDomainService.save(new ResponseDomain());
        r2 = responseDomainService.save(new ResponseDomain());

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
        List<ResponseDomainCode> rdcs = responseDomainCodeService.findByResponseDomainId(r1.getId());
        assertEquals("Expected two elements!", rdcs.size(), 2);
    }
}
