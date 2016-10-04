package no.nsd.qddt.domain.surveyprogram.web;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.surveyprogram.SurveyProgram;
import no.nsd.qddt.domain.surveyprogram.audit.SurveyProgramAuditService;
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

import java.util.*;

/**
 * @author Dag Østgulen Heradstveit
 */
@RestController
@RequestMapping(value = "/audit/surveyprogram/")
public class SurveyProgramAuditController {

    private SurveyProgramAuditService service;

    @Autowired
    public SurveyProgramAuditController(SurveyProgramAuditService service) {
        this.service = service;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Revision<Integer, SurveyProgram> findLastRevisionById(@PathVariable("id") UUID id) {
        return service.findLastChange(id);
    }

    @RequestMapping(value = "/{id}/{revision}", method = RequestMethod.GET)
    public Revision<Integer, SurveyProgram> findByRevision(@PathVariable("id") UUID id, @PathVariable("revision") Integer revision) {
        return service.findRevision(id, revision);
    }

    @RequestMapping(value = "/{id}/list", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public HttpEntity<PagedResources<Revision<Integer, SurveyProgram>>> list(
            @PathVariable("id") UUID id, Pageable pageable, PagedResourcesAssembler assembler){

//        Collection<AbstractEntityAudit.ChangeKind> changeKinds = new ArrayList<>();
//        changeKinds.add(AbstractEntityAudit.ChangeKind.IN_DEVELOPMENT);

        Page<Revision<Integer, SurveyProgram>> revisions = service.findRevisions(id, pageable);
        return new ResponseEntity<>(assembler.toResource(revisions), HttpStatus.OK);
    }

//    @RequestMapping(value = "/{id}/filteredlist", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
//    public HttpEntity<PagedResources<Revision<Integer, SurveyProgram>>> list(
//            @PathVariable("id") UUID id, @PathVariable("changeKinds") String[]changes, Pageable pageable, PagedResourcesAssembler assembler){
//
//        Collection<AbstractEntityAudit.ChangeKind> changeKinds = new ArrayList<>();
//        for (String change:changes) {
//            changeKinds.add(AbstractEntityAudit.ChangeKind.valueOf(change));
//        }
//
//        Page<Revision<Integer, SurveyProgram>> revisions = service.findRevisionByIdAndChangeKindNotIn(id,changeKinds , pageable);
//        return new ResponseEntity<>(assembler.toResource(revisions), HttpStatus.OK);
//    }

}