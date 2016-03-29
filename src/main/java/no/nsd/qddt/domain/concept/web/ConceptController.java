package no.nsd.qddt.domain.concept.web;

import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.concept.ConceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */
@RestController
@RequestMapping("/concept")
public class ConceptController {

    private ConceptService conceptService;

    @Autowired
    public ConceptController(ConceptService conceptService) {
        this.conceptService = conceptService;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Concept get(@PathVariable("id") UUID id) {
        return conceptService.findOne(id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Concept update(@RequestBody Concept concept) {
        return conceptService.save(concept);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Concept create(@RequestBody Concept concept) {
        return conceptService.save(concept);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create/{parentId}", method = RequestMethod.POST)
    public Concept create(@RequestBody Concept concept,@PathVariable("parentId") UUID parentId) {
        Concept parent = conceptService.findOne(parentId);
        concept.setParent(parent);
        return conceptService.save(concept);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public void delete(@PathVariable("id") UUID id) {
        conceptService.delete(id);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public HttpEntity<PagedResources<Concept>> getAll(Pageable pageable, PagedResourcesAssembler assembler) {
        Page<Concept> concepts = conceptService.findAllPageable(pageable);
        return new ResponseEntity<>(assembler.toResource(concepts), HttpStatus.OK);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page/by-topicgroup/{topicId}", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public HttpEntity<PagedResources<Concept>> getbyTopicId(@PathVariable("topicId") UUID id, Pageable pageable, PagedResourcesAssembler assembler) {
        Page<Concept> concepts = conceptService.findByTopicGroupPageable(id,pageable);
        return new ResponseEntity<>(assembler.toResource(concepts), HttpStatus.OK);
    }

}
