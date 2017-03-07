package no.nsd.qddt.domain.concept.web;

import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.concept.ConceptJsonEdit;
import no.nsd.qddt.domain.concept.ConceptService;
import no.nsd.qddt.domain.conceptquestionitem.ConceptQuestionItemService;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.questionItem.QuestionItemService;
import no.nsd.qddt.domain.topicgroup.TopicGroupService;
import no.nsd.qddt.exception.ResourceNotFoundException;
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

import java.util.ArrayList;
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
    private ConceptQuestionItemService conceptQuestionItemService;

    @Autowired
    public ConceptController(ConceptService conceptService, TopicGroupService topicGroupService,
                             QuestionItemService questionItemService,ConceptQuestionItemService conceptQuestionItemService) {
        this.conceptService = conceptService;
        this.topicGroupService = topicGroupService;
        this.questionItemService = questionItemService;
        this.conceptQuestionItemService = conceptQuestionItemService;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ConceptJsonEdit get(@PathVariable("id") UUID id) {
        return concept2Json(conceptService.findOne(id));
    }


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ConceptJsonEdit update(@RequestBody Concept concept) {
        return concept2Json(conceptService.save(concept));
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/combine", method = RequestMethod.GET, params = { "concept", "questionitem"})
    public ConceptJsonEdit addQuestionItem(@RequestParam("concept") UUID conceptId, @RequestParam("questionitem") UUID questionItemId) {
        try {
            Concept concept = conceptService.findOne(conceptId);
            QuestionItem questionItem = questionItemService.findOne(questionItemId);
            concept.addQuestionItem(questionItem);
            return concept2Json(conceptService.save(concept));
        }catch (Exception ex){
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            return null;
        }
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/decombine", method = RequestMethod.GET, params = { "concept", "questionitem"})
    public ConceptJsonEdit removeQuestionItem(@RequestParam("concept") UUID conceptId, @RequestParam("questionitem") UUID questionItemId) {
        Concept concept=null;
        try{
            conceptQuestionItemService.findByConceptQuestionItem(conceptId,questionItemId).forEach(c->
                    conceptQuestionItemService.delete(c.getId()));
            return concept2Json(conceptService.findOne(conceptId));
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println( ex.getMessage());
            return concept2Json(concept);
        }
    }


    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create/by-parent/{uuid}", method = RequestMethod.POST)
    public ConceptJsonEdit createByParent(@RequestBody Concept concept, @PathVariable("uuid") UUID parentId) {

        Concept parent = conceptService.findOne(parentId);
        parent.addChildren(concept);
        ConceptJsonEdit parentJson = concept2Json(conceptService.save(parent));

        return parentJson.getChildren().stream()
                .filter(c -> c.getName() == concept.getName()).findFirst()
                .orElseThrow( ()-> new ResourceNotFoundException(0, Concept.class));
    }


    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create/by-topicgroup/{uuid}", method = RequestMethod.POST)
    public ConceptJsonEdit createByTopic(@RequestBody Concept concept, @PathVariable("uuid") UUID topicId) {
        topicGroupService.findOne(topicId).addConcept(concept);
        return concept2Json(conceptService.save(concept));
    }


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public void delete(@PathVariable("id") UUID id) {
        conceptService.delete(id);
    }


    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public HttpEntity<PagedResources<ConceptJsonEdit>> getAll(Pageable pageable, PagedResourcesAssembler assembler) {

        Page<ConceptJsonEdit> concepts = conceptService.findAllPageable(pageable).map(F->new ConceptJsonEdit(F));
        return new ResponseEntity<>(assembler.toResource(concepts), HttpStatus.OK);
    }


    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page/by-topicgroup/{topicId}", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public HttpEntity<PagedResources<ConceptJsonEdit>> getbyTopicId(@PathVariable("topicId") UUID id, Pageable pageable, PagedResourcesAssembler assembler) {

        Page<ConceptJsonEdit> concepts = conceptService.findByTopicGroupPageable(id,pageable).map(F->new ConceptJsonEdit(F));
        return new ResponseEntity<>(assembler.toResource(concepts), HttpStatus.OK);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page/search", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public HttpEntity<PagedResources<ConceptJsonEdit>> getBy(@RequestParam(value = "name",defaultValue = "%") String name,
                                                        Pageable pageable, PagedResourcesAssembler assembler) {

        Page<ConceptJsonEdit> items;
        name = name.replace("*","%");

        items = conceptService.findByNameAndDescriptionPageable(name,name, pageable).map(F->new ConceptJsonEdit(F));

        return new ResponseEntity<>(assembler.toResource(items), HttpStatus.OK);
    }

    @ResponseStatus(value = HttpStatus.NOT_IMPLEMENTED)
    @RequestMapping(value = "/list/by-QuestionItem/{qiId}", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<Concept> getbyQuestionItemId(@PathVariable("qiId") UUID id) {
        return  new ArrayList<>(); // conceptService.findByQuestionItem(id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/xml/{id}", method = RequestMethod.GET)
    public String getXml(@PathVariable("id") UUID id) {
        return conceptService.findOne(id).toDDIXml();
    }




    private ConceptJsonEdit concept2Json(Concept concept){
        return  new ConceptJsonEdit(concept);
    }

}
