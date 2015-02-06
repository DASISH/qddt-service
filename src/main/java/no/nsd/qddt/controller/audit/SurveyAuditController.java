package no.nsd.qddt.controller.audit;

import no.nsd.qddt.domain.Survey;
import no.nsd.qddt.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Dag Østgulen Heradstveit
 */
@RestController
@RequestMapping(value = "/audit/survey/", produces = MediaType.APPLICATION_JSON_VALUE)
public class SurveyAuditController {

    private SurveyService surveyService;

    @Autowired
    public SurveyAuditController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Revision<Integer, Survey> getLastRevision(@PathVariable("id") Long id) {
        return surveyService.findLastChange(id);
    }

    @RequestMapping(value = "/{id}/{revision}", method = RequestMethod.GET)
    public Revision<Integer, Survey> getByRevision(@PathVariable("id") Long id, @PathVariable("revision") Integer revision) {
        return surveyService.findEntityAtRevision(id, revision);
    }

    @RequestMapping(value = "/{id}/all", method = RequestMethod.GET)
    public HttpEntity<PagedResources<Revision<Integer, Survey>>> allProjects(
            @PathVariable("id") Long id,Pageable pageable, PagedResourcesAssembler assembler){

        Page<Revision<Integer, Survey>> studies = surveyService.findAllRevisionsPageable(id, pageable);
        return new ResponseEntity<>(assembler.toResource(studies), HttpStatus.OK);
    }
}
