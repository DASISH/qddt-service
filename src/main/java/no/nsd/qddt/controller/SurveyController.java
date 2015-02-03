package no.nsd.qddt.controller;

import no.nsd.qddt.domain.Survey;
import no.nsd.qddt.service.SurveyService;
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
@RequestMapping("/survey")
public class SurveyController implements BaseController<Survey> {

    @Autowired
    private SurveyService surveyService;


    @Override
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Survey> getAll() {
        return surveyService.findAll();
    }

    @Override
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public HttpEntity<PagedResources<Survey>> getAll(Pageable pageable, PagedResourcesAssembler assembler) {

        Page<Survey> instances = surveyService.findAll(pageable);
        return new ResponseEntity<>(assembler.toResource(instances), HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Survey getOne(@PathVariable("id") Long id) {
            return surveyService.findById(id);
        }

    @Override
    @RequestMapping(value = "/UUID/{id}", method = RequestMethod.GET)
    public Survey getOne(@PathVariable("id") UUID id) {
        return surveyService.findById(id);
    }

    @Override
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Survey create(Survey instance) {
        return surveyService.save(instance);
    }

    @Override
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public void delete(Survey instance) {
        surveyService.delete(instance);
    }
}
