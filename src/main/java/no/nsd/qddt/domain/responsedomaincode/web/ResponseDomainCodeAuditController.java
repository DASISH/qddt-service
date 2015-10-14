package no.nsd.qddt.domain.responsedomaincode.web;

import no.nsd.qddt.domain.responsedomaincode.ResponseDomainCode;
import no.nsd.qddt.domain.responsedomaincode.audit.ResponseDomainCodeAuditService;
import no.nsd.qddt.domain.study.Study;
import no.nsd.qddt.domain.study.audit.StudyAuditService;
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
@RequestMapping(value = "/audit/responsedomaincode/", produces = MediaType.APPLICATION_JSON_VALUE)
public class ResponseDomainCodeAuditController {

    private ResponseDomainCodeAuditService auditService;

    @Autowired
    public ResponseDomainCodeAuditController(ResponseDomainCodeAuditService service) {
        this.auditService = service;
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Revision<Integer, ResponseDomainCode> getLastRevision(@PathVariable("id") UUID id) {
        return auditService.findLastChange(id);
    }

    @RequestMapping(value = "/{id}/{revision}", method = RequestMethod.GET)
    public Revision<Integer, ResponseDomainCode> getByRevision(@PathVariable("id") UUID id, @PathVariable("revision") Integer revision) {
        return auditService.findRevision(id, revision);
    }

    @RequestMapping(value = "/{id}/all", method = RequestMethod.GET)
    public HttpEntity<PagedResources<Revision<Integer, ResponseDomainCode>>> allProjects(
            @PathVariable("id") UUID id, Pageable pageable, PagedResourcesAssembler assembler) {

        Page<Revision<Integer, ResponseDomainCode>> entities = auditService.findRevisions(id, pageable);
        return new ResponseEntity<>(assembler.toResource(entities), HttpStatus.OK);
    }
}