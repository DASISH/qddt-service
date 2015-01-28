package no.nsd.qddt.controller;

import no.nsd.qddt.domain.Study;
import no.nsd.qddt.service.StudyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Dag Østgulen Heradstveit
 */
@RestController
@RequestMapping("/study")
public class StudyController {

    private StudyService studyService;

    @Autowired
    public StudyController(StudyService studyService) {
        this.studyService = studyService;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Study> getAll() {
        return studyService.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Study getOne(@PathVariable("id") Long id) {
        return studyService.findById(id);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Study create(Study study) {
        return studyService.save(study);
    }
}