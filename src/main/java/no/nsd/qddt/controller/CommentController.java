package no.nsd.qddt.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author Stig Norland
 */
@RestController
@RequestMapping("/comment")
public class CommentController  extends AbstractController<Comment,UUID> {

    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService){
        super(commentService);
        this.commentService = commentService;
    }


    @RequestMapping(value = "/byowner/{ownerUUID}/page/thread", method = RequestMethod.GET)
    public HttpEntity<PagedResources<Comment>> get(UUID ownerUUID, Pageable pageable, PagedResourcesAssembler assembler) {

        Page<Comment> comments = commentService.findAllByOwnerUUIDPageable(ownerUUID, pageable);
        return new ResponseEntity<>(assembler.toResource(comments), HttpStatus.OK);
    }


}
