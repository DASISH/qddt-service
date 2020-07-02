package no.nsd.qddt.domain.author.web;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import no.nsd.qddt.domain.author.Author;
import no.nsd.qddt.domain.author.AuthorService;

/**
 * @author Stig Norland
 */
@RestController
@RequestMapping(value = "/author")
public class AuthorController {

    private final AuthorService authorService;


    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }


    @GetMapping(value = "{id}")
    public Author get(@PathVariable("id") UUID id) {
        return authorService.findOne(id);
    }

    @PostMapping(value = "")
    public Author update(@RequestBody Author author) {
        return authorService.save(author);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(value = "/create")
    public Author create(@RequestBody Author author) {
        return authorService.save(author);
    }

    @DeleteMapping(value = "/delete/{id}")
    public void delete(@PathVariable("id") UUID id) {
        authorService.delete(id);
    }


    @GetMapping(value = "/page/search",produces = {MediaType.APPLICATION_JSON_VALUE})
    public  PagedResources<Resource<Author>> getBy(@RequestParam(value = "name", defaultValue = "") String name,
                                            @RequestParam(value = "about",defaultValue = "") String about,
                                            @RequestParam(value = "email",defaultValue = "") String email,
                                            Pageable pageable, PagedResourcesAssembler<Author> assembler) {

        Page<Author> authors = authorService.findbyPageable( name, about, email, pageable );
        return assembler.toResource(authors);
    }

}
