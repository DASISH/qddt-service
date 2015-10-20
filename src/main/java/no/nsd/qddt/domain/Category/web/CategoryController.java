package no.nsd.qddt.domain.Category.web;

import no.nsd.qddt.domain.Category.Category;
import no.nsd.qddt.domain.Category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Stig Norland
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService service) {
        this.categoryService = service;
    }


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Category> allCategories(){
        return categoryService.findAll();
    }


    @RequestMapping(value = "/allPageable", method = RequestMethod.GET)
    public HttpEntity<PagedResources<Category>> allCategoriesPage(
            Pageable pageable, PagedResourcesAssembler assembler){

        Page<Category> categories = categoryService.findAll(pageable);
        return new ResponseEntity<>(assembler.toResource(categories), HttpStatus.OK);
    }
}
