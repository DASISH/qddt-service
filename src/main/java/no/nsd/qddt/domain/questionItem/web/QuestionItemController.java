package no.nsd.qddt.domain.questionItem.web;

import no.nsd.qddt.domain.question.QuestionService;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.questionItem.QuestionItemJsonEdit;
import no.nsd.qddt.domain.questionItem.QuestionItemService;
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

    private QuestionItemService service;
    private QuestionService questionService;

    @Autowired
    public QuestionItemController(QuestionItemService service,QuestionService questionService){
        this.service = service;
        this.questionService = questionService;
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

        instance.setQuestion(
                questionService.save(
                        instance.getQuestion()));

        return question2Json(service.save(instance));
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public void delete(@PathVariable("id") UUID id) {
        service.delete(id);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public HttpEntity<PagedResources<QuestionItemJsonEdit>> getAll(Pageable pageable, PagedResourcesAssembler assembler) {

        Page<QuestionItemJsonEdit> questionitems =
                service.findAllPageable(pageable).map(F->question2Json(F));

        return new ResponseEntity<>(assembler.toResource(questionitems), HttpStatus.OK);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page/search", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public HttpEntity<PagedResources<QuestionItemJsonEdit>>  getBy(@RequestParam(value = "name",defaultValue = "%") String name,
                                                                   @RequestParam(value = "question",defaultValue = "%") String question,
                                                                   Pageable pageable, PagedResourcesAssembler assembler) {
        // Originally name and question was 2 separate search strings, now we search both name and questiontext for value in "question"
        Page<QuestionItemJsonEdit> questionitems = null;
        try {
            questionitems = service.findByNameLikeOrQuestionLike(question, pageable).map(F->question2Json(F));
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(assembler.toResource(questionitems), HttpStatus.OK);
    }

    private QuestionItemJsonEdit question2Json(QuestionItem questionItem){
        return  new QuestionItemJsonEdit(questionItem);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/xml/{id}", method = RequestMethod.GET)
    public String getXml(@PathVariable("id") UUID id) {
        return service.findOne(id).toDDIXml();
    }

}
