package no.nsd.qddt.domain.topicgroup.web;

import no.nsd.qddt.domain.BaseController;
import no.nsd.qddt.domain.conceptquestionitem.ParentQuestionItemId;
import no.nsd.qddt.domain.study.StudyService;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import no.nsd.qddt.domain.topicgroup.TopicGroupService;
import no.nsd.qddt.domain.topicgroup.json.TopicGroupJson;
import no.nsd.qddt.domain.topicgroupquestionitem.TopicGroupQuestionItem;
import no.nsd.qddt.domain.topicgroupquestionitem.TopicGroupQuestionItemService;
import no.nsd.qddt.exception.StackTraceFilter;
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
public class TopicGroupController extends BaseController {

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
    public TopicGroupJson get(@PathVariable("id") UUID id) {
        return new TopicGroupJson(service.findOne(id));
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public TopicGroupJson update(@RequestBody TopicGroup instance) {
        return new TopicGroupJson(service.save(instance));
    }


    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create/{studyId}", method = RequestMethod.POST)
    public TopicGroupJson create(@RequestBody TopicGroup instance, @PathVariable("studyId")UUID studyId) {

        if(instance.getStudy() == null ){
            studyService.findOne(studyId).addTopicGroup(instance);
        }
        try {
            instance = service.save(instance);
        }catch (Exception ex){
            StackTraceFilter.println(ex.getStackTrace());
        }
        return new TopicGroupJson(instance);
    }
    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/copy/{uuid}/{rev}/{parentUuid}", method = RequestMethod.POST)
    public TopicGroupJson copy(@PathVariable("uuid") UUID sourceId ,
                                @PathVariable("rev") Long sourceRev,
                                @PathVariable("parentUuid") UUID parentId) {
        return new TopicGroupJson(
                service.save(
                        service.copy(sourceId,sourceRev,parentId)));
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") UUID id){
        service.delete(id);
    }


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/list/by-study/{uuid}", method = RequestMethod.GET)
    public List<TopicGroupJson> findByStudy(@PathVariable("uuid") UUID studyId) {
        try {
            return service.findByStudyId(studyId).stream()
                    .map(TopicGroupJson::new)
                    .collect(Collectors.toList());
        } catch (Exception ex){
            LOG.error("findByStudy",ex);
            StackTraceFilter.filter(ex.getStackTrace()).stream()
                    .map(a->a.toString())
                    .forEach(LOG::info);
            return Collections.emptyList();
        }
    }


    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page/search", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public HttpEntity<PagedResources<TopicGroupJson>> getBy(@RequestParam(value = "name",defaultValue = "%") String name,
                                                         Pageable pageable, PagedResourcesAssembler assembler) {
        name = name.replace("*","%");
        Page<TopicGroupJson> items =
                service.findByNameAndDescriptionPageable(name,name, pageable)
                        .map(TopicGroupJson::new);

        return new ResponseEntity<>(assembler.toResource(items), HttpStatus.OK);
    }


    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/combine", method = RequestMethod.POST, params = { "topicid", "questionitemid","questionitemrevision"})
    public TopicGroupJson addQuestionItem(@RequestParam("topicid") UUID topicId, @RequestParam("questionitemid") UUID questionItemId,
                                           @RequestParam("questionitemrevision") Number questionItemRevision ) {
        try {
            TopicGroup topicGroup = service.findOne(topicId);
            if (questionItemRevision == null)
                questionItemRevision=0;
            topicGroup.addTopicQuestionItem(
                    new TopicGroupQuestionItem(
                            new ParentQuestionItemId(topicId,questionItemId),questionItemRevision.longValue()));

            return new TopicGroupJson(service.save(topicGroup));
        } catch (Exception ex){
            LOG.error("addQuestionItem",ex);
            StackTraceFilter.filter(ex.getStackTrace()).stream()
                    .map(a->a.toString())
                    .forEach(LOG::info);

            return null;
        }
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/decombine", method = RequestMethod.POST, params = { "topicid", "questionitemid"})
    public TopicGroupJson removeQuestionItem(@RequestParam("topicid") UUID topicId, @RequestParam("questionitemid") UUID questionItemId) {
        TopicGroup topicGroup =null;
        try{
            topicGroup = service.findOne(topicId);
            topicGroup.removeQuestionItem(questionItemId);
            cqiService.delete(new ParentQuestionItemId(topicId,questionItemId));
            return new TopicGroupJson(service.save(topicGroup));
        } catch (Exception ex) {
            LOG.error("removeQuestionItem",ex);
            StackTraceFilter.filter(ex.getStackTrace()).stream()
                    .map(a->a.toString())
                    .forEach(LOG::info);
            return new TopicGroupJson(topicGroup);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/pdf/{id}", method = RequestMethod.GET, produces = "application/pdf")
    public byte[] getPdf(@PathVariable("id") UUID id) {
        return service.findOne(id).makePdf().toByteArray();
    }



    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/xml/{id}", method = RequestMethod.GET)
    public String getXml(@PathVariable("id") UUID id) {
        return service.findOne(id).toDDIXml();
    }
}
