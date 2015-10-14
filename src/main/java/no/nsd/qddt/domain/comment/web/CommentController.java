package no.nsd.qddt.domain.comment.web;

import no.nsd.qddt.domain.AbstractController;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.comment.CommentService;
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
@RequestMapping("/comment")
public class CommentController {

    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page/{ownerId}", method = RequestMethod.GET)
    public HttpEntity<PagedResources<Comment>> get(@PathVariable("ownerId")UUID ownerId, Pageable pageable, PagedResourcesAssembler assembler) {

        Page<Comment> comments = commentService.findAllByOwnerIdPageable(ownerId, pageable);
        return new ResponseEntity<>(assembler.toResource(comments), HttpStatus.OK);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Comment get(@PathVariable("id") UUID id) {
        return commentService.findOne(id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Comment update(@RequestBody Comment comment) {
        return commentService.save(comment);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create/{ownerId}", method = RequestMethod.POST)
    public Comment create(@RequestBody Comment comment, @PathVariable("ownerId") UUID ownerId) {
        comment.setOwnerId(ownerId);
        return commentService.save(comment);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public void delete(@PathVariable("id") UUID id) {
        commentService.delete(id);
    }


}
