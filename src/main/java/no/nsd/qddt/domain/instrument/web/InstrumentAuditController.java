package no.nsd.qddt.domain.instrument.web;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.instrument.audit.InstrumentAuditService;
import no.nsd.qddt.domain.instrument.pojo.Instrument;
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
@RequestMapping(value = "/audit/instrument", produces = MediaType.APPLICATION_JSON_VALUE)
public class InstrumentAuditController {

    private final InstrumentAuditService auditService;

    @Autowired
    public InstrumentAuditController(InstrumentAuditService service) {
        this.auditService = service;
    }

    // @JsonView(View.Audit.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Revision<Integer, Instrument> getLastRevision(@PathVariable("id") UUID id) {
        return auditService.findLastChange(id);
    }

    // @JsonView(View.Audit.class)
    @RequestMapping(value = "/{id}/{revision}", method = RequestMethod.GET)
    public Revision<Integer, Instrument> getByRevision(@PathVariable("id") UUID id, @PathVariable("revision") Integer revision) {
        return auditService.findRevision(id, revision);
    }

    // @JsonView(View.Audit.class)
    @RequestMapping(value = "/{id}/all", method = RequestMethod.GET)
    public PagedModel<EntityModel<Revision<Integer, Instrument>>> allProjects(
            @PathVariable("id") UUID id,
            @RequestParam(value = "ignorechangekinds",defaultValue = "IN_DEVELOPMENT,UPDATED_HIERARCHY_RELATION,UPDATED_PARENT")
                    Collection<AbstractEntityAudit.ChangeKind> changekinds,
            Pageable pageable, PagedResourcesAssembler<Revision<Integer, Instrument>> assembler) {

        Page<Revision<Integer, Instrument>> entities = auditService.findRevisionByIdAndChangeKindNotIn(id,changekinds, pageable);
        return assembler.toModel(entities);
    }

    // @JsonView(View.Audit.class)
    @ResponseBody
    @RequestMapping(value = "/pdf/{id}/{revision}",  method = RequestMethod.GET, produces = "application/pdf")
    public byte[] getPdf(@PathVariable("id") UUID id, @PathVariable("revision") Integer revision) {
        return auditService.findRevision(id, revision).getEntity().makePdf().toByteArray();
    }

}
