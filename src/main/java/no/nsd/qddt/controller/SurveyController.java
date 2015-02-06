package no.nsd.qddt.controller;

import no.nsd.qddt.domain.Survey;
import no.nsd.qddt.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@RestController
@RequestMapping("/survey")
public class SurveyController extends AbstractAuditController<Survey> {

    @Autowired
    public SurveyController(SurveyService service){ super(service);}

}
