package no.nsd.qddt.domain.comment.web;

import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.comment.audit.CommentAuditService;
import no.nsd.qddt.domain.question.Question;
import no.nsd.qddt.domain.question.audit.QuestionAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author Stig Norland
 */
@RestController
@RequestMapping(value = "/audit/comment/", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommentAuditController {

    private CommentAuditService auditService;

    @Autowired
    public CommentAuditController(CommentAuditService service) {
        this.auditService = service;
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Revision<Integer, Comment> getLastRevision(@PathVariable("id") UUID id) {
        return auditService.findLastChange(id);
    }

    @RequestMapping(value = "/{id}/{revision}", method = RequestMethod.GET)
    public Revision<Integer, Comment> getByRevision(@PathVariable("id") UUID id, @PathVariable("revision") Integer revision) {
        return auditService.findRevision(id, revision);
    }

    @RequestMapping(value = "/{id}/all", method = RequestMethod.GET)
    public HttpEntity<PagedResources<Revision<Integer, Comment>>> allProjects(
            @PathVariable("id") UUID id, Pageable pageable, PagedResourcesAssembler assembler) {

        Page<Revision<Integer, Comment>> entities = auditService.findRevisions(id, pageable);
        return new ResponseEntity<>(assembler.toResource(entities), HttpStatus.OK);
    }
}