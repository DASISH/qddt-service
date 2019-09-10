package no.nsd.qddt.domain.category.web;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.domain.category.audit.CategoryAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
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
    public Page<Revision<Integer, Category>> allProjects(
            @PathVariable("id") UUID id,
            @RequestParam(value = "ignorechangekinds",defaultValue = "IN_DEVELOPMENT,UPDATED_HIERARCHY_RELATION,UPDATED_PARENT")
            Collection<AbstractEntityAudit.ChangeKind> changekinds,
            Pageable pageable) {
            return auditService.findRevisionByIdAndChangeKindNotIn(id,changekinds, pageable);
//        Page<Revision<Integer, Category>> entities = auditService.findRevisionByIdAndChangeKindNotIn(id,changekinds, pageable);
//        return new ResponseEntity<>(assembler.toResource(entities), HttpStatus.OK);
    }

//    @RequestMapping(value = "/version/{id}/{version}", method = RequestMethod.GET)
//    public Revision<Integer, Category> getByVersion(@PathVariable("id") UUID id, @PathVariable("version") String version) {
//        return auditService.findVersion(id, version);
//    }

}