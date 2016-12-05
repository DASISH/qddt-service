package no.nsd.qddt.domain.questionItem.web;

import no.nsd.qddt.domain.question.QuestionService;
import no.nsd.qddt.domain.questionItem.QuestionItem;
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

    private QuestionItemService questionItemService;
    private QuestionService questionService;

    @Autowired
    public QuestionItemController(QuestionItemService questionItemService,QuestionService questionService){
        this.questionItemService = questionItemService;
        this.questionService = questionService;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public QuestionItem get(@PathVariable("id") UUID id) {
        return questionItemService.findOne(id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public QuestionItem update(@RequestBody QuestionItem instance) {
        return questionItemService.save(instance);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public QuestionItem create(@RequestBody QuestionItem instance) {

        instance.setQuestion(
                questionService.save(
                        instance.getQuestion()));

        return questionItemService.save(instance);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public void delete(@PathVariable("id") UUID id) {
        questionItemService.delete(id);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public HttpEntity<PagedResources<QuestionItem>> getAll(Pageable pageable, PagedResourcesAssembler assembler) {

        Page<QuestionItem> questionitems =
                questionItemService.findAllPageable(pageable);

        return new ResponseEntity<>(assembler.toResource(questionitems), HttpStatus.OK);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page/search", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public HttpEntity<PagedResources<QuestionItem>>  getBy(@RequestParam(value = "name",defaultValue = "%") String name,
                                                           @RequestParam(value = "question",defaultValue = "%") String question,
                                                       Pageable pageable, PagedResourcesAssembler assembler) {
        // Originally name and question was 2 separate search strings, now we search both name and questiontext for value in "question"
        Page<QuestionItem> questionitems = null;
        try {
            questionitems = questionItemService.findByNameLikeOrQuestionLike(question, pageable);
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(assembler.toResource(questionitems), HttpStatus.OK);
    }

}
