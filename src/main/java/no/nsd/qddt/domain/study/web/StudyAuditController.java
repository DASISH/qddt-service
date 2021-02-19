package no.nsd.qddt.domain.study.web;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.study.Study;
import no.nsd.qddt.domain.study.audit.StudyAuditService;
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
 * @author Dag Østgulen Heradstveit
 */
@RestController
@RequestMapping(value = "/audit/study", produces = MediaType.APPLICATION_JSON_VALUE)
public class StudyAuditController {

    private final StudyAuditService service;

    @Autowired
    public StudyAuditController(StudyAuditService service) {
        this.service = service;
    }


    // @JsonView(View.Audit.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Revision<Integer, Study> getLastRevision(@PathVariable("id") UUID id) {
        return service.findLastChange(id);
    }

    // @JsonView(View.Audit.class)
    @RequestMapping(value = "/{id}/{revision}", method = RequestMethod.GET)
    public Revision<Integer, Study> getByRevision(@PathVariable("id") UUID id, @PathVariable("revision") Integer revision) {
        return service.findRevision(id, revision);
    }

    // @JsonView(View.Audit.class)
    @RequestMapping(value = "/{id}/all", method = RequestMethod.GET)
    public PagedModel<EntityModel<Revision<Integer, Study>>> allProjects(
            @PathVariable("id") UUID id,
            @RequestParam(value = "ignorechangekinds",defaultValue = "IN_DEVELOPMENT,UPDATED_HIERARCHY_RELATION,UPDATED_PARENT,UPDATED_CHILD")
                    Collection<AbstractEntityAudit.ChangeKind> changekinds,
            Pageable pageable, PagedResourcesAssembler<Revision<Integer, Study>> assembler) {

        Page<Revision<Integer, Study>> revisions = service.findRevisionByIdAndChangeKindNotIn(id,changekinds, pageable);
        return assembler.toModel(revisions);
    }

    @ResponseBody
    @RequestMapping(value = "/pdf/{id}/{revision}",  method = RequestMethod.GET, produces = "application/pdf")
    public byte[] getPdf(@PathVariable("id") UUID id, @PathVariable("revision") Integer revision) {
        return service.findRevision(id, revision).getEntity().makePdf().toByteArray();
    }

}
