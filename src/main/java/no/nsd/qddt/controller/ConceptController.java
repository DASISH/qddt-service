package no.nsd.qddt.controller;

import no.nsd.qddt.domain.Concept;
import no.nsd.qddt.service.ConceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Stig Norland
 */
@RestController
@RequestMapping("/concept")
public class ConceptController {

    private ConceptService conceptService;

    @Autowired
    public ConceptController(ConceptService conceptService){
        this.conceptService = conceptService;
    }

    @RequestMapping(value = "/{id}/all", method = RequestMethod.GET)
    public HttpEntity<PagedResources<Concept>> getThread(
            @PathVariable("id") Long id,Pageable pageable, PagedResourcesAssembler assembler)
    {
        Page<Concept> comments = conceptService.findSiblingsPageable(id, pageable);
        return new ResponseEntity<>(assembler.toResource(comments), HttpStatus.OK);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Concept getOne(@PathVariable("id") Long id) {
        return conceptService.findById(id);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Concept create(Concept concept) {
        return conceptService.save(concept);
    }

}
