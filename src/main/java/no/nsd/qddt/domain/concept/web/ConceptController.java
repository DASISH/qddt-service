package no.nsd.qddt.domain.concept.web;

import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.concept.json.ConceptJsonEdit;
import no.nsd.qddt.domain.concept.ConceptService;
import no.nsd.qddt.domain.conceptquestionitem.ConceptQuestionItem;
import no.nsd.qddt.domain.conceptquestionitem.ConceptQuestionItemService;
import no.nsd.qddt.domain.conceptquestionitem.ParentQuestionItemId;
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

import java.io.*;
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

    private ConceptService service;
    private TopicGroupService topicGroupService;
    private ConceptQuestionItemService cqiService;

    @Autowired
    public ConceptController(ConceptService conceptService, TopicGroupService topicGroupService,ConceptQuestionItemService conceptQuestionItem) {
        this.service = conceptService;
        this.topicGroupService = topicGroupService;
        this.cqiService = conceptQuestionItem;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ConceptJsonEdit get(@PathVariable("id") UUID id) {
        return concept2Json(service.findOne(id));
    }


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ConceptJsonEdit update(@RequestBody Concept concept) {
        System.out.println(concept);
        return concept2Json(service.save(concept));
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/combine", method = RequestMethod.GET, params = { "conceptid", "questionitemid","questionitemrevision"})
    public ConceptJsonEdit addQuestionItem(@RequestParam("conceptid") UUID conceptId, @RequestParam("questionitemid") UUID questionItemId,
                                           @RequestParam("questionitemrevision") Number questionItemRevision ) {
        try {
            Concept concept = service.findOne(conceptId);
            if (questionItemRevision == null)
                questionItemRevision=0;
            concept.addConceptQuestionItem(
                new ConceptQuestionItem(
                    new ParentQuestionItemId(conceptId,questionItemId),questionItemRevision.intValue()));

            return concept2Json(service.save(concept));
        }catch (Exception ex){
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            return null;
        }
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/decombine", method = RequestMethod.GET, params = { "conceptid", "questionitemid"})
    public ConceptJsonEdit removeQuestionItem(@RequestParam("conceptid") UUID conceptId, @RequestParam("questionitemid") UUID questionItemId) {
        Concept concept=null;
        try{
            concept = service.findOne(conceptId);
            concept.removeQuestionItem(questionItemId);
            cqiService.delete(new ParentQuestionItemId(conceptId,questionItemId));
            return concept2Json(service.save(concept));
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println( ex.getMessage());
            return concept2Json(concept);
        }
    }


    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create/by-parent/{uuid}", method = RequestMethod.POST)
    public ConceptJsonEdit createByParent(@RequestBody Concept concept, @PathVariable("uuid") UUID parentId) {

        Concept parent = service.findOne(parentId);
        parent.addChildren(concept);
        ConceptJsonEdit parentJson = concept2Json(service.save(parent));

        return parentJson.getChildren().stream()
                .filter(c -> c.getName() == concept.getName()).findFirst()
                .orElseThrow( ()-> new ResourceNotFoundException(0, Concept.class));
    }


    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create/by-topicgroup/{uuid}", method = RequestMethod.POST)
    public ConceptJsonEdit createByTopic(@RequestBody Concept concept, @PathVariable("uuid") UUID topicId) {
        topicGroupService.findOne(topicId).addConcept(concept);
        return concept2Json(service.save(concept));
    }


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public void delete(@PathVariable("id") UUID id) {
        service.delete(id);
    }


    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public HttpEntity<PagedResources<ConceptJsonEdit>> getAll(Pageable pageable, PagedResourcesAssembler assembler) {

        Page<ConceptJsonEdit> concepts = service.findAllPageable(pageable).map(F->new ConceptJsonEdit(F));
        return new ResponseEntity<>(assembler.toResource(concepts), HttpStatus.OK);
    }


    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page/by-topicgroup/{topicId}", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public HttpEntity<PagedResources<ConceptJsonEdit>> getbyTopicId(@PathVariable("topicId") UUID id, Pageable pageable, PagedResourcesAssembler assembler) {

        Page<ConceptJsonEdit> concepts = service.findByTopicGroupPageable(id,pageable).map(F->new ConceptJsonEdit(F));
        return new ResponseEntity<>(assembler.toResource(concepts), HttpStatus.OK);
    }


    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page/search", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public HttpEntity<PagedResources<ConceptJsonEdit>> getBy(@RequestParam(value = "name",defaultValue = "%") String name,
                                                        Pageable pageable, PagedResourcesAssembler assembler) {

        name = name.replace("*","%");
        Page<ConceptJsonEdit> items = service.findByNameAndDescriptionPageable(name,name, pageable).map(F->new ConceptJsonEdit(F));
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
        return service.findOne(id).toDDIXml();
    }

//    @ResponseBody()
//    @RequestMapping(value="/pdf/{id}", method=RequestMethod.GET,produces = MediaType.APPLICATION_OCTET_STREAM_VALUE )
//    public byte[] getasPdf(@PathVariable("id") UUID id)  {
//        try {
//            ByteArrayOutputStream pdfStream = service.findOne(id).makePdf();
//            return pdfStream.toByteArray();
//
//        } catch (Exception ex){
//            ex.printStackTrace();
//        }
//    }


    @RequestMapping(value="/pdf/{id}", method=RequestMethod.GET,produces = MediaType.APPLICATION_OCTET_STREAM_VALUE )
    public @ResponseBody
    ResponseEntity<ByteArrayInputStream > getPdf(@PathVariable("id") UUID id) {
        try {
            ByteArrayOutputStream pdfStream = service.findOne(id).makePdf();
            return ResponseEntity
                    .ok()
                    .contentLength(pdfStream.size())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new ByteArrayInputStream (pdfStream.toByteArray()));
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }

    private ConceptJsonEdit concept2Json(Concept concept){
        return  new ConceptJsonEdit(concept);
    }

}
