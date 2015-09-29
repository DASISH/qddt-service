package no.nsd.qddt.controller;

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
public class ConceptController extends AbstractAuditController<Concept,UUID> {


    @Autowired
    public ConceptController(ConceptService service){ super(service);}

}
