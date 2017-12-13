package no.nsd.qddt.domain.concept;

import no.nsd.qddt.domain.AbstractServiceTest;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.questionItem.QuestionItemService;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

//import no.nsd.qddt.domain.question.Question;

/**
 * Created by Dag Østgulen Heradstveit.
 */
public class ConceptServiceTest  extends AbstractServiceTest {

    @Autowired
    private ConceptService conceptService;

    @Autowired
    private ConceptRepository conceptRepository;

//    @Autowired
//    private QuestionService questionService;

    @Autowired
    private QuestionItemService questionItemService;

    @Before
    public void setup() {
        super.setup();
super.setBaseRepositories(conceptRepository);
    }

    @Test
    @Override
    public void testCount() throws Exception {
        Concept concept = new Concept();
        concept.setName("Test Concept One");
        conceptService.save(concept);

        concept = new Concept();
        concept.setName("Test Concept Two");
        conceptService.save(concept);

        concept = new Concept();
        concept.setName("Test Concept Three");
        conceptService.save(concept);

        assertThat("Should be three", conceptService.count(), is(3L));
    }

    @Test
    @Override
    public void testExists() throws Exception {
        Concept concept = new Concept();
        concept.setName("Existing concept");
        concept = conceptService.save(concept);
        assertTrue("Concept should exist", conceptService.exists(concept.getId()));
    }

    @Test
    @Override
    public void testFindOne() throws Exception {
        Concept concept = new Concept();
        concept.setName("Existing concept");
        concept = conceptService.save(concept);
        assertNotNull("Concept should not be null", conceptService.findOne(concept.getId()));
    }

    @Test
    @Override
    public void testSave() throws Exception {
        Concept concept = new Concept();
        concept.setName("Existing concept");
        assertNotNull("Concept should be saved", conceptService.save(concept));
    }

    @Test
    @Override
    public void testSaveAll() throws Exception {
        List<Concept> agencyList = new ArrayList<>();
        Concept concept = new Concept();
        concept.setName("Test Concept One");
        agencyList.add(concept);

        concept = new Concept();
        concept.setName("Test Concept Two");
        agencyList.add(concept);

        concept = new Concept();
        concept.setName("Test Concept Three");
        agencyList.add(concept);

        conceptService.save(agencyList);

        assertEquals("Should return 3", conceptService.count(), 3L);
    }

    @Test(expected = ResourceNotFoundException.class)
    @Override
    public void testDelete() throws Exception {
        Concept concept = new Concept();
        concept.setName("Existing concept");
        concept = conceptService.save(concept);
        conceptService.delete(concept.getId());

        assertNull("Should return null", conceptService.findOne(concept.getId()));
    }

    @Test(expected = ResourceNotFoundException.class)
    @Override
    public void testDeleteAll() throws Exception {
        List<Concept> agencyList = new ArrayList<>();
        Concept concept = new Concept();
        concept.setName("Test Concept One");
        agencyList.add(concept);

        concept = new Concept();
        concept.setName("Test Concept Two");
        agencyList.add(concept);

        concept = new Concept();
        concept.setName("Test Concept Three");
        agencyList.add(concept);

        agencyList = conceptService.save(agencyList);
        conceptService.delete(agencyList);

        agencyList.forEach(a -> assertNull("Should return null", conceptService.findOne(a.getId())));

    }

    @Test
    public void testAddQuestion() throws Exception {
        QuestionItem questionItem = new QuestionItem();
        questionItem.setQuestion("What???");

        Concept concept = new Concept();
        concept.setName("FIRST");
        concept = conceptService.save(concept);
        concept.addQuestionItem(questionItem);

        Concept savedConcept = conceptService.save(concept);

//        assertEquals("they are equal" ,savedConcept.getQuestionItems(),concept.getQuestionItems());

    }

}