package no.nsd.qddt.controller;

import no.nsd.qddt.domain.Question;
import no.nsd.qddt.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@RestController
@RequestMapping("/question")
public class QuestionController implements BaseHierachyController<Question> {

    private QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService){
        this.questionService = questionService;
    }

    @RequestMapping(value = "/{id}/all", method = RequestMethod.GET)
    public HttpEntity<PagedResources<Question>> getInstrumentThread(
            @PathVariable("id") Long id,Pageable pageable, PagedResourcesAssembler assembler)
    {

        Page<Question> questions = questionService.findQuestionInstrumentPageable(id, pageable);
        return new ResponseEntity<>(assembler.toResource(questions), HttpStatus.OK);
    }


    //TODO reevaluere hvordan dette skal gjøres

    @RequestMapping(value = "/{id}/all/concept", method = RequestMethod.GET)
    public HttpEntity<PagedResources<Question>> getConceptThread(
            @PathVariable("id") Long id,Pageable pageable, PagedResourcesAssembler assembler)
    {

        Page<Question> questions = questionService.findQuestionConceptPageable(id, pageable);
        return new ResponseEntity<>(assembler.toResource(questions), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/all/Question", method = RequestMethod.GET)
    public HttpEntity<PagedResources<Question>> getSiblingsThread(
            @PathVariable("id") Long id,Pageable pageable, PagedResourcesAssembler assembler)
    {

        Page<Question> questions = questionService.findByParentPageable(id, pageable);
        return new ResponseEntity<>(assembler.toResource(questions), HttpStatus.OK);
    }



    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Question> getAll() {
        return questionService.findAll();
    }

    @Override
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public HttpEntity<PagedResources<Question>> getAll(Pageable pageable, PagedResourcesAssembler assembler) {

        Page<Question> questions = questionService.findAll(pageable);
        return new ResponseEntity<>(assembler.toResource(questions), HttpStatus.OK);
    }


    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Question getOne(@PathVariable("id") Long id) {

        return questionService.findById(id);
    }


    @Override
    @RequestMapping(value = "/UUID/{id}", method = RequestMethod.GET)
    public Question getOne(@PathVariable("id") UUID id) {
        return questionService.findById(id);
    }

    @Override
    public HttpEntity<PagedResources<Revision<Integer, Question>>> getAllRevisionsPageable(Long id, Pageable pageable) {
        return null;
    }


    @Override
    public Revision<Integer, Question> getEntityAtRevision(Long id, Integer revision) {
        return null;
    }

    @Override
    public Revision<Integer, Question> getLastChange(Long id) {
        return null;
    }


    @Override
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Question create(Question question) {

        return questionService.save(question);
    }


    @Override
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public void delete(Question instance) {
        questionService.delete(instance);
    }

    @Override
    @RequestMapping(value = "/{id}/all", method = RequestMethod.GET)
    public HttpEntity<PagedResources<Question>> getThreadbyId(@PathVariable("id") Long id, Pageable pageable, PagedResourcesAssembler assembler) {

        Page<Question> questions = questionService.findByParentPageable(id, pageable);
        return new ResponseEntity<>(assembler.toResource(questions), HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/UUID/{id}/all", method = RequestMethod.GET)
    public HttpEntity<PagedResources<Question>> getThreadbyGuid(@PathVariable("id") UUID id, Pageable pageable, PagedResourcesAssembler assembler) {

//        Page<Question> questions = questionService.findByParentPageable(id,pageable);
//        return new ResponseEntity<>(assembler.toResource(questions), HttpStatus.OK);
        return null;
    }

}
