package no.nsd.qddt.domain.instruction.web;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.instruction.Instruction;
import no.nsd.qddt.domain.instruction.audit.InstructionAuditService;
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
@RequestMapping(value = "/audit/instruction", produces = MediaType.APPLICATION_JSON_VALUE)
public class InstructionAuditController {

    private final InstructionAuditService auditService;

    @Autowired
    public InstructionAuditController(InstructionAuditService service) {
        this.auditService = service;
    }


    // @JsonView(View.Audit.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Revision<Integer, Instruction> getLastRevision(@PathVariable("id") UUID id) {
        return auditService.findLastChange(id);
    }

    // @JsonView(View.Audit.class)
    @RequestMapping(value = "/{id}/{revision}", method = RequestMethod.GET)
    public Revision<Integer, Instruction> getByRevision(@PathVariable("id") UUID id, @PathVariable("revision") Integer revision) {
        return auditService.findRevision(id, revision);
    }

    // @JsonView(View.Audit.class)
    @RequestMapping(value = "/{id}/all", method = RequestMethod.GET)
    public PagedModel<EntityModel<Revision<Integer, Instruction>>> allProjects(
            @PathVariable("id") UUID id,
            @RequestParam(value = "ignorechangekinds",defaultValue = "IN_DEVELOPMENT,UPDATED_HIERARCHY_RELATION,UPDATED_PARENT")
                    Collection<AbstractEntityAudit.ChangeKind> changekinds,
            Pageable pageable, PagedResourcesAssembler<Revision<Integer, Instruction>> assembler) {

        Page<Revision<Integer, Instruction>> entities = auditService.findRevisionByIdAndChangeKindNotIn(id,changekinds, pageable);
        return assembler.toModel(entities);
    }
}
