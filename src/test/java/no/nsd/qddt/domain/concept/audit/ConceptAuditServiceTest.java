package no.nsd.qddt.domain.concept.audit;

import no.nsd.qddt.domain.AbstractAuditServiceTest;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.concept.ConceptService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ConceptAuditServiceTest extends AbstractAuditServiceTest {

    @Autowired
    private ConceptService conceptService;

    @Autowired
    private ConceptAuditService conceptAuditService;

    private Concept concept;

    @Before
    public void setup() {
        super.setup();
        concept = new Concept();
        concept.setLabel("A concept in version 1");
        concept = conceptService.save(concept);
        concept.setLabel("A concept in version 2");
        concept = conceptService.save(concept);
        concept.setLabel("A concept in version 3");
        concept = conceptService.save(concept);
    }


    @Test
    public void testSaveSurveyWithAudit() {
        concept = conceptService.findOne(concept.getId());

        // Find the last revision based on the entity id
        Revision<Integer, Concept> revision = conceptAuditService.findLastChange(concept.getId());

        // Find all revisions based on the entity id as a page
        Page<Revision<Integer, Concept>> revisions = conceptAuditService.findRevisions(
                concept.getId(), new PageRequest(0, 10));
        assertThat(revisions.getNumberOfElements(), is(3));

        // Find all revisions
        Revisions<Integer, Concept> wrapper = Revisions.of(revisions.getContent());
        assertThat(wrapper.getLatestRevision().getRevisionNumber() , is(revision.getRevisionNumber()));
    }
}
