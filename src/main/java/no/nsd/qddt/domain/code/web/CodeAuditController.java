package no.nsd.qddt.domain.code.web;

import no.nsd.qddt.domain.code.Code;
import no.nsd.qddt.domain.code.audit.CodeAuditService;
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

import java.util.UUID;

/**
 * @author Stig Norland
 */
@RestController
@RequestMapping(value = "/audit/code/", produces = MediaType.APPLICATION_JSON_VALUE)
public class CodeAuditController {

    private CodeAuditService auditService;

    @Autowired
    public CodeAuditController(CodeAuditService service) {
        this.auditService = service;
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Revision<Integer, Code> getLastRevision(@PathVariable("id") UUID id) {
        return auditService.findLastChange(id);
    }

    @RequestMapping(value = "/{id}/{revision}", method = RequestMethod.GET)
    public Revision<Integer, Code> getByRevision(@PathVariable("id") UUID id, @PathVariable("revision") Integer revision) {
        return auditService.findRevision(id, revision);
    }

    @RequestMapping(value = "/{id}/all", method = RequestMethod.GET)
    public HttpEntity<PagedResources<Revision<Integer, Code>>> allProjects(
            @PathVariable("id") UUID id, Pageable pageable, PagedResourcesAssembler assembler) {

        Page<Revision<Integer, Code>> entities = auditService.findRevisions(id, pageable);
        return new ResponseEntity<>(assembler.toResource(entities), HttpStatus.OK);
    }
}