package no.nsd.qddt.domain.category.web;

import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.domain.category.CategoryService;
import no.nsd.qddt.domain.category.json.CategoryJsonEdit;
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

    private final CategoryService service;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.service = categoryService;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public CategoryJsonEdit get(@PathVariable("id") UUID id) {
        return  new CategoryJsonEdit(service.findOne(id));
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public CategoryJsonEdit update(@RequestBody Category category) {
        return new CategoryJsonEdit(service.save(category));
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public CategoryJsonEdit create(@RequestBody Category category) {
        return new CategoryJsonEdit(service.save(category));
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") UUID id) {
        service.delete(id);
    }


    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page/search", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public HttpEntity<PagedResources<CategoryJsonEdit>>  getBy(@RequestParam(value = "level", defaultValue = "") String level,
                                                       @RequestParam(value = "categoryKind",defaultValue = "") String categoryKind,
                                                       @RequestParam(value = "name",defaultValue = "") String name,
                                                       @RequestParam(value = "description",defaultValue = "") String description,
                                                       Pageable pageable, PagedResourcesAssembler<CategoryJsonEdit> assembler) {

        Page<CategoryJsonEdit> categories = service.findBy(level,categoryKind, name, description, pageable)
            .map(converter -> new CategoryJsonEdit(converter));

        
        return new ResponseEntity(assembler.toResource(categories), HttpStatus.OK);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/xml/{id}", method = RequestMethod.GET)
    public String getXml(@PathVariable("id") UUID id) {
        return service.findOne(id).toDDIXml();
    }
}
