package no.nsd.qddt.domain.study.web;

import no.nsd.qddt.domain.study.Study;
import no.nsd.qddt.domain.study.StudyService;
import no.nsd.qddt.domain.user.User;
import no.nsd.qddt.utils.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@RestController
@RequestMapping("/study")
public class StudyController {

    private StudyService studyService;

    @Autowired
    public StudyController(StudyService studyService) {
        this.studyService = studyService;
    }


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Study get(@PathVariable("id") UUID id) {
        System.out.println("FINDONE STUDY->" +id);
        return studyService.findOne(id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Study update(@RequestBody Study instance) {
        User user = SecurityContext.getUserDetails().getUser();
        instance.setCreatedBy(user);
        return studyService.save(instance);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Study create(@RequestBody Study instance) {
        User user = SecurityContext.getUserDetails().getUser();
        instance.setCreatedBy(user);
        instance.setAgency(user.getAgency());
        return studyService.save(instance);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public void delete(@PathVariable("id") UUID id) {
        studyService.delete(id);
    }


}