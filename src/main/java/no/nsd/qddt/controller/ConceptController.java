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

import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@RestController
@RequestMapping("/concept")
public class ConceptController  implements BaseController<Concept> {

    private ConceptService conceptService;

    @Autowired
    public ConceptController(ConceptService conceptService){
        this.conceptService = conceptService;
    }


    @Override
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Concept> getAll() {
        return null;
    }

    @Override
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public HttpEntity<PagedResources<Concept>> getAll(Pageable pageable, PagedResourcesAssembler assembler) {

        Page<Concept> comments = conceptService.findAll(pageable);
        return new ResponseEntity<>(assembler.toResource(comments), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Concept getOne(@PathVariable("id") Long id) {
        return conceptService.findById(id);
    }

    @Override
    @RequestMapping(value = "/UUID{id}/all", method = RequestMethod.GET)
    public Concept getOne(@PathVariable("id")UUID id) {
        return null;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Concept create(Concept concept) {
        return conceptService.save(concept);
    }

    @Override
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public void delete(Concept instance) {
        conceptService.delete(instance);
    }

}
