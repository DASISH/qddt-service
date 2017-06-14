package no.nsd.qddt.domain.topicgroup.web;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.concept.json.ConceptJsonEdit;
import no.nsd.qddt.domain.conceptquestionitem.ConceptQuestionItem;
import no.nsd.qddt.domain.conceptquestionitem.ConceptQuestionItemService;
import no.nsd.qddt.domain.conceptquestionitem.ParentQuestionItemId;
import no.nsd.qddt.domain.publication.Publication;
import no.nsd.qddt.domain.study.StudyService;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import no.nsd.qddt.domain.topicgroup.TopicGroupService;
import no.nsd.qddt.domain.topicgroup.json.TopicGroupRevisionJson;
import no.nsd.qddt.domain.topicgroupquestionitem.TopicGroupQuestionItem;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * @author Stig Norland
 */
@RestController
@RequestMapping("/topicgroup")
public class TopicGroupController {

    private TopicGroupService service;
    private StudyService studyService;
    private ConceptQuestionItemService cqiService;


    @Autowired
    public TopicGroupController(TopicGroupService service, StudyService studyService, ConceptQuestionItemService conceptQuestionItemService) {
        this.service = service;
        this.studyService = studyService;
        this.cqiService = conceptQuestionItemService;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public TopicGroup get(@PathVariable("id") UUID id) {
        return service.findOne(id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public TopicGroup update(@RequestBody TopicGroup instance) {
        return service.save(instance);
    }


    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create/{studyId}", method = RequestMethod.POST)
    public TopicGroup create(@RequestBody TopicGroup instance, @PathVariable("studyId")UUID studyId) {

        if(instance.getStudy() == null ){
            instance.setStudy(studyService.findOne(studyId));
        }
        try {
            instance = service.save(instance);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return instance;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public void delete(@PathVariable("id") UUID id){
        service.delete(id);
    }


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/list/by-study/{uuid}", method = RequestMethod.GET)
    public List<TopicGroupRevisionJson> findByStudy(@PathVariable("uuid") UUID studyId) {
        try {
            return service.findByStudyId(studyId).stream().map(i->postLoad(i)).collect(Collectors.toList());
        } catch (Exception ex){
            System.out.println("findByStudy Exception");
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }


    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page/search", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public HttpEntity<PagedResources<TopicGroupRevisionJson>> getBy(@RequestParam(value = "name",defaultValue = "%") String name,
                                                         Pageable pageable, PagedResourcesAssembler assembler) {
        name = name.replace("*","%");
        Page<TopicGroupRevisionJson> items =
                service.findByNameAndDescriptionPageable(name,name, pageable).map(i->postLoad(i));

        return new ResponseEntity<>(assembler.toResource(items), HttpStatus.OK);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/combine", method = RequestMethod.GET, params = { "topicid", "questionitemid","questionitemrevision"})
    public TopicGroupRevisionJson addQuestionItem(@RequestParam("topicid") UUID topicId, @RequestParam("questionitemid") UUID questionItemId,
                                           @RequestParam("questionitemrevision") Number questionItemRevision ) {
        try {
            TopicGroup topicGroup = service.findOne(topicId);
            if (questionItemRevision == null)
                questionItemRevision=0;
            topicGroup.addConceptQuestionItem(
                    new TopicGroupQuestionItem(
                            new ParentQuestionItemId(topicId,questionItemId),questionItemRevision.intValue()));

            return new TopicGroupRevisionJson(service.save(topicGroup));
        }catch (Exception ex){
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            return null;
        }
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/decombine", method = RequestMethod.GET, params = { "topicid", "questionitemid"})
    public TopicGroupRevisionJson removeQuestionItem(@RequestParam("topicid") UUID topicId, @RequestParam("questionitemid") UUID questionItemId) {
        TopicGroup topicGroup =null;
        try{
            topicGroup = service.findOne(topicId);
            topicGroup.removeQuestionItem(questionItemId);
            cqiService.delete(new ParentQuestionItemId(topicId,questionItemId));
            return new TopicGroupRevisionJson(service.save(topicGroup));
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println( ex.getMessage());
            return new TopicGroupRevisionJson(topicGroup);
        }
    }

    @RequestMapping(value="/pdf/{id}", method=RequestMethod.GET,produces = MediaType.APPLICATION_OCTET_STREAM_VALUE )
    public @ResponseBody
    ResponseEntity<ByteArrayInputStream> getPdf(@PathVariable("id") UUID id) {
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

    private TopicGroupRevisionJson postLoad(TopicGroup topicGroup){
        return new TopicGroupRevisionJson(topicGroup);
    }


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/xml/{id}", method = RequestMethod.GET)
    public String getXml(@PathVariable("id") UUID id) {
        return service.findOne(id).toDDIXml();
    }
}
