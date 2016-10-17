package no.nsd.qddt.domain.surveyprogram.web;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.surveyprogram.SurveyProgram;
import no.nsd.qddt.domain.surveyprogram.SurveyProgramService;
import no.nsd.qddt.domain.user.User;
import no.nsd.qddt.utils.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@RestController
@RequestMapping("/surveyprogram")
public class SurveyProgramController {

    private SurveyProgramService surveyProgramService;
//    private CommentService commentService;

    @Autowired
    public SurveyProgramController(SurveyProgramService surveyProgramService){ //, CommentService commentService) {
        this.surveyProgramService = surveyProgramService;
//        this.commentService = commentService;
    }


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public SurveyProgram get(@PathVariable("id") UUID id) {
        return surveyProgramService.findOne(id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    public SurveyProgram update(@RequestBody SurveyProgram instance) {

        instance.getStudies().forEach(c->{
            c.setChangeKind(AbstractEntityAudit.ChangeKind.UPDATED_PARENT);
            c.setChangeComment("");
        });
        return surveyProgramService.save(instance);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    public SurveyProgram create(@RequestBody SurveyProgram instance) {
        return surveyProgramService.save(instance);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public void delete(@PathVariable("id") UUID id) {
        surveyProgramService.delete(id);
    }


//    /**
//     * Add a comment to the survey, this should
//     * @param id of the survey
//     * @param comment to add
//     * @return the added comment with no relations
//     */
//    @ResponseStatus(value = HttpStatus.CREATED)
//    @RequestMapping(value = "/{id}/comment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//    public Comment addComment(@RequestBody Comment comment, @PathVariable("id") UUID id) {
//
//        System.out.println("COMMENTS->");
//        SurveyProgram surveyProgram = surveyProgramService.findOne(id);
//        comment.setOwnerId(surveyProgram.getId());
//        return commentService.save(comment);
//    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/list/by-user", method = RequestMethod.GET)
    public List<SurveyProgram> listByUser() {
        User user = SecurityContext.getUserDetails().getUser();
        return surveyProgramService.findByAgency(user);
    }


}
