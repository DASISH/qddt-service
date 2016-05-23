package no.nsd.qddt.domain.study.web;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.instrument.Instrument;
import no.nsd.qddt.domain.instrument.InstrumentService;
import no.nsd.qddt.domain.study.Study;
import no.nsd.qddt.domain.study.StudyService;
import no.nsd.qddt.domain.surveyprogram.SurveyProgramService;
import no.nsd.qddt.utils.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@RestController
@RequestMapping("/study")
public class StudyController {

    private StudyService studyService;
    private SurveyProgramService surveyProgramService;
    private InstrumentService instrumentService;

    @Autowired
    public StudyController(StudyService studyService, SurveyProgramService surveyProgramService,
                            InstrumentService instrumentService) {
        this.studyService = studyService;
        this.surveyProgramService = surveyProgramService;
        this.instrumentService = instrumentService;
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

        //Surveyprogram has JsonIgnore, needs to fetch Survey from the DB
        if (instance.getSurveyProgram() == null){
            Study original =  studyService.findOne(instance.getId());
            instance.setSurveyProgram(original.getSurveyProgram());
            System.out.println("UPS, this code shouldn't have been triggered... (fetching Survey ID)");
        }

        if (instance.getInstruments().size() == 0){
            instance.setInstruments(new HashSet<Instrument>(){{add(new Instrument());}});
        }
        else {
            instance.getInstruments().forEach(c -> c.setChangeKind(AbstractEntityAudit.ChangeKind.UPDATED_PARENT));
        }

        instance.setModifiedBy(SecurityContext.getUserDetails().getUser());
        instance.getTopicGroups().forEach(c->c.setChangeKind(AbstractEntityAudit.ChangeKind.UPDATED_PARENT));

        return studyService.save(instance);

    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create/{surveyId}", method = RequestMethod.POST)
    public Study create(@RequestBody Study instance, @PathVariable("surveyId")UUID surveyId) {

        instance.setSurveyProgram(surveyProgramService.findOne(surveyId));
        instance.setModifiedBy(SecurityContext.getUserDetails().getUser());
        return studyService.save(instance);

    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public void delete(@PathVariable("id") UUID id) {
        studyService.delete(id);
    }

}