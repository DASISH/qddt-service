package no.nsd.qddt.domain.concept.web;

import no.nsd.qddt.domain.AbstractController;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.concept.ConceptService;
import no.nsd.qddt.domain.concept.json.ConceptJsonEdit;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import no.nsd.qddt.domain.topicgroup.TopicGroupService;
import no.nsd.qddt.domain.xml.XmlDDIFragmentAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */
@RestController
@RequestMapping("/concept")
public class ConceptController extends AbstractController {

    private final ConceptService service;
    private final TopicGroupService topicGroupService;

    @Autowired
    public ConceptController(ConceptService conceptService, TopicGroupService topicGroupService) {
        this.service = conceptService;
        this.topicGroupService = topicGroupService;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ConceptJsonEdit get(@PathVariable("id") UUID id) {
        return concept2Json(service.findOne(id));
    }


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ConceptJsonEdit update(@RequestBody Concept concept) {
        LOG.debug(concept.toString());
        return concept2Json(service.save(concept));
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/combine", method = RequestMethod.POST, params = { "parentId", "questionitemid","questionitemrevision"})
    public ConceptJsonEdit addQuestionItem(@RequestParam("parentId") UUID conceptId, @RequestParam("questionitemid") UUID questionItemId,
                                           @RequestParam("questionitemrevision") Number questionItemRevision ) {
        try {
            Concept concept = service.findOne(conceptId);
            if (questionItemRevision == null)
                questionItemRevision=0;

            concept.addQuestionItem(questionItemId, questionItemRevision.intValue()  );

            return concept2Json(service.save(concept));
        } catch (Exception ex) {
            LOG.error("addQuestionItem",ex);
            throw ex;
        }
    }


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/decombine", method = RequestMethod.POST, params = { "parentId", "questionitemid"})
    public ConceptJsonEdit removeQuestionItem(@RequestParam("parentId") UUID conceptId, @RequestParam("questionitemid") UUID questionItemId,
                                               @RequestParam("questionitemrevision") Number questionItemRevision) {
        Concept concept=null;
        try{
            concept = service.findOne(conceptId);
            concept.removeQuestionItem(questionItemId,questionItemRevision.intValue());
            return concept2Json(service.save(concept));
        } catch (Exception ex) {
            LOG.error("removeQuestionItem",ex);
            return concept2Json(concept);
        }
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/copy/{uuid}/{rev}/{parentUuid}", method = RequestMethod.POST)
    public ConceptJsonEdit copy(@PathVariable("uuid") UUID sourceId ,
                                @PathVariable("rev") Integer sourceRev,
                                @PathVariable("parentUuid") UUID parentId)  {
        return concept2Json(
            service.save(
                service.copy( sourceId, sourceRev, parentId ) ) );
    }


    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create/{uuid}", method = RequestMethod.POST)
    public ConceptJsonEdit createByParent(@RequestBody Concept concept, @PathVariable("uuid") UUID parentId) {

        if (service.exists( parentId )) {
            return concept2Json(
                service.save(
                    service
                        .findOne(parentId)
                        .addChildren(concept)));

        } else {
            return concept2Json(
                service.save(
                    topicGroupService
                        .findOne(parentId)
                        .addConcept(concept)));
        }
    }


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") UUID id) {
        service.delete(id);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public HttpEntity<PagedResources<ConceptJsonEdit>> getAll(Pageable pageable, PagedResourcesAssembler assembler) {

        Page<ConceptJsonEdit> concepts = service.findAllPageable(pageable).map(ConceptJsonEdit::new);
        return new ResponseEntity<>(assembler.toResource(concepts), HttpStatus.OK);
    }


    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page/by-parent/{topicId}", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public HttpEntity<PagedResources<ConceptJsonEdit>> getbyPageTopicId(@PathVariable("topicId") UUID id, Pageable pageable, PagedResourcesAssembler assembler) {

        Page<ConceptJsonEdit> concepts = service.findByTopicGroupPageable(id,pageable).map(ConceptJsonEdit::new);
        return new ResponseEntity<>(assembler.toResource(concepts), HttpStatus.OK);
    }


    @RequestMapping(value = "/list/by-parent/{topicId}", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<ConceptJsonEdit> getbyTopicId(@PathVariable("topicId") UUID id) {

        return service.findByTopicGroup(id).stream().map( p -> new ConceptJsonEdit(p) ).collect( Collectors.toList());
    }


    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page/search", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public HttpEntity<PagedResources<ConceptJsonEdit>> getBy(@RequestParam(value = "name",defaultValue = "%") String name,
                                                             @RequestParam(value = "description",defaultValue = "%") String description,
                                                        Pageable pageable, PagedResourcesAssembler assembler) {

        Page<ConceptJsonEdit> items = service.findByNameAndDescriptionPageable(name,description, pageable).map(ConceptJsonEdit::new);
        return new ResponseEntity<>(assembler.toResource(items), HttpStatus.OK);
    }


    @ResponseStatus(value = HttpStatus.NOT_IMPLEMENTED)
    @RequestMapping(value = "/list/by-QuestionItem/{qiId}", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<Concept> getByQuestionItemId(@PathVariable("qiId") UUID id) {
        return  service.findByQuestionItem(id,null);
    }


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/xml/{id}", method = RequestMethod.GET)
    public String getXml(@PathVariable("id") UUID id) {
        return new XmlDDIFragmentAssembler<Concept>(service.findOne(id)).compileToXml();
    }


    @ResponseBody
    @RequestMapping(value = "/pdf/{id}", method = RequestMethod.GET, produces = "application/pdf")
    public byte[] getPdf(@PathVariable("id") UUID id) {
        return service.findOne(id).makePdf().toByteArray();
    }

    private ConceptJsonEdit concept2Json(Concept concept){
        return  new ConceptJsonEdit(concept);
    }

}
