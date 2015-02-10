package no.nsd.qddt.controller;

import no.nsd.qddt.domain.Question;
import no.nsd.qddt.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author Stig Norland
 */
@RestController
@RequestMapping("/question")
public class QuestionController extends AbstractAuditController<Question>  {

    private QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService){
        super(questionService);
        this.questionService = questionService;
    }

//    @RequestMapping(value = "/{id}/all", method = RequestMethod.GET)
//    public HttpEntity<PagedResources<Question>> getInstrumentThread(
//            @PathVariable("id") Long id,Pageable pageable, PagedResourcesAssembler assembler)
//    {
//
//        Page<Question> questions = questionService.findQuestionInstrument(id, pageable);
//        return new ResponseEntity<>(assembler.toResource(questions), HttpStatus.OK);
//    }


    //TODO reevaluere hvordan dette skal gjøres

//    @RequestMapping(value = "/{id}/all/concept", method = RequestMethod.GET)
//    public HttpEntity<PagedResources<Question>> getConceptThread(
//            @PathVariable("id") Long id,Pageable pageable, PagedResourcesAssembler assembler)
//    {
//
//        Page<Question> questions = questionService.findQuestionConceptPageable(id, pageable);
//        return new ResponseEntity<>(assembler.toResource(questions), HttpStatus.OK);
//    }


//    HierachyController<Question>
    @RequestMapping(value = "/{id}/page/thread", method = RequestMethod.GET)
    public HttpEntity<PagedResources<Question>> getThreadbyId(Long id, Pageable pageable, PagedResourcesAssembler assembler) {

        Page<Question> questions = questionService.findByParentPageable(id, pageable);
        return new ResponseEntity<>(assembler.toResource(questions), HttpStatus.OK);
    }

//    HierachyController<Question>
    @RequestMapping(value = "/UUID{id}/page/thread", method = RequestMethod.GET)
    public HttpEntity<PagedResources<Question>> getThreadbyGuid(UUID id, Pageable pageable, PagedResourcesAssembler assembler) {

        Page<Question> questions = questionService.findByParentPageable(id, pageable);
        return new ResponseEntity<>(assembler.toResource(questions), HttpStatus.OK);
    }
}
