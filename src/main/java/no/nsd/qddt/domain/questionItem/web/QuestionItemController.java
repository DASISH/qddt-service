package no.nsd.qddt.domain.questionItem.web;

import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.questionItem.json.QuestionItemJsonEdit;
import no.nsd.qddt.domain.questionItem.json.QuestionItemListJson;
import no.nsd.qddt.domain.questionItem.QuestionItemService;
import no.nsd.qddt.domain.xml.XmlReport;
import no.nsd.qddt.exception.StackTraceFilter;
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

import java.util.UUID;

/**
 * @author Stig Norland
 */
@RestController
@RequestMapping("/questionitem")
public class QuestionItemController {

    private final QuestionItemService service;

    @Autowired
    public QuestionItemController(QuestionItemService service){
        this.service = service;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public QuestionItemJsonEdit get(@PathVariable("id") UUID id) {
        return question2Json(service.findOne(id));
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public QuestionItemJsonEdit update(@RequestBody QuestionItem instance) {
        return question2Json(service.save(instance));
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public QuestionItemJsonEdit create(@RequestBody QuestionItem instance) {
        return question2Json(service.save(instance));
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") UUID id) {
        service.delete(id);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public HttpEntity<PagedResources<QuestionItemListJson>> getAll(Pageable pageable, PagedResourcesAssembler assembler) {

        Page<QuestionItemListJson> questionitems =
                service.findAllPageable(pageable).map(QuestionItemListJson::new);
        return new ResponseEntity<>(assembler.toResource(questionitems), HttpStatus.OK);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page/search", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public HttpEntity<PagedResources<QuestionItemListJson>>  getBy(@RequestParam(value = "name",defaultValue = "") String name,
                                                                   @RequestParam(value = "question",defaultValue = "") String question,
                                                                   @RequestParam(value = "responsename",defaultValue = "") String responseName,
                                                                   Pageable pageable, PagedResourcesAssembler assembler) {
        // Originally name and question was 2 separate search strings, now we search both name and questiontext for value in "question"
        try {
            Page<QuestionItemListJson> questionitems
                = service.findByNameOrQuestionOrResponseName(name, question, responseName, pageable).map(QuestionItemListJson::new);
            return new ResponseEntity<>(assembler.toResource(questionitems), HttpStatus.OK);
        } catch (Exception ex){
            StackTraceFilter.println(ex.getStackTrace());
            throw ex;
        }
    }

    private QuestionItemJsonEdit question2Json(QuestionItem questionItem){
        return  new QuestionItemJsonEdit(questionItem);
    }


    @ResponseBody
    @RequestMapping(value = "/pdf/{id}", method = RequestMethod.GET, produces = "application/pdf")
    public byte[] getPdf(@PathVariable("id") UUID id) {
        return service.findOne(id).makePdf().toByteArray();
    }


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/xml/{id}", method = RequestMethod.GET)
    public String getXml(@PathVariable("id") UUID id) {
        return new XmlReport(service.findOne(id)).get();
    }

}
