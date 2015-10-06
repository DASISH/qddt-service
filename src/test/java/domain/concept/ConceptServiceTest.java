package domain.concept;

import no.nsd.qddt.QDDT;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.concept.ConceptService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Dag Østgulen Heradstveit.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QDDT.class)
public class ConceptServiceTest {

    @Autowired
    private ConceptService conceptService;

    private Concept concept;

    @Before
    public void setup() {
        concept = new Concept();
        concept.setLabel("A concept in version 1");
        concept = conceptService.save(concept);
        concept.setLabel("A concept in version 2");
        concept = conceptService.save(concept);
        concept.setLabel("A concept in version 3");
        concept = conceptService.save(concept);
    }


    @Test
    public void testSaveSurveyWithAudit() throws Exception {
        concept = conceptService.findOne(concept.getId());

        // Find the last revision based on the entity id
        Revision<Integer, Concept> revision = conceptService.findLastChange(concept.getId());

        // Find all revisions based on the entity id as a page
        Page<Revision<Integer, Concept>> revisions = conceptService.findAllRevisionsPageable(
                concept.getId(), new PageRequest(0, 10));
        assertThat(revisions.getNumberOfElements(), is(3));

        // Find all revisions
        Revisions<Integer, Concept> wrapper = new Revisions<>(revisions.getContent());
        assertThat(wrapper.getLatestRevision(), is(revision));
    }
}
