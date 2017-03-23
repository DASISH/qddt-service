package no.nsd.qddt.domain.publication;

import no.nsd.qddt.domain.AbstractServiceTest;
import no.nsd.qddt.domain.publicationstatus.PublicationStatus;
import no.nsd.qddt.domain.publicationstatus.PublicationStatusJsonListView;
import no.nsd.qddt.domain.publicationstatus.PublicationStatusService;
import no.nsd.qddt.domain.question.audit.QuestionAuditService;
import org.hibernate.envers.Audited;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public class PublicationServiceTest extends AbstractServiceTest {

    @Autowired
    private PublicationRepository repository;

    @Autowired
    private PublicationService service;

    @Autowired
    private PublicationStatusService statusService;


    List<PublicationStatusJsonListView> pubstat = null;

    @Before
    public void setup() {
        super.setup();
        pubstat =  statusService.findAll();
        super.setBaseRepositories(repository);
    }

    @Override
    public void testCount() throws Exception {

    }

    @Override
    public void testExists() throws Exception {

    }

    @Override
    public void testFindOne() throws Exception {

    }

    @Test
    @Override
    public void testSave() throws Exception {
        Publication publication = new Publication();
        publication.setName("TEST");
        publication.setPurpose("TEST PURPOSE");
        publication.setStatus(pubstat.get(2).getStatus());
        List<PublicationElement> pubelements = new ArrayList<>();
        pubelements.add(new PublicationElement(ElementKind.QUESTION_ITEM, UUID.fromString("fe107534-b071-41d3-bafc-71cf5cf716d6"),420));
        pubelements.add(new PublicationElement(ElementKind.QUESTION_ITEM, UUID.fromString("635e2eac-91c2-4f1a-b6cf-3478621194c6"),418));
        pubelements.add(new PublicationElement(ElementKind.QUESTION_ITEM, UUID.fromString("081bb8ff-b800-416a-b851-612ee7c04b32"),417));
        publication.setPublicationElements(pubelements);
        service.save(publication);

    }

    @Override
    public void testSaveAll() throws Exception {

    }

    @Override
    public void testDelete() throws Exception {

    }

    @Override
    public void testDeleteAll() throws Exception {

    }
}
