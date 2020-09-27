package no.nsd.qddt.domain.publication;

import no.nsd.qddt.domain.AbstractServiceTest;
import no.nsd.qddt.domain.elementref.ElementKind;
import no.nsd.qddt.domain.elementref.ElementRefImpl;
import no.nsd.qddt.domain.publicationstatus.PublicationStatus;
import no.nsd.qddt.domain.publicationstatus.PublicationStatusService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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


    List<PublicationStatus> pubstat = null;

    @Before
    public void setup() {
        super.setup();
        pubstat =  statusService.findAll().stream().collect(Collectors.toList());
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
        publication.setStatus(pubstat.get(2));
        List<ElementRefImpl> pubelements = new ArrayList<>();
        pubelements.add(new ElementRefImpl( ElementKind.QUESTION_ITEM, UUID.fromString("fe107534-b071-41d3-bafc-71cf5cf716d6"),420));
        pubelements.add(new ElementRefImpl(ElementKind.QUESTION_ITEM, UUID.fromString("635e2eac-91c2-4f1a-b6cf-3478621194c6"),418));
        pubelements.add(new ElementRefImpl(ElementKind.QUESTION_ITEM, UUID.fromString("081bb8ff-b800-416a-b851-612ee7c04b32"),417));
        publication.setPublicationElements(pubelements);
        service.save(publication);

    }



    @Override
    public void testDelete() throws Exception {

    }

}
