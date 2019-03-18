package no.nsd.qddt.domain.topicgroup.web;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.nsd.qddt.domain.AbstractController;
import no.nsd.qddt.domain.concept.json.ConceptJsonEdit;
import no.nsd.qddt.domain.othermaterial.OtherMaterialService;
import no.nsd.qddt.domain.study.StudyService;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import no.nsd.qddt.domain.topicgroup.TopicGroupService;
import no.nsd.qddt.domain.topicgroup.json.TopicGroupJson;
import no.nsd.qddt.domain.xml.XmlDDIFragmentAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * @author Stig Norland
 */
@RestController
@RequestMapping("/topicgroup")
public class TopicGroupController extends AbstractController {

    @Autowired
    private TopicGroupService service;
    @Autowired
    private StudyService studyService;
    @Autowired
    private OtherMaterialService omService;



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

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/createfile", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
    public TopicGroupJson createWithFile(@RequestParam("files") MultipartFile[] files,@RequestParam("topicgroup") String jsonString) throws IOException {

        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        TopicGroup instance = mapper.readValue( jsonString, TopicGroup.class );

        if (files != null && files.length > 0) {
            for (MultipartFile multipartFile : files) {
                instance.getOtherMaterials().add(omService.saveFile( multipartFile, instance.getId() ));
            }
        }
        
        return new TopicGroupJson(service.save(instance));
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create/{studyId}", method = RequestMethod.POST)
    public TopicGroupJson create(@RequestBody TopicGroup instance, @PathVariable("studyId")UUID studyId) {

        if (instance.getStudy() == null){
            studyService.findOne(studyId).addTopicGroup(instance);
        }

        return new TopicGroupJson(service.save( instance ));
//        Study study = studyService.findOne(studyId);
//        study.addTopicGroup(instance.getStudyIndex(), instance);
//        instance = studyService.save( study ).getTopicGroups().stream()
//            .max( Comparator.comparing(TopicGroup::getModified)).get();

    }
    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/copy/{uuid}/{rev}/{parentUuid}", method = RequestMethod.POST)
    public TopicGroupJson copy(@PathVariable("uuid") UUID sourceId ,
                                @PathVariable("rev") Integer sourceRev,
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
    @RequestMapping(value = "/list/by-parent/{uuid}", method = RequestMethod.GET)
    public List<TopicGroupJson> findByStudy(@PathVariable("uuid") UUID studyId) {
        try {
            return service.findByStudyId(studyId).stream()
                    .map(TopicGroupJson::new)
                    .collect(Collectors.toList());
        } catch (Exception ex){
            LOG.error("findByStudy",ex);
            return Collections.emptyList();
        }
    }


    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page/search", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public HttpEntity<PagedResources<ConceptJsonEdit>> getBy(@RequestParam(value = "name",defaultValue = "%") String name,
                                                             @RequestParam(value = "description",defaultValue = "%") String description,
                                                             Pageable pageable, PagedResourcesAssembler assembler) {
        return new ResponseEntity<>(
            assembler.toResource(
                service.findByNameAndDescriptionPageable(name,description, pageable)
                    .map(TopicGroupJson::new))
                ,HttpStatus.OK);
    }


    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/combine", method = RequestMethod.POST, params = { "parentId", "questionitemid","questionitemrevision"})
    public TopicGroupJson addQuestionItem(@RequestParam("parentId") UUID topicId, @RequestParam("questionitemid") UUID questionItemId,
                                           @RequestParam("questionitemrevision") Number questionItemRevision ) {
        try {
            TopicGroup topicGroup = service.findOne(topicId);
            if (questionItemRevision == null)
                questionItemRevision=0;
            topicGroup.addQuestionItem(questionItemId,questionItemRevision.intValue());

            return new TopicGroupJson(service.save(topicGroup));
        } catch (Exception ex){
            LOG.error("addQuestionItem",ex);
//            StackTraceFilter.filter(ex.getStackTrace()).stream()
//                    .map(a->a.toString())
//                    .forEach(LOG::info);

            return null;
        }
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/decombine", method = RequestMethod.POST, params = { "parentId", "questionitemid","questionitemrevision"})
    public TopicGroupJson removeQuestionItem(@RequestParam("parentId") UUID topicId, @RequestParam("questionitemid") UUID questionItemId,
                                          @RequestParam("questionitemrevision") Number revision) {
        TopicGroup topicGroup =null;
        try{
            topicGroup = service.findOne(topicId);
            topicGroup.removeQuestionItem(questionItemId,revision.intValue());
            return new TopicGroupJson(service.save(topicGroup));
        } catch (Exception ex) {
            LOG.error("removeQuestionItem",ex);
//            StackTraceFilter.filter(ex.getStackTrace()).stream()
//                    .map(a->a.toString())
//                    .forEach(LOG::info);
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
        return new XmlDDIFragmentAssembler<TopicGroup>(service.findOne(id)).compileToXml();
    }
}
