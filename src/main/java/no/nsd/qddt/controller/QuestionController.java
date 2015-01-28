package no.nsd.qddt.controller;

import no.nsd.qddt.domain.Question;
import no.nsd.qddt.domain.instrument.InstrumentQuestion;
import no.nsd.qddt.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Stig Norland
 */
//@RestController
//@RequestMapping("/question")
public class QuestionController {

//    private QuestionService questionService;
//
//    @Autowired
//    public QuestionController(QuestionService questionService){
//        this.questionService = questionService;
//    }

    //    @RequestMapping(value = "/{id}/all", method = RequestMethod.GET)
//    public HttpEntity<PagedResources<InstrumentQuestion>> getThread(
//            @PathVariable("id") Long id,Pageable pageable, PagedResourcesAssembler assembler)
//    {
//        Page<InstrumentQuestion> instrumentQuestions = instrumentQuestionService (id, pageable);
//        return new ResponseEntity<>(assembler.toResource(instrumentQuestions), HttpStatus.OK);
//    }

//    @RequestMapping(value = "/list", method = RequestMethod.GET)
//    public List<Question> getAll() {
//        return questionService.findAll();
//    }
//
//
//    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
//    public Question getOne(@PathVariable("id") Long id) {
//
//        return questionService.findById(id);
//    }
//
//    @RequestMapping(value = "/create", method = RequestMethod.POST)
//    public Question create(Question question) {
//
//        return questionService.save(question);
//    }
}
