package no.nsd.qddt.domain.question.web;

import no.nsd.qddt.domain.question.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Stig Norland
 */
@RestController
@RequestMapping("/question")
public class QuestionController {

    private QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService){
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


////    HierachyController<Question>
//    @RequestMapping(value = "/{id}/page/thread", method = RequestMethod.GET)
//    public HttpEntity<PagedResources<Question>> getThreadbyId(Long id, Pageable pageable, PagedResourcesAssembler assembler) {
//
//        Page<Question> questions = questionService.findByParentPageable(id, pageable);
//        return new ResponseEntity<>(assembler.toResource(questions), HttpStatus.OK);
//    }
//
////    HierachyController<Question>
//    @RequestMapping(value = "/UUID{id}/page/thread", method = RequestMethod.GET)
//    public HttpEntity<PagedResources<Question>> getThreadbyGuid(UUID id, Pageable pageable, PagedResourcesAssembler assembler) {
//
//        Page<Question> questions = questionService.findByParentPageable(id, pageable);
//        return new ResponseEntity<>(assembler.toResource(questions), HttpStatus.OK);
//    }
}
