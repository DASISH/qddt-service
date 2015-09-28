package no.nsd.qddt.controller;

import no.nsd.qddt.domain.Study;
import no.nsd.qddt.service.StudyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@RestController
@RequestMapping("/study")
public class StudyController extends AbstractAuditController<Study,UUID> {

    @Autowired
    public StudyController(StudyService service){ super(service);}

}