package no.nsd.qddt.controller;

import no.nsd.qddt.domain.Comment;
import no.nsd.qddt.domain.Survey;
import no.nsd.qddt.service.CommentService;
import no.nsd.qddt.service.SurveyService;
import no.nsd.qddt.utils.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@RestController
@RequestMapping("/survey")
public class SurveyController extends AbstractAuditController<Survey> {

    private CommentService commentService;

    @Autowired
    public SurveyController(SurveyService service, CommentService commentService) {
        super(service);
        this.commentService = commentService;
    }

    /**
     * Add a comment to the survey
     * @param id of the survey
     * @param comment to add
     * @return the added comment with no relations
     */
    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/{id}/comment", method = RequestMethod.POST)
    public Comment addComment(@RequestBody Comment comment, @PathVariable("id") Long id) {
        Survey survey = service.findById(id);
        comment.setSurvey(survey);
        comment.setCreatedBy(SecurityContext.getUserDetails().getUser());

        return commentService.save(comment);
    }

}
