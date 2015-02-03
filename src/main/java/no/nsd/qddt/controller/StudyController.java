package no.nsd.qddt.controller;

import no.nsd.qddt.domain.Study;
import no.nsd.qddt.service.StudyService;
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
 * @author Dag Østgulen Heradstveit
 */
@RestController
@RequestMapping("/study")
public class StudyController implements BaseController<Study> {

    private StudyService studyService;

    @Autowired
    public StudyController(StudyService studyService) {
        this.studyService = studyService;
    }


    @Override
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Study> getAll() {
        return studyService.findAll();
    }


    @Override
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public HttpEntity<PagedResources<Study>> getAll(Pageable pageable, PagedResourcesAssembler assembler) {

        Page<Study> questions = studyService.findAll(pageable);
        return new ResponseEntity<>(assembler.toResource(questions), HttpStatus.OK);
    }


    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Study getOne(@PathVariable("id") Long id) {
        return studyService.findById(id);
    }


    @Override
    @RequestMapping(value = "/UUID/{id}", method = RequestMethod.GET)
    public Study getOne(@PathVariable("id") UUID id) {
        return studyService.findById(id);
    }


    @Override
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Study create(Study instance) {
        return studyService.save(instance);
    }


    @Override
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public void delete(Study instance) {
        studyService.delete(instance);
    }
}