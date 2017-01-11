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
import org.springframework.http.MediaType;
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

    private CategoryService service;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.service = categoryService;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Category get(@PathVariable("id") UUID id) {
        return service.findOne(id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Category update(@RequestBody Category category) {
        return service.save(category);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Category create(@RequestBody Category category) {
        return service.save(category);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public void delete(@PathVariable("id") UUID id) {
        System.out.println("delete -> " + id);
        service.delete(id);
    }


    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page/by-group/{name}", method = RequestMethod.GET)
    public HttpEntity<PagedResources<Category>> getByGroup(@PathVariable("name")String name, Pageable pageable, PagedResourcesAssembler assembler) {

        name = name.replace("*","%");
        Page<Category> categories = service.findByHierarchyAndNameLike(HierarchyLevel.GROUP_ENTITY, name, pageable);

        return new ResponseEntity<>(assembler.toResource(categories), HttpStatus.OK);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page/by-leaf/{name}", method = RequestMethod.GET)
    public HttpEntity<PagedResources<Category>> getByLeaf(@PathVariable("name")String name, Pageable pageable, PagedResourcesAssembler assembler) {
        name = name.replace("*","%");

        Page<Category> categories = service.findByHierarchyAndNameLike(HierarchyLevel.ENTITY, name, pageable);
        return new ResponseEntity<>(assembler.toResource(categories), HttpStatus.OK);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page/search", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public HttpEntity<PagedResources<Category>>  getBy(@RequestParam(value = "level", required = false) String level,
                                                       @RequestParam(value = "category",required = false) String category,
                                                       @RequestParam(value = "name",defaultValue = "%") String name,
                                                       Pageable pageable, PagedResourcesAssembler assembler) {

        Page<Category> categories = null;
        name = name.replace("*","%");
        if (level == null || level.isEmpty()) {
            if (category == null || category.isEmpty()) {

                categories = service.findByNameLike(name, pageable);
            } else {

                categories = service.findByCategoryTypeAndNameLike(CategoryType.valueOf(category), name, pageable);
            }
        } else {
            if (category == null || category.isEmpty()) {

                categories = service.findByHierarchyAndNameLike(HierarchyLevel.valueOf(level), name, pageable);
            } else {

                categories = service.findByHierarchyAndCategoryAndNameLike(HierarchyLevel.valueOf(level),CategoryType.valueOf(category), name, pageable);
            }
        }

        return new ResponseEntity<>(assembler.toResource(categories), HttpStatus.OK);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/xml/{id}", method = RequestMethod.GET)
    public String getXml(@PathVariable("id") UUID id) {
        return service.findOne(id).toDDIXml();
    }
}
