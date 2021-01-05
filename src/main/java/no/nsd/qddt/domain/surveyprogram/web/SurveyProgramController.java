package no.nsd.qddt.domain.surveyprogram.web;

import no.nsd.qddt.classes.AbstractEntityAudit;
import no.nsd.qddt.domain.surveyprogram.SurveyOrders;
import no.nsd.qddt.domain.surveyprogram.SurveyProgram;
import no.nsd.qddt.domain.surveyprogram.SurveyProgramService;
import no.nsd.qddt.domain.user.User;
import no.nsd.qddt.domain.security.SecurityContext;
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

    private final SurveyProgramService service;

    @Autowired
    public SurveyProgramController(SurveyProgramService service){ //, CommentService commentService) {
        this.service = service;
    }


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public SurveyProgram get(@PathVariable("id") UUID id) {
        return service.findOne(id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    public SurveyProgram update(@RequestBody SurveyProgram instance) {

        instance.getStudies().forEach(c->{
            c.setChangeKind( AbstractEntityAudit.ChangeKind.UPDATED_PARENT);
            c.setChangeComment("touched me to stay in sync...");
        });
        return service.save(instance);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    public SurveyProgram create(@RequestBody SurveyProgram instance) {
        return service.save(instance);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") UUID id) {
        service.delete(id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<SurveyProgram> listByUser() {
        User user = SecurityContext.getUserDetails().getUser();
        return service.findByAgency(user);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/reorder", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SurveyProgram> reOrder(@RequestBody SurveyOrders orders) {
        return service.reOrder(orders.getContent());
    }


    @ResponseBody
    @RequestMapping(value = "/pdf/{id}", method = RequestMethod.GET, produces = "application/pdf")
    public byte[] getPdf(@PathVariable("id") UUID id) {
        return service.findOne(id).makePdf().toByteArray();
    }



}
