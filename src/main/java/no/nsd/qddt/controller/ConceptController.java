package no.nsd.qddt.controller;

import no.nsd.qddt.domain.Concept;
import no.nsd.qddt.service.ConceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Stig Norland
 */
@RestController
@RequestMapping("/concept")
public class ConceptController extends AbstractAuditController<Concept> {


    @Autowired
    public ConceptController(ConceptService service){ super(service);}

}
