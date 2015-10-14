package no.nsd.qddt.domain.instrument.web;

import no.nsd.qddt.domain.instrument.Instrument;
import no.nsd.qddt.domain.instrument.audit.InstrumentAuditService;
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
@RequestMapping(value = "/audit/instrument/", produces = MediaType.APPLICATION_JSON_VALUE)
public class InstrumentAuditController {

    private InstrumentAuditService auditService;

    @Autowired
    public InstrumentAuditController(InstrumentAuditService service) {
        this.auditService = service;
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Revision<Integer, Instrument> getLastRevision(@PathVariable("id") UUID id) {
        return auditService.findLastChange(id);
    }

    @RequestMapping(value = "/{id}/{revision}", method = RequestMethod.GET)
    public Revision<Integer, Instrument> getByRevision(@PathVariable("id") UUID id, @PathVariable("revision") Integer revision) {
        return auditService.findRevision(id, revision);
    }

    @RequestMapping(value = "/{id}/all", method = RequestMethod.GET)
    public HttpEntity<PagedResources<Revision<Integer, Instrument>>> allProjects(
            @PathVariable("id") UUID id, Pageable pageable, PagedResourcesAssembler assembler) {

        Page<Revision<Integer, Instrument>> entities = auditService.findRevisions(id, pageable);
        return new ResponseEntity<>(assembler.toResource(entities), HttpStatus.OK);
    }
}