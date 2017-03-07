package no.nsd.qddt.domain.topicgroup.web;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.publication.Publication;
import no.nsd.qddt.domain.study.StudyService;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import no.nsd.qddt.domain.topicgroup.TopicGroupService;
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
import java.util.HashSet;
import java.util.List;
import java.util.UUID;


/**
 * @author Stig Norland
 */
@RestController
@RequestMapping("/topicgroup")
public class TopicGroupController {

    private TopicGroupService service;
    private StudyService studyService;

    @Autowired
    public TopicGroupController(TopicGroupService service, StudyService studyService) {
        this.service = service;
        this.studyService = studyService;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public TopicGroup get(@PathVariable("id") UUID id) {
        return service.findOne(id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public TopicGroup update(@RequestBody TopicGroup instance) {

//        instance.getConcepts().forEach(c->{
//            c.setChangeKind(AbstractEntityAudit.ChangeKind.UPDATED_PARENT);
//            c.setChangeComment("");
//        });
//        instance.getOtherMaterials().forEach(c->{
//            c.setChangeKind(AbstractEntityAudit.ChangeKind.UPDATED_PARENT);
//            c.setChangeComment("");
//        });
        return service.save(instance);
    }


    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create/{studyId}", method = RequestMethod.POST)
    public TopicGroup create(@RequestBody TopicGroup instance, @PathVariable("studyId")UUID studyId) {

        if(instance.getStudy() == null ){
            instance.setStudy(studyService.findOne(studyId));
        }

        return service.save(instance);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public void delete(@PathVariable("id") UUID id){
        service.delete(id);
    }


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/list/by-study/{uuid}", method = RequestMethod.GET)
    public List<TopicGroup> findByStudy(@PathVariable("uuid") UUID studyId) {
        try {
            return service.findByStudyId(studyId);
        } catch (Exception ex){
            System.out.println("findByStudy Exception");
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }


    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page/search", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public HttpEntity<PagedResources<TopicGroup>> getBy(@RequestParam(value = "name",defaultValue = "%") String name,
                                                         Pageable pageable, PagedResourcesAssembler assembler) {

        Page<TopicGroup> items = null;
        name = name.replace("*","%");

        items = service.findByNameAndDescriptionPageable(name,name, pageable);

        return new ResponseEntity<>(assembler.toResource(items), HttpStatus.OK);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/xml/{id}", method = RequestMethod.GET)
    public String getXml(@PathVariable("id") UUID id) {
        return service.findOne(id).toDDIXml();
    }
}
