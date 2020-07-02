package no.nsd.qddt.domain.universe.web;

import java.util.Collection;
import java.util.UUID;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.universe.Universe;
import no.nsd.qddt.domain.universe.audit.UniverseAuditService;

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


    @GetMapping(value = "/{id}")
    public Revision<Integer, Universe> getLastRevision(@PathVariable("id") UUID id) {
        return auditService.findLastChange(id);
    }

    // @JsonView(View.Audit.class)
    @GetMapping(value = "/{id}/{revision}")
    public Revision<Integer, Universe> getByRevision(@PathVariable("id") UUID id, @PathVariable("revision") Integer revision) {
        return auditService.findRevision(id, revision);
    }

    // @JsonView(View.Audit.class)
    @GetMapping(value = "/{id}/all")
    public HttpEntity<PagedResources<Revision<Integer, Universe>>> allProjects(
            @PathVariable("id") UUID id,
            @RequestParam(value = "ignorechangekinds",
                    defaultValue = "IN_DEVELOPMENT,UPDATED_HIERARCHY_RELATION,UPDATED_PARENT,UPDATED_CHILD")
                    Collection<AbstractEntityAudit.ChangeKind> changekinds,
            Pageable pageable, PagedResourcesAssembler assembler) {

        Page<Revision<Integer, Universe>> entities = auditService.findRevisionByIdAndChangeKindNotIn(id,changekinds, pageable);
        return new ResponseEntity<>(assembler.toResource(entities), HttpStatus.OK);
    }
}