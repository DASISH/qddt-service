package no.nsd.qddt.domain.concept.web;

import no.nsd.qddt.domain.AbstractController;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.concept.ConceptService;
import no.nsd.qddt.domain.concept.json.ConceptJsonEdit;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import no.nsd.qddt.domain.topicgroup.TopicGroupService;
import no.nsd.qddt.domain.xml.XmlReport;
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
        return concept2Json(service.save(concept));
    }


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/list/{parentId}", method = RequestMethod.POST)
    public List<ConceptJsonEdit> updateAll(@RequestBody List<Concept> concepts, 
        @PathVariable(name = "parentId", required= false) UUID id) {

        if (id!=null) {
            if (service.exists(id)) {
                Concept concept = service.findOne(id);
                concept.setChildren(concepts);
                concepts = service.save(concept).getChildren();
            } else {
                TopicGroup tg = topicGroupService.findOne(id);
                tg.setConcepts(concepts);
                concepts = topicGroupService.save(tg).getConcepts();
            }
        } else 
            concepts=  service.saveAll( concepts);

        return concepts.stream().filter( f -> f != null)
            .map( this::concept2Json )
            .collect( Collectors.toList());
    }

    @RequestMapping(value = "/list/by-parent/{uuid}", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<ConceptJsonEdit> getList(@PathVariable("uuid") UUID parentid) {
        return this.topicGroupService.findOne( parentid ).getConcepts()
            .parallelStream().filter( f -> f != null).map(this::concept2Json).collect( Collectors.toList());
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
//
//    @ResponseStatus(value = HttpStatus.CREATED)
//    @RequestMapping(value = "/move/{targetId}/{index}/{sourceId}", method = RequestMethod.POST)
//    public ConceptJsonEdit moveTo(@PathVariable("targetId") UUID targetId,
//                                  @PathVariable("index") Integer index,
//                                  @PathVariable("sourceId") UUID sourceId)  {
//        return concept2Json(service.moveTo(targetId, index, sourceId));
//    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create/{uuid}", method = RequestMethod.POST)
    public ConceptJsonEdit createByParent(@RequestBody Concept concept, @PathVariable("uuid") UUID parentId) {

        // changeing to save child....

        if (service.exists( parentId )) {
             service.findOne( parentId ).addChildren( concept.getIndex(), concept );
            return concept2Json( service.save( concept ) );

//            return parentJson.getChildren().stream()
//                .filter( c -> Objects.equals( c.getName(), concept.getName() ) ).findFirst()
//                .orElseThrow( () -> new ResourceNotFoundException( 0, Concept.class ) );
        } else {
            topicGroupService.findOne(parentId).addConcept(concept.getIndex(), concept);
            return concept2Json(service.save(concept));
        }
    }


//    @ResponseStatus(value = HttpStatus.CREATED)
//    @RequestMapping(value = "/create/{uuid}", method = RequestMethod.POST)
//    public ConceptJsonEdit createByTopic(@RequestBody Concept concept, @PathVariable("uuid") UUID topicId) {
//
//        topicGroupService.findOne(topicId).addConcept(concept);
//        return concept2Json(service.save(concept));
//    }


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
    public HttpEntity<PagedResources<ConceptJsonEdit>> getbyTopicId(@PathVariable("topicId") UUID id, Pageable pageable, PagedResourcesAssembler assembler) {

        Page<ConceptJsonEdit> concepts = service.findByTopicGroupPageable(id,pageable).map(ConceptJsonEdit::new);
        return new ResponseEntity<>(assembler.toResource(concepts), HttpStatus.OK);
    }


    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page/search", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public HttpEntity<PagedResources<ConceptJsonEdit>> getBy(@RequestParam(value = "name",defaultValue = "%") String name,
                                                             @RequestParam(value = "description",defaultValue = "%") String description,
                                                        Pageable pageable, PagedResourcesAssembler assembler) {

        Page<ConceptJsonEdit> items = service.findByNameAndDescriptionPageable(name,description, pageable).map(ConceptJsonEdit::new);
        return new ResponseEntity<>(assembler.toResource(items), HttpStatus.OK);
    }


//    @ResponseStatus(value = HttpStatus.NOT_IMPLEMENTED)
//    @RequestMapping(value = "/list/by-QuestionItem/{qiId}", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
//    public List<Concept> getByQuestionItemId(@PathVariable("qiId") UUID id) {
//        return  service.findByQuestionItem(id,null);
//    }


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/xml/{id}", method = RequestMethod.GET)
    public String getXml(@PathVariable("id") UUID id) {
        return new XmlReport(service.findOne(id)).get();
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
