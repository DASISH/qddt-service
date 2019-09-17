package no.nsd.qddt.domain.questionitem;


import no.nsd.qddt.domain.AbstractServiceTest;
//import no.nsd.qddt.domain.question.Question;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
/**
 * @author Stig Norland
 */

public class QuestionItemServiceTest  extends AbstractServiceTest {

    @Autowired
    private QuestionItemService service;

    @Autowired
    private QuestionItemRepository repository;


    @Before
    public void setup() {
        super.setup();
        QuestionItem qi = new QuestionItem();
        qi.setQuestion("test question 1 ?");
        service.save(qi);

        qi = new QuestionItem();
        qi.setQuestion("test question 2 ?");
        service.save(qi);

        qi = new QuestionItem();
        qi.setQuestion("test question 3 ?");
        service.save(qi);

        super.setBaseRepositories(repository);
    }

    @Override
    public void testCount() throws Exception {

    }

    @Override
    public void testExists() throws Exception {

    }

    @Test
    @Override
    public void testFindOne() throws Exception {
         Page result= service.findByNameOrQuestionOrResponseName("","%2%","",new PageRequest(0, 20));

        Assert.assertThat("Should be one", result.getTotalElements(),  Is.is(1L));
    }

    @Override
    public void testSave() throws Exception {

    }


    @Override
    public void testDelete() throws Exception {

    }




}
