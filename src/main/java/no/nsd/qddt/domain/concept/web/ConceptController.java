package no.nsd.qddt.domain.concept.web;

import no.nsd.qddt.domain.AbstractController;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.concept.ConceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author Stig Norland
 */
@RestController
@RequestMapping("/concept")
public class ConceptController extends AbstractController<Concept,UUID> {

    private ConceptService conceptService;

    @Autowired
    public ConceptController(ConceptService conceptService) {
        super(conceptService);
        this.conceptService = conceptService;
    }

}
