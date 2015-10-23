package no.nsd.qddt.domain.bcategory.web;

import no.nsd.qddt.domain.bcategory.Category;
import no.nsd.qddt.domain.bcategory.CategoryService;
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

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page/{TAG}", method = RequestMethod.GET)
    public HttpEntity<PagedResources<Category>> get(@PathVariable("TAG")String tag, Pageable pageable, PagedResourcesAssembler assembler) {

        Page<Category> comments = categoryService.findByTagPageable(tag, pageable);
        return new ResponseEntity<>(assembler.toResource(comments), HttpStatus.OK);
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
}
