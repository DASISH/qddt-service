package no.nsd.qddt.controller;

import no.nsd.qddt.domain.Comment;
import no.nsd.qddt.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Stig Norland
 */
@RestController
@RequestMapping("/comment")
public class CommentController  extends AbstractAuditController<Comment> {

//    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService){
        super(commentService);
    }



}
