package no.nsd.qddt.domain.controlconstruct.web;

import no.nsd.qddt.domain.controlconstruct.ControlConstruct;
import no.nsd.qddt.domain.controlconstruct.ControlConstructService;
import no.nsd.qddt.domain.othermaterial.OtherMaterial;
import no.nsd.qddt.domain.othermaterial.OtherMaterialService;
import no.nsd.qddt.domain.questionItem.QuestionItemService;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * This controller relates to a meta storage, which has a rank,logic and Rankrationale property, and thus need control
 *
 * @author Stig Norland
 */

@RestController
@RequestMapping("/controlconstruct")
public class ControlConstructController {

    private ControlConstructService controlConstructService;
    private OtherMaterialService omService;

    @Autowired
    public ControlConstructController(ControlConstructService ccService,OtherMaterialService otherMaterialService){
        this.controlConstructService = ccService;
        this.omService = otherMaterialService;
    }


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ControlConstruct get(@PathVariable("id") UUID id) {
        return controlConstructService.findOne(id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ControlConstruct update(@RequestBody ControlConstruct instance) {
        return controlConstructService.save(instance);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ControlConstruct create(@RequestBody ControlConstruct instance) {
        return controlConstructService.save(instance);
    }

//    @ResponseStatus(value = HttpStatus.OK)
//    @RequestMapping(value = "", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
//    public ControlConstruct updateWithFile(@RequestParam("controlconstruct") ControlConstruct instance, @RequestParam("files") MultipartFile[] files) throws FileUploadException {
//        instance = controlConstructService.save(instance);
//        if (files != null && files.length > 0)
//            for (MultipartFile multipartFile:files) {
//                instance.addOtherMaterials(omService.saveFile(multipartFile, instance.getId()));
//            }
//        return instance;
//    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/createfile", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
    public ControlConstruct createWithFile(@RequestParam("files") MultipartFile[] files,@RequestParam("controlconstruct") ControlConstruct instance) throws FileUploadException {
        instance = controlConstructService.save(instance);
        if (files != null && files.length > 0)
            for (MultipartFile multipartFile:files) {
                instance.addOtherMaterials(omService.saveFile(multipartFile, instance.getId()));
            }
        return instance;
    }
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public void delete(@PathVariable("id") UUID id) {
        controlConstructService.delete(id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/list/by-instrument/{uuid}", method = RequestMethod.GET)
    public List<ControlConstruct> getByFirst(@PathVariable("uuid") UUID firstId) {

        return controlConstructService.findByInstrumentId(firstId);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/list/by-question/{uuid}", method = RequestMethod.GET)
    public List<ControlConstruct> getBySecond(@PathVariable("uuid") UUID secondId) {
        return controlConstructService.findByQuestionItemUUIDs(Arrays.asList(new UUID[]{secondId}));
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/list/by-questiontext/{question}", method = RequestMethod.GET)
    public List<ControlConstruct> getTop25ByQuestionText(@PathVariable("question") String questionText) {
        return controlConstructService.findTop25ByQuestionItemQuestion(questionText);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page/search", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public HttpEntity<PagedResources<ControlConstruct>> getBy(@RequestParam(value = "name",defaultValue = "%") String name,
                                                              @RequestParam(value = "questiontext",defaultValue = "%") String question,
                                                              Pageable pageable, PagedResourcesAssembler assembler) {
        // Originally name and question was 2 separate search strings, now we search both name and questiontext for value in "question"

        Page<ControlConstruct> questionitems =
                controlConstructService.findByNameLikeOrQuestionLike(name, question, pageable);

        return new ResponseEntity<>(assembler.toResource(questionitems), HttpStatus.OK);
    }

}
