package no.nsd.qddt.domain.concept.web;

import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.concept.ConceptService;
import no.nsd.qddt.domain.question.Question;
import no.nsd.qddt.domain.question.QuestionService;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.questionItem.QuestionItemService;
import no.nsd.qddt.domain.topicgroup.TopicGroupService;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.hibernate.internal.util.xml.ErrorLogger;
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

import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */
@RestController
@RequestMapping("/concept")
public class ConceptController {

    private ConceptService conceptService;
    private TopicGroupService topicGroupService;
    private QuestionItemService questionItemService;

    @Autowired
    public ConceptController(ConceptService conceptService, TopicGroupService topicGroupService, QuestionItemService questionItemService) {
        this.conceptService = conceptService;
        this.topicGroupService = topicGroupService;
        this.questionItemService = questionItemService;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Concept get(@PathVariable("id") UUID id) {
        return conceptService.findOne(id);
    }


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Concept update(@RequestBody Concept concept) {
        return conceptService.save(concept);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/combine", method = RequestMethod.GET, params = { "concept", "questionitem"})
    public Concept addQuestionItem(@RequestParam("concept") UUID conceptId, @RequestParam("questionitem") UUID questionItemId) {
        try {
            Concept concept = conceptService.findOne(conceptId);
            QuestionItem questionItem = questionItemService.findOne(questionItemId);
            concept.addQuestionItem(questionItem);
            return conceptService.save(concept);
        }catch (Exception ex){
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            return null;
        }
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/decombine", method = RequestMethod.GET, params = { "concept", "questionitem"})
    public Concept removeQuestionItem(@RequestParam("concept") UUID conceptId, @RequestParam("questionitem") UUID questionItemId) {
        try{
            Concept concept  = conceptService.findOne(conceptId);
            QuestionItem questionItem =  questionItemService.findOne(questionItemId);
            questionItem.updateStatusQI(concept);
            concept.getQuestionItems().removeIf(qi ->qi.equals(questionItem));
            return conceptService.save(concept);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println( ex.getMessage());
            return null;
        }
    }




    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create/by-parent/{uuid}", method = RequestMethod.POST)
    public Concept createByParent(@RequestBody Concept concept,@PathVariable("uuid") UUID parentId) {

        Concept parent = conceptService.findOne(parentId);
        parent.addChildren(concept);
        parent = conceptService.save(parent);

        return parent.getChildren().stream()
                .filter(c -> c.getName() == concept.getName()).findFirst()
                .orElseThrow( ()-> new ResourceNotFoundException(0, Concept.class));
    }


    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create/by-topicgroup/{uuid}", method = RequestMethod.POST)
    public Concept createByTopic(@RequestBody Concept concept, @PathVariable("uuid") UUID topicId) {
        topicGroupService.findOne(topicId).addConcept(concept);
        return conceptService.save(concept);
    }


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public void delete(@PathVariable("id") UUID id) {
        conceptService.delete(id);
    }


    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public HttpEntity<PagedResources<Concept>> getAll(Pageable pageable, PagedResourcesAssembler assembler) {

        Page<Concept> concepts = conceptService.findAllPageable(pageable);
        return new ResponseEntity<>(assembler.toResource(concepts), HttpStatus.OK);
    }


    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page/by-topicgroup/{topicId}", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public HttpEntity<PagedResources<Concept>> getbyTopicId(@PathVariable("topicId") UUID id, Pageable pageable, PagedResourcesAssembler assembler) {

        Page<Concept> concepts = conceptService.findByTopicGroupPageable(id,pageable);
        return new ResponseEntity<>(assembler.toResource(concepts), HttpStatus.OK);
    }



    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/list/by-QuestionItem/{qiId}", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<Concept> getbyQuestionItemId(@PathVariable("qiId") UUID id) {
        return conceptService.findByQuestionItem(id);
    }


}
