package no.nsd.qddt.domain.author.web;

import no.nsd.qddt.domain.author.Author;
import no.nsd.qddt.domain.author.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
    public PagedModel<EntityModel<Author>> getBy(@RequestParam(value = "name", defaultValue = "") String name,
                                                 @RequestParam(value = "about",defaultValue = "") String about,
                                                 @RequestParam(value = "email",defaultValue = "") String email,
                                                 Pageable pageable, PagedResourcesAssembler<Author> assembler) {

        return assembler.toModel(
            authorService.findbyPageable( name, about, email, pageable )
        );
    }

}
