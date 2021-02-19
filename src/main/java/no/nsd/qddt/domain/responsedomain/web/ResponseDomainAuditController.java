package no.nsd.qddt.domain.responsedomain.web;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import no.nsd.qddt.domain.responsedomain.audit.ResponseDomainAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.history.Revision;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import static no.nsd.qddt.domain.AbstractEntityAudit.ChangeKind.*;

/**
 * @author Stig Norland
 */
@RestController
@RequestMapping(value = "/audit/responsedomain", produces = MediaType.APPLICATION_JSON_VALUE)
public class ResponseDomainAuditController {

    private final ResponseDomainAuditService auditService;

    @Autowired
    public ResponseDomainAuditController(ResponseDomainAuditService service) {
        this.auditService = service;
    }


    // @JsonView(View.Audit.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Revision<Integer, ResponseDomain> getLastRevision(@PathVariable("id") UUID id) {
        return auditService.findLastChange(id);
    }

    // @JsonView(View.Audit.class)
    @RequestMapping(value = "/{id}/{revision}", method = RequestMethod.GET)
    public Revision<Integer, ResponseDomain> getByRevision(@PathVariable("id") UUID id, @PathVariable("revision") Integer revision) {
        return auditService.findRevision(id, revision);
    }

    // @JsonView(View.Audit.class)
    @RequestMapping(value = "/{id}/all", method = RequestMethod.GET)
    public PagedModel<EntityModel<Revision<Integer, ResponseDomain>>> allProjects(
            @PathVariable("id") UUID id,
            @RequestParam(value = "ignorechangekinds",defaultValue = "IN_DEVELOPMENT,UPDATED_HIERARCHY_RELATION,UPDATED_PARENT")
                    Collection<AbstractEntityAudit.ChangeKind> changekinds,
            Pageable pageable, PagedResourcesAssembler<Revision<Integer, ResponseDomain>> assembler) {

        Page<Revision<Integer, ResponseDomain>> revisions = auditService.findRevisionByIdAndChangeKindNotIn(id,changekinds, pageable);
        return assembler.toModel(revisions);
    }

    // @JsonView(View.Audit.class)
    @RequestMapping(value = "/{id}/latestversion", method = RequestMethod.GET)
    public Revision<Integer, ResponseDomain> getLatestVersion(@PathVariable("id") UUID id ){
        Collection<AbstractEntityAudit.ChangeKind> changekinds =
            Arrays.asList(IN_DEVELOPMENT,UPDATED_HIERARCHY_RELATION, UPDATED_PARENT,UPDATED_CHILD);

        Pageable pageable = PageRequest.of(0,
            1,
            Sort.by( new Sort.Order( Sort.Direction.ASC, "updated"))  );
        Page<Revision<Integer, ResponseDomain>> revisions = auditService.findRevisionByIdAndChangeKindNotIn(id,changekinds, pageable);
        return revisions.getContent().get( 0 );
    }

}
