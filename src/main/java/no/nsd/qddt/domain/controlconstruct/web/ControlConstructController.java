package no.nsd.qddt.domain.controlconstruct.web;

import no.nsd.qddt.domain.AbstractController;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.controlconstruct.ControlConstructService;
import no.nsd.qddt.domain.controlconstruct.json.ConstructJson;
import no.nsd.qddt.domain.controlconstruct.json.ConstructQuestionJson;
import no.nsd.qddt.domain.controlconstruct.pojo.*;
import no.nsd.qddt.domain.othermaterial.OtherMaterialService;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * This controller relates to a meta storage, which has a rank,logic and Rankrationale property, and thus need control
 *
 * @author Stig Norland
 */

@RestController
@RequestMapping("/controlconstruct")
public class ControlConstructController extends AbstractController {

    private final ControlConstructService service;
    private final OtherMaterialService omService;

    @Autowired
    public ControlConstructController(ControlConstructService ccService, OtherMaterialService otherMaterialService) {
        this.service = ccService;
        this.omService = otherMaterialService;
      //  this.iService = instrumentService;
    }


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public <S extends ControlConstruct> S get(@PathVariable("id") UUID id) {
        return service.findOne(id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/condition", method = RequestMethod.POST)
    public ConditionConstruct update(@RequestBody ConditionConstruct instance) {
        return service.save(instance);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/question", method = RequestMethod.POST)
    public QuestionConstruct update(@RequestBody QuestionConstruct instance) {
        return service.save(instance);
    }
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/sequence", method = RequestMethod.POST)
    public Sequence update(@RequestBody Sequence instance) {
        return service.save(instance);
    }
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/statement", method = RequestMethod.POST)
    public StatementItem update(@RequestBody StatementItem instance) {
        return service.save(instance);
    }


    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/condition/create", method = RequestMethod.POST)
    public ConditionConstruct createCC(@RequestBody ConditionConstruct instance) {
        return service.save(instance);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/question/create", method = RequestMethod.POST)
    public QuestionConstruct createQC(@RequestBody QuestionConstruct instance) {
        return service.save(instance);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/sequence/create", method = RequestMethod.POST)
    public Sequence createSC(@RequestBody Sequence instance) {
        return service.save(instance);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/statement/create", method = RequestMethod.POST)
    public StatementItem createSI(@RequestBody StatementItem instance) {
        return service.save(instance);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/createfile", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
    public ControlConstruct createWithFile(@RequestParam("files") MultipartFile[] files,@RequestParam("controlconstruct") ControlConstruct instance) throws FileUploadException {
        if (instance.getChangeKind() == null || instance.getChangeKind()  == AbstractEntityAudit.ChangeKind.CREATED)
            instance = service.save(instance);
        if (files != null && files.length > 0)
            for (MultipartFile multipartFile:files) {
                instance.addOtherMaterial(omService.saveFile(multipartFile, instance.getId(),"CC"));
            }
        return service.save(instance);
    }
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") UUID id) {
        service.delete(id);
    }


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/list/by-question/{uuid}", method = RequestMethod.GET)
    public List<ConstructQuestionJson> getBySecond(@PathVariable("uuid") UUID secondId) {
        try {
            return service.findByQuestionItems(Collections.singletonList(secondId));
        } catch (Exception ex){
            LOG.error("getBySecond",ex);
            throw ex;
        }
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/list/by-questiontext/{question}", method = RequestMethod.GET)
    public List<ConstructQuestionJson> getTop25ByQuestionText(@PathVariable("question") String questionText) {
        return service.findTop25ByQuestionItemQuestion(questionText);
    }

//    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page/search", method = RequestMethod.GET,produces = { MediaType.APPLICATION_JSON_VALUE })
    public HttpEntity<PagedResources<ConstructJson>> getBy(@RequestParam(value = "name",defaultValue = "%") String name,
                                                              @RequestParam(value = "questiontext",defaultValue = "%") String question,
                                                              @RequestParam(value = "constructkind",defaultValue = "QUESTION_CONSTRUCT") String kind,
                                                              Pageable pageable, PagedResourcesAssembler assembler) {


        // Originally name and question was 2 separate search strings, now we search both name and questiontext for value in "question"
        // Change in frontEnd usage made it necessary to distinguish

        Page<ConstructJson> controlConstructs =
                service.findByNameLikeAndControlConstructKind(name,question,kind,pageable); //.map( source -> Converter.mapConstruct( source ));

        return new ResponseEntity<>(assembler.toResource(controlConstructs), HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/pdf/{id}", method = RequestMethod.GET, produces = "application/pdf")
    public byte[] getPdf(@PathVariable("id") UUID id) {
        return service.findOne(id).makePdf().toByteArray();
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/xml/{id}", method = RequestMethod.GET)
    public String getXml(@PathVariable("id") UUID id) {
        return service.findOne(id).toDDIXml();
    }
}