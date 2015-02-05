package no.nsd.qddt.controller;

import no.nsd.qddt.domain.Comment;
import no.nsd.qddt.service.CommentService;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
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


    @Override
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Comment> getAll() {
        return commentService.findAll();
    }


    @Override
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public HttpEntity<PagedResources<Comment>> getAll(Pageable pageable, PagedResourcesAssembler assembler) {
        Page<Comment> comments = commentService.findAll(pageable);
        return new ResponseEntity<>(assembler.toResource(comments), HttpStatus.OK);
    }


    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Comment getOne(@PathVariable("id") Long id) {
        return commentService.findById(id);
    }


    @Override
    @RequestMapping(value = "/UUID/{id}", method = RequestMethod.GET)
    public Comment getOne(@PathVariable("id") UUID id) {
        throw new NotImplementedException();
//        return null;
    }

    @Override
    @RequestMapping(value = "/revision/{id}/page", method = RequestMethod.GET)
    public HttpEntity<PagedResources<Revision<Integer, Comment>>> getAllRevisionsPageable(@PathVariable("id") Long id, Pageable pageable) {
        throw new NotImplementedException();
//        Page<Revision<Integer,Comment>> comments = commentService.findAllRevisionsPageable(id,pageable);
//        return new ResponseEntity<>(assembler.toResource(comments), HttpStatus.OK);
    }

    @Override
    public Revision<Integer, Comment> getEntityAtRevision(@PathVariable("id") Long id, @PathVariable("revision") Integer revision) {
        return commentService.findEntityAtRevision(id,revision);
    }

    @Override
    public Revision<Integer, Comment> getLastChange(@PathVariable("id") Long id) {
        return commentService.findLastChange(id);
    }


    @Override
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Comment create(Comment instance) {
        return commentService.save(instance);
    }


    @Override
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public void delete(Comment instance) {
        commentService.delete(instance);
    }


    @Override
    @RequestMapping(value = "/{id}/page", method = RequestMethod.GET)
    public HttpEntity<PagedResources<Comment>> getThreadbyId(@PathVariable("id") Long id, Pageable pageable, PagedResourcesAssembler assembler) {
        throw new NotImplementedException();
//        Page<Comment> comments = commentService.findSiblingsPageable(id,pageable);
//        return new ResponseEntity<>(assembler.toResource(comments), HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "UUID/{id}/page", method = RequestMethod.GET)
    public HttpEntity<PagedResources<Comment>> getThreadbyGuid(@PathVariable("id") UUID id, Pageable pageable, PagedResourcesAssembler assembler) {
        //No UUID within this entity

        throw new NotImplementedException();
//        Page<Comment> comments = commentService.findSiblingsPageable(id,pageable);
//        return new ResponseEntity<>(assembler.toResource(comments), HttpStatus.OK);
    }


}
