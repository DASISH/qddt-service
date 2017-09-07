package no.nsd.qddt.domain.topicgroup.web;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.conceptquestionitem.ConceptQuestionItemService;
import no.nsd.qddt.domain.conceptquestionitem.ParentQuestionItemId;
import no.nsd.qddt.domain.study.StudyService;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import no.nsd.qddt.domain.topicgroup.TopicGroupService;
import no.nsd.qddt.domain.topicgroup.json.TopicGroupRevisionJson;
import no.nsd.qddt.domain.topicgroupquestionitem.TopicGroupQuestionItem;
import no.nsd.qddt.domain.topicgroupquestionitem.TopicGroupQuestionItemService;
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

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * @author Stig Norland
 */
@RestController
@RequestMapping("/topicgroup")
public class TopicGroupController {

    private final TopicGroupService service;
    private final StudyService studyService;
    private final TopicGroupQuestionItemService cqiService;


    @Autowired
    public TopicGroupController(TopicGroupService service, StudyService studyService, TopicGroupQuestionItemService topicGroupQuestionItemService) {
        this.service = service;
        this.studyService = studyService;
        this.cqiService = topicGroupQuestionItemService;
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
            instance.getStudy().setChangeKind(AbstractEntityAudit.ChangeKind.UPDATED_HIERARCHY_RELATION);
        }
        try {
            instance = service.save(instance);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return instance;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") UUID id){
        service.delete(id);
    }


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/list/by-study/{uuid}", method = RequestMethod.GET)
    public List<TopicGroupRevisionJson> findByStudy(@PathVariable("uuid") UUID studyId) {
        try {
            return service.findByStudyId(studyId).stream()
                    .map(this::postLoad)
                    .collect(Collectors.toList());
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
                service.findByNameAndDescriptionPageable(name,name, pageable).map(this::postLoad);

        return new ResponseEntity<>(assembler.toResource(items), HttpStatus.OK);
    }


    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/combine", method = RequestMethod.POST, params = { "topicid", "questionitemid","questionitemrevision"})
    public TopicGroup addQuestionItem(@RequestParam("topicid") UUID topicId, @RequestParam("questionitemid") UUID questionItemId,
                                           @RequestParam("questionitemrevision") Number questionItemRevision ) {
        try {
            TopicGroup topicGroup = service.findOne(topicId);
            if (questionItemRevision == null)
                questionItemRevision=0;
            topicGroup.addTopicQuestionItem(
                    new TopicGroupQuestionItem(
                            new ParentQuestionItemId(topicId,questionItemId),questionItemRevision.intValue()));

            return service.save(topicGroup);
        }catch (Exception ex){
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            return null;
        }
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/decombine", method = RequestMethod.DELETE, params = { "topicid", "questionitemid"})
    public TopicGroup removeQuestionItem(@RequestParam("topicid") UUID topicId, @RequestParam("questionitemid") UUID questionItemId) {
        TopicGroup topicGroup =null;
        try{
            topicGroup = service.findOne(topicId);
            topicGroup.removeQuestionItem(questionItemId);
            cqiService.delete(new ParentQuestionItemId(topicId,questionItemId));
            return service.save(topicGroup);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println( ex.getMessage());
            return topicGroup;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/pdf/{id}", method = RequestMethod.GET, produces = "application/pdf")
    public byte[] getPdf(@PathVariable("id") UUID id) {
        return service.findOne(id).makePdf().toByteArray();
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
