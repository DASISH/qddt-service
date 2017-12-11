package no.nsd.qddt.domain.universe.web;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.universe.Universe;
import no.nsd.qddt.domain.universe.audit.UniverseAuditService;
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
@RequestMapping(value = "/audit/universe", produces = MediaType.APPLICATION_JSON_VALUE)
public class UniverseAuditController {

    private final UniverseAuditService auditService;

    @Autowired
    public UniverseAuditController(UniverseAuditService service) {
        this.auditService = service;
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Revision<Long, Universe> getLastRevision(@PathVariable("id") UUID id) {
        return auditService.findLastChange(id);
    }

    @RequestMapping(value = "/{id}/{revision}", method = RequestMethod.GET)
    public Revision<Long, Universe> getByRevision(@PathVariable("id") UUID id, @PathVariable("revision") Long revision) {
        return auditService.findRevision(id, revision);
    }

    @RequestMapping(value = "/{id}/all", method = RequestMethod.GET)
    public HttpEntity<PagedResources<Revision<Long, Universe>>> allProjects(
            @PathVariable("id") UUID id,
            @RequestParam(value = "ignorechangekinds",
                    defaultValue = "IN_DEVELOPMENT,UPDATED_HIERARCHY_RELATION,UPDATED_PARENT,UPDATED_CHILD")
                    Collection<AbstractEntityAudit.ChangeKind> changekinds,
            Pageable pageable, PagedResourcesAssembler assembler) {

        Page<Revision<Long, Universe>> entities = auditService.findRevisionByIdAndChangeKindNotIn(id,changekinds, pageable);
        return new ResponseEntity<>(assembler.toResource(entities), HttpStatus.OK);
    }
}