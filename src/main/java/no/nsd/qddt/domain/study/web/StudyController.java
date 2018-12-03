package no.nsd.qddt.domain.study.web;

import no.nsd.qddt.domain.AbstractController;
import no.nsd.qddt.domain.study.Study;
import no.nsd.qddt.domain.study.StudyService;
import no.nsd.qddt.domain.surveyprogram.SurveyProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@RestController
@RequestMapping("/study")
public class StudyController extends AbstractController {

    private final StudyService service;
    private final SurveyProgramService surveyProgramService;

    @Autowired
    public StudyController(StudyService service, SurveyProgramService surveyProgramService
    ) {
        this.service = service;
        this.surveyProgramService = surveyProgramService;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Study get(@PathVariable("id") UUID id) {

        return service.findOne(id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Study update(@RequestBody Study instance) {
        return service.save(instance);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create/{surveyId}", method = RequestMethod.POST)
    public Study create(@RequestBody Study instance, @PathVariable("surveyId")UUID surveyId) {

        if (instance.getSurveyProgram() == null){
            surveyProgramService.findOne(surveyId).addStudy(instance);
        }
        return service.save(instance);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") UUID id) {

        try {
            service.delete(id);
        } catch (Exception ex) {
            LOG.error("Delete failed for Study " + id,ex);
            throw  ex;
        }
    }

    @RequestMapping(value = "/list/by-parent/{uuid}", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<Study> getList(@PathVariable("uuid") UUID parentid) {
        return this.surveyProgramService.findOne( parentid ).getStudies()
            .stream().filter( f -> f != null).collect( Collectors.toList());
    }


    @ResponseBody
    @RequestMapping(value = "/pdf/{id}", method = RequestMethod.GET, produces = "application/pdf")
    public byte[] getPdf(@PathVariable("id") UUID id) {
        return service.findOne(id).makePdf().toByteArray();
    }

//    @ResponseStatus(value = HttpStatus.OK)
//    @RequestMapping(value = "/xml/{id}", method = RequestMethod.GET)
//    public String getXml(@PathVariable("id") UUID id) {
//        return new XmlFragmentAssembler<Study>(service.findOne(id)).compileToXml();
//
//    }
}