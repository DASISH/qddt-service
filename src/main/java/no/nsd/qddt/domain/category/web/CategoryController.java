package no.nsd.qddt.domain.category.web;

import no.nsd.qddt.domain.HierarchyLevel;
import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.domain.category.CategoryService;
import no.nsd.qddt.domain.category.CategoryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Category get(@PathVariable("id") UUID id) {
        return categoryService.findOne(id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Category update(@RequestBody Category category) {
        return categoryService.save(category);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Category create(@RequestBody Category category) {
        return categoryService.save(category);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public void delete(@PathVariable("id") UUID id) {
        categoryService.delete(id);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page/by-root/{name}", method = RequestMethod.GET)
    public HttpEntity<PagedResources<Category>> getByRoot(@PathVariable("name")String name, Pageable pageable, PagedResourcesAssembler assembler) {

        if (name.isEmpty() || name.length() == 0)
            name = "%";
        Page<Category> categories = categoryService.findByHierarchyAndNameLike(HierarchyLevel.ROOT_ENTITY, name, pageable);
        return new ResponseEntity<>(assembler.toResource(categories), HttpStatus.OK);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page/by-group/{name}", method = RequestMethod.GET)
    public HttpEntity<PagedResources<Category>> getByGroup(@PathVariable("name")String name, Pageable pageable, PagedResourcesAssembler assembler) {

        if (name.isEmpty() || name.length() == 0)
            name = "%";
        Page<Category> categories = categoryService.findByHierarchyAndNameLike(HierarchyLevel.GROUP_ENTITY, name, pageable);
        return new ResponseEntity<>(assembler.toResource(categories), HttpStatus.OK);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page/by-leaf/{name}", method = RequestMethod.GET)
    public HttpEntity<PagedResources<Category>> getByLeaf(@PathVariable("name")String name, Pageable pageable, PagedResourcesAssembler assembler) {

        if (name.isEmpty() || name.length() == 0)
            name = "%";
        Page<Category> categories = categoryService.findByHierarchyAndNameLike(HierarchyLevel.ENTITY, name, pageable);
        return new ResponseEntity<>(assembler.toResource(categories), HttpStatus.OK);
    }


}
