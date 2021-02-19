package no.nsd.qddt.domain.universe.web;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.universe.Universe;
import no.nsd.qddt.domain.universe.audit.UniverseAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
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
    public PagedModel<EntityModel<Revision<Integer, Universe>>> allProjects(
            @PathVariable("id") UUID id,
            @RequestParam(value = "ignorechangekinds",
                    defaultValue = "IN_DEVELOPMENT,UPDATED_HIERARCHY_RELATION,UPDATED_PARENT,UPDATED_CHILD")
                    Collection<AbstractEntityAudit.ChangeKind> changekinds,
            Pageable pageable, PagedResourcesAssembler<Revision<Integer, Universe>> assembler) {

        Page<Revision<Integer, Universe>> entities = auditService.findRevisionByIdAndChangeKindNotIn(id,changekinds, pageable);
        return assembler.toModel(entities);
    }
}
