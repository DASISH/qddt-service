package no.nsd.qddt.domain.study.web;

import no.nsd.qddt.domain.AbstractController;
import no.nsd.qddt.domain.study.Study;
import no.nsd.qddt.domain.study.StudyService;
import no.nsd.qddt.domain.surveyprogram.SurveyProgramService;
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
    public Study createByParent(@RequestBody Study instance, @PathVariable("surveyId")UUID parentId) {
        return service.save(
            surveyProgramService
                .findOne(parentId)
                .addStudy(instance));
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


    @ResponseBody
    @RequestMapping(value = "/pdf/{id}", method = RequestMethod.GET, produces = "application/pdf")
    public byte[] getPdf(@PathVariable("id") UUID id) {
        return service.findOne(id).makePdf().toByteArray();
    }

}
