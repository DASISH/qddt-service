package no.nsd.qddt.controller;

import no.nsd.qddt.domain.Comment;
import no.nsd.qddt.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@RestController
@RequestMapping("/comment")
public class CommentController  implements BaseHierachyController<Comment> {

    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    @RequestMapping(value = "/{id}/all", method = RequestMethod.GET)
    public HttpEntity<PagedResources<Comment>> getThread(
            @PathVariable("id") Long id,Pageable pageable, PagedResourcesAssembler assembler)
    {
        Page<Comment> comments = commentService.findSiblingsPageable(id, pageable);
        return new ResponseEntity<>(assembler.toResource(comments), HttpStatus.OK);
    }


    @Override
    public List<Comment> getAll() {
        return null;
    }

    @Override
    public HttpEntity<PagedResources<Comment>> getAll(Pageable pageable, PagedResourcesAssembler assembler) {
        return null;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Comment getOne(@PathVariable("id") Long id) {
        return commentService.findById(id);
    }

    @Override
    public Comment getOne(UUID id) {
        return null;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Comment create(Comment comment) {
        return commentService.save(comment);
    }

    @Override
    public void delete(Comment instance) {

    }

    @Override
    public HttpEntity<PagedResources<Comment>> getThreadbyId(Long id, Pageable pageable, PagedResourcesAssembler assembler) {
        return null;
    }

    @Override
    public HttpEntity<PagedResources<Comment>> getThreadbyGuid(UUID id, Pageable pageable, PagedResourcesAssembler assembler) {
        return null;
    }
}
