package no.nsd.qddt.domain.category.web;

import no.nsd.qddt.domain.classes.AbstractEntityAudit;
import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.domain.category.audit.CategoryAuditService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(value = "/audit/category", produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoryAuditController {

    private final CategoryAuditService auditService;

    @Autowired
    public CategoryAuditController(CategoryAuditService service) {
        this.auditService = service;
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Revision<Integer, Category> getLastRevision(@PathVariable("id") UUID id) {
        return auditService.findLastChange(id);
    }

    @RequestMapping(value = "/{id}/{revision}", method = RequestMethod.GET)
    public Revision<Integer, Category> getByRevision(@PathVariable("id") UUID id, @PathVariable("revision") Integer revision) {
        return auditService.findRevision(id, revision);
    }

    @RequestMapping(value = "/{id}/all", method = RequestMethod.GET)
    public PagedModel<EntityModel<Revision<Integer, Category>>> allProjects(
            @PathVariable("id") UUID id,
            @RequestParam(value = "ignorechangekinds",defaultValue = "IN_DEVELOPMENT,UPDATED_HIERARCHY_RELATION,UPDATED_PARENT")
            Collection<AbstractEntityAudit.ChangeKind> changekinds,
            Pageable pageable, PagedResourcesAssembler<Revision<Integer, Category>> assembler) {

        return assembler.toModel(
            auditService.findRevisionByIdAndChangeKindNotIn(id,changekinds, pageable)
        );
    }

}
