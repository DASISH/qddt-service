package no.nsd.qddt.domain.comment.web;

import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.comment.CommentJsonEdit;
import no.nsd.qddt.domain.comment.CommentService;
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
@RequestMapping(value = "/comment")
public class CommentController {

    private final CommentService service;

    @Autowired
    public CommentController(CommentService service){
        this.service = service;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Comment get(@PathVariable("id") UUID id) {
        return service.findOne(id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Comment update(@RequestBody Comment comment) {
        return service.save(comment);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create/{ownerId}", method = RequestMethod.POST)
    public Comment create(@RequestBody Comment comment, @PathVariable("ownerId") UUID ownerId) {
        comment.setOwnerId(ownerId);
        return service.save(comment);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public void delete(@PathVariable("id") UUID id) {
        service.delete(id);
    }

    @SuppressWarnings("unchecked")
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/page/by-owner/{ownerId}", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public HttpEntity<PagedResources<CommentJsonEdit>> get(@PathVariable("ownerId")UUID ownerId, Pageable pageable, PagedResourcesAssembler assembler) {
        Page<CommentJsonEdit> comments = service.findAllByOwnerIdPageable(ownerId, pageable).map(CommentJsonEdit::new);
        return new ResponseEntity<>(assembler.toResource(comments), HttpStatus.OK);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/xml/{id}", method = RequestMethod.GET)
    public String getXml(@PathVariable("id") UUID id) {
        return service.findOne(id).toDDIXml();
    }
}
