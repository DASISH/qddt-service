package no.nsd.qddt.controller;

import no.nsd.qddt.domain.Survey;
import no.nsd.qddt.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Dag Østgulen Heradstveit
 */
@RestController
@RequestMapping("/survey")
public class SurveyController {

    @Autowired
    private SurveyService surveyService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Survey findOne(@PathVariable("id") Long id) {
        return surveyService.findById(id);
    }
}
