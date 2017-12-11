package no.nsd.qddt.domain.concept.web;


import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.concept.audit.ConceptAuditService;
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
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

/**
 * @author Stig Norland
 */

@RestController
@RequestMapping(value = "/audit/concept", produces = MediaType.APPLICATION_JSON_VALUE)
public class ConceptAuditController {

    private final ConceptAuditService auditService;

    @Autowired
    public ConceptAuditController(ConceptAuditService service) {
        this.auditService = service;
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Revision<Long, Concept> getLastRevision(@PathVariable("id") UUID id) {
        return auditService.findLastChange(id);
    }

    @RequestMapping(value = "/{id}/{revision}", method = RequestMethod.GET)
    public Revision<Long, Concept> getByRevision(@PathVariable("id") UUID id, @PathVariable("revision") Long revision) {
        return auditService.findRevision(id, revision);
    }

    @RequestMapping(value = "/{id}/all", method = RequestMethod.GET)
    public HttpEntity<PagedResources<Revision<Long, Concept>>> allProjects(
            @PathVariable("id") UUID id,
            @RequestParam(value = "ignorechangekinds",defaultValue = "IN_DEVELOPMENT,UPDATED_HIERARCHY_RELATION,UPDATED_PARENT,UPDATED_CHILD") Collection<AbstractEntityAudit.ChangeKind> changekinds,
            Pageable pageable, PagedResourcesAssembler assembler) {

        Page<Revision<Long, Concept>> entities = auditService.findRevisionsByChangeKindNotIn(id,changekinds, pageable);

        return new ResponseEntity<>(assembler.toResource(entities), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/allinclatest", method = RequestMethod.GET)
    public HttpEntity<PagedResources<Revision<Long, Concept>>> allIncludinglatest(
            @PathVariable("id") UUID id,
            @RequestParam(value = "ignorechangekinds",defaultValue = "IN_DEVELOPMENT,UPDATED_HIERARCHY_RELATION,UPDATED_PARENT,UPDATED_CHILD") Collection<AbstractEntityAudit.ChangeKind> changekinds,
            Pageable pageable, PagedResourcesAssembler assembler) {

        Page<Revision<Long, Concept>> entities = auditService.findRevisionsByChangeKindIncludeLatest(id,changekinds, pageable);

        return new ResponseEntity<>(assembler.toResource(entities), HttpStatus.OK);
    }



}