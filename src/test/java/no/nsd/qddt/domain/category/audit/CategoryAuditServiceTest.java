package no.nsd.qddt.domain.category.audit;

import no.nsd.qddt.domain.AbstractAuditServiceTest;
import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.domain.category.CategoryService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * @author Stig Norland
 */
public class CategoryAuditServiceTest extends AbstractAuditServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryAuditService categoryAuditService;

    private Category entity;

    @Before
    public void setUp() {
        super.setup();


        entity = categoryService.save(new Category());

        entity.setName("First");
        entity = categoryService.save(entity);
        entity.setName("Second");
        entity = categoryService.save(entity);
        entity.setName("Third");
        entity = categoryService.save(entity);
    }

    @Test
    public void testSaveSurveyWithAudit() {
        entity = categoryService.findOne(entity.getId());

        // Find the last revision based on the entity id
        categoryAuditService.findLastChange( entity.getId() );

        // Find all revisions based on the entity id as a page
        var revisions = categoryAuditService.findRevisions(
                entity.getId(), PageRequest.of(0, 10));

        var  wrapper = Revisions.of(revisions.getContent());

        assertEquals(wrapper.getLatestRevision().getEntity().getId(), entity.getId());
        assertThat(revisions.getNumberOfElements(), is(4));
    }

    @Test
    public void getAllRevisionsTest() {
        var revisions =
                categoryAuditService.findRevisions(entity.getId(), PageRequest.of(0, 20));

        assertEquals("Excepted four revisions.",
                revisions.getNumberOfElements(), 4);
    }

    @Test
    public void getfirstAndLastRevisionTest() {
        var  revision = categoryAuditService.findLastChange(entity.getId());

        assertEquals("Excepted initial ResponseDomain Object.",
                revision.getEntity().hashCode(), entity.hashCode());
        assertEquals("Expected Name to be 'Third'", revision.getEntity().getName(), "Third");

        revision = categoryAuditService.findFirstChange(entity.getId());

        assertEquals("Excepted initial ResponseDomain Object.",
                revision.getEntity().hashCode(), entity.hashCode());
        assertEquals("Expected Name to be 'Third'", revision.getEntity().getName(), "First");

    }
}
