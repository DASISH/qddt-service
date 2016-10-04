package no.nsd.qddt.domain.topicgroup.web;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import no.nsd.qddt.domain.topicgroup.audit.TopicGroupAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@RestController
@RequestMapping(value = "/audit/topicgroup", produces = MediaType.APPLICATION_JSON_VALUE)
public class TopicGroupAuditController {

    private TopicGroupAuditService service;

    @Autowired
    public TopicGroupAuditController(TopicGroupAuditService service) {
        this.service = service;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Revision<Integer, TopicGroup> getLastRevision(@PathVariable("id") UUID id) {
        return service.findLastChange(id);
    }

    @RequestMapping(value = "/{id}/{revision}", method = RequestMethod.GET)
    public Revision<Integer, TopicGroup> getByRevision(@PathVariable("id") UUID id, @PathVariable("revision") Integer revision) {
        return service.findRevision(id, revision);
    }

    @RequestMapping(value = "/{id}/all", method = RequestMethod.GET)
    public HttpEntity<PagedResources<Revision<Integer, TopicGroup>>> allProjects(
            @PathVariable("id") UUID id,Pageable pageable, PagedResourcesAssembler assembler){

        Collection<AbstractEntityAudit.ChangeKind> changeKinds = new ArrayList<>();
        changeKinds.add(AbstractEntityAudit.ChangeKind.IN_DEVELOPMENT);

        Page<Revision<Integer, TopicGroup>> revisions = service.findRevisions(id, pageable);

        return new ResponseEntity<>(assembler.toResource(revisions), HttpStatus.OK);
    }

}