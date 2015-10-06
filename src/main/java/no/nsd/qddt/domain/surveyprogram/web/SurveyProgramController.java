package no.nsd.qddt.domain.surveyprogram.web;

import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.comment.CommentService;
import no.nsd.qddt.domain.surveyprogram.SurveyProgram;
import no.nsd.qddt.domain.surveyprogram.SurveyProgramService;
import no.nsd.qddt.utils.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@RestController
@RequestMapping("/surveyprogram")
public class SurveyProgramController {

    private SurveyProgramService surveyProgramService;
    private CommentService commentService;

    @Autowired
    public SurveyProgramController(SurveyProgramService surveyProgramService, CommentService commentService) {
        this.surveyProgramService = surveyProgramService;
        this.commentService = commentService;
    }

    /**
     * Add a comment to the survey
     * @param id of the survey
     * @param comment to add
     * @return the added comment with no relations
     */
    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/{id}/comment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Comment addComment(@RequestBody Comment comment, @PathVariable("id") UUID id) {
        SurveyProgram surveyProgram = surveyProgramService.findOne(id);
        comment.setOwnerId(surveyProgram.getId());
        comment.setCreatedBy(SecurityContext.getUserDetails().getUser());

        return commentService.save(comment);
    }

}
