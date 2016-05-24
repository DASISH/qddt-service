package no.nsd.qddt.domain.topicgroup.web;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.study.StudyService;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import no.nsd.qddt.domain.topicgroup.TopicGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


/**
 * @author Stig Norland
 */
@RestController
@RequestMapping("/topicgroup")
public class TopicGroupController {

    private TopicGroupService topicGroupService;
    private StudyService studyService;

    @Autowired
    public TopicGroupController(TopicGroupService topicGroupService, StudyService studyService) {
        this.topicGroupService = topicGroupService;
        this.studyService = studyService;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public TopicGroup get(@PathVariable("id") UUID id) {
        return topicGroupService.findOne(id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public TopicGroup update(@RequestBody TopicGroup instance) {

        instance.getConcepts().forEach(c->c.setChangeKind(AbstractEntityAudit.ChangeKind.UPDATED_PARENT));
        instance.getOtherMaterials().forEach(c->c.setChangeKind(AbstractEntityAudit.ChangeKind.UPDATED_PARENT));
        return topicGroupService.save(instance);
    }


    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create/{studyId}", method = RequestMethod.POST)
    public TopicGroup create(@RequestBody TopicGroup instance, @PathVariable("studyId")UUID studyId) {
//
//        if(instance.getStudy() == null ){
//            instance.setStudy(studyService.findOne(studyId));
//        }

        if(instance.getConcepts().isEmpty()){
            instance.addConcept(new Concept());
        }
        return topicGroupService.save(instance);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public void delete(@PathVariable("id") UUID id){
        topicGroupService.delete(id);
    }


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/list/by-study/{uuid}", method = RequestMethod.GET)
    public List<TopicGroup> findByStudy(@PathVariable("uuid") UUID studyId) {
        return topicGroupService.findByStudyId(studyId);
    }

}
