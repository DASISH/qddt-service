package no.nsd.qddt.domain.surveyprogram.web;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.surveyprogram.SurveyProgram;
import no.nsd.qddt.domain.surveyprogram.audit.SurveyProgramAuditService;
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
 */
@RestController
@RequestMapping(value = "/audit/surveyprogram")
public class SurveyProgramAuditController {

    private final SurveyProgramAuditService service;

    @Autowired
    public SurveyProgramAuditController(SurveyProgramAuditService service) {
        this.service = service;
    }

    // @JsonView(View.Audit.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Revision<Integer, SurveyProgram> findLastRevisionById(@PathVariable("id") UUID id) {
        return service.findLastChange(id);
    }

    // @JsonView(View.Audit.class)
    @RequestMapping(value = "/{id}/{revision}", method = RequestMethod.GET)
    public Revision<Integer, SurveyProgram> findByRevision(@PathVariable("id") UUID id, @PathVariable("revision") Integer revision) {
        return service.findRevision(id, revision);
    }

    // @JsonView(View.Audit.class)
    @RequestMapping(value = "/{id}/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public PagedModel<EntityModel<Revision<Integer, SurveyProgram>>> allProjects(
            @PathVariable("id") UUID id,
            @RequestParam(value = "ignorechangekinds",defaultValue = "IN_DEVELOPMENT,UPDATED_HIERARCHY_RELATION,UPDATED_PARENT,UPDATED_CHILD")
                    Collection<AbstractEntityAudit.ChangeKind> changekinds,
            Pageable pageable, PagedResourcesAssembler<Revision<Integer, SurveyProgram>> assembler) {

        Page<Revision<Integer, SurveyProgram>> revisions = service.findRevisionByIdAndChangeKindNotIn(id,changekinds, pageable);
        return assembler.toModel(revisions);
    }

    @ResponseBody
    @RequestMapping(value = "/pdf/{id}/{revision}",  method = RequestMethod.GET, produces = "application/pdf")
    public byte[] getPdf(@PathVariable("id") UUID id, @PathVariable("revision") Integer revision) {
        return service.findRevision(id, revision).getEntity().makePdf().toByteArray();
    }

//    @RequestMapping(value = "/{id}/filteredlist", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
//    public HttpEntity<PagedModel<Revision<Integer, SurveyProgram>>> list(
//            @PathVariable("id") UUID id, @PathVariable("changeKinds") String[]changes, Pageable pageable, PagedResourcesAssembler assembler){
//
//        Collection<AbstractEntityAudit.ChangeKind> changeKinds = new ArrayList<>();
//        for (String change:changes) {
//            changeKinds.add(AbstractEntityAudit.ChangeKind.valueOf(change));
//        }
//
//        Page<Revision<Integer, SurveyProgram>> revisions = service.findRevisionByIdAndChangeKindNotIn(id,changeKinds , pageable);
//        return assembler.toModel(revisions), HttpStatus.OK);
//    }

}
