package no.nsd.qddt.domain.topicgroup.web;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import no.nsd.qddt.domain.topicgroup.audit.TopicGroupAuditService;
import no.nsd.qddt.domain.topicgroup.json.TopicGroupRevisionJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@RestController
@RequestMapping(value = "/audit/topicgroup", produces = MediaType.APPLICATION_JSON_VALUE)
public class TopicGroupAuditController {

    private final TopicGroupAuditService service;

    @Autowired
    public TopicGroupAuditController(TopicGroupAuditService service) {
        this.service = service;
    }

    // @JsonView(View.Audit.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Revision<Integer, TopicGroupRevisionJson> getLastRevision(@PathVariable("id") UUID id) {
        return topicRev2Json(service.findLastChange(id));
    }

    // @JsonView(View.Audit.class)
    @RequestMapping(value = "/{id}/{revision}", method = RequestMethod.GET)
    public Revision<Integer, TopicGroupRevisionJson> getByRevision(@PathVariable("id") UUID id, @PathVariable("revision") Integer revision) {
        return topicRev2Json(service.findRevision(id, revision));
    }

    // @JsonView(View.Audit.class)
    @RequestMapping(value = "/{id}/all", method = RequestMethod.GET)
    public PagedModel<EntityModel<Revision<Integer, TopicGroupRevisionJson>>> allProjects(
            @PathVariable("id") UUID id,
            @RequestParam(value = "ignorechangekinds",
                    defaultValue = "IN_DEVELOPMENT,UPDATED_HIERARCHY_RELATION,UPDATED_PARENT,UPDATED_CHILD")
                    Collection<AbstractEntityAudit.ChangeKind> changekinds,
            Pageable pageable, PagedResourcesAssembler<Revision<Integer, TopicGroupRevisionJson>> assembler) {

            Page<Revision<Integer, TopicGroupRevisionJson>> revisions =
                    service.findRevisionByIdAndChangeKindNotIn(id,changekinds, pageable).map(this::topicRev2Json);

        return assembler.toModel(revisions);
    }

    // @JsonView(View.Audit.class)
    @RequestMapping(value = "/{id}/allinclatest", method = RequestMethod.GET)
    public PagedModel<EntityModel<Revision<Integer, TopicGroupRevisionJson>>> allIncludinglatest(
            @PathVariable("id") UUID id,
            @RequestParam(value = "ignorechangekinds",
                    defaultValue = "IN_DEVELOPMENT,UPDATED_HIERARCHY_RELATION,UPDATED_PARENT,UPDATED_CHILD,BASED_ON")
                    Collection<AbstractEntityAudit.ChangeKind> changekinds,
            Pageable pageable, PagedResourcesAssembler<Revision<Integer, TopicGroupRevisionJson>> assembler) {

        Page<Revision<Integer, TopicGroupRevisionJson>> revisions =
                service.findRevisionsByChangeKindIncludeLatest(id,changekinds, pageable).map(this::topicRev2Json);

        return assembler.toModel(revisions);
    }

    @ResponseBody
    @RequestMapping(value = "/pdf/{id}/{revision}",  method = RequestMethod.GET, produces = "application/pdf")
    public byte[] getPdf(@PathVariable("id") UUID id, @PathVariable("revision") Integer revision) {
        return service.findRevision(id, revision).getEntity().makePdf().toByteArray();
    }

    private Revision<Integer,TopicGroupRevisionJson> topicRev2Json(Revision<Integer, TopicGroup> revision){
        return Revision.of(
                revision.getMetadata(),
                new TopicGroupRevisionJson(revision.getEntity()));
    }
}
