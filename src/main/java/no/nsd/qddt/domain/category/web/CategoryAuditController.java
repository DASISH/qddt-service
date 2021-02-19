package no.nsd.qddt.domain.category.web;


import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.domain.category.CategoryService;
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

    private final CategoryService categoryService;

    @Autowired
    public CategoryAuditController(CategoryService repository) {
        this.categoryService = repository;
    }

    @GetMapping( "/{id}")
    public Revision<Integer, Category> getLastRevision(@PathVariable("id") UUID id) {
        return categoryService.findLastChange(id);
    }

    @GetMapping( "/{id}/{revision}")
    public Revision<Integer, Category> getByRevision(@PathVariable("id") UUID id, @PathVariable("revision") Integer revision) {
        return categoryService.findRevision(id, revision);
    }

    @GetMapping( "/{id}/all")
    public PagedModel<EntityModel<Revision<Integer, Category>>> allProjects(
        @PathVariable("id") UUID id,
        @RequestParam(value = "ignorechangekinds",defaultValue = "IN_DEVELOPMENT,UPDATED_HIERARCHY_RELATION,UPDATED_PARENT")
            Collection<AbstractEntityAudit.ChangeKind> changekinds,
        Pageable pageable, PagedResourcesAssembler<Revision<Integer, Category>> assembler) {
//        return getPageIncLatest(categoryService.findRevisions(id),changeKinds,pageable);

        return assembler.toModel(
            categoryService.findRevisionsByChangeKindIncludeLatest(id,changekinds, pageable)
        );
    }

}
