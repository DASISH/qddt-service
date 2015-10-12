package no.nsd.qddt.domain.study.web;

import no.nsd.qddt.domain.study.Study;
import no.nsd.qddt.domain.study.StudyService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Study getById(@PathVariable("id") UUID id) {
        return studyService.findOne(id);
    }

}