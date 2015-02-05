package no.nsd.qddt.controller;

import no.nsd.qddt.domain.Study;
import no.nsd.qddt.service.BaseServiceAudit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public abstract class AbstractAuditController<T> extends AbstractController<T> {

    protected BaseServiceAudit<T> service;

    @Autowired
    public AbstractAuditController(BaseServiceAudit<T> service) {
        super(service);
        this.service = service;
    }


    @RequestMapping(value = "/UUID/{id}", method = RequestMethod.GET)
    public T getOneByGuid(@PathVariable("id") UUID id){
        return service.findByGuid(id);
    }


    @RequestMapping(value = "/audit/{id}", method = RequestMethod.GET)
    public Revision<Integer, T> getLastRevision(@PathVariable("id") Long id) {
        return service.findLastChange(id);
    }

    @RequestMapping(value = "/audit/{id}/{revision}", method = RequestMethod.GET)
    public Revision<Integer, T> getByRevision(@PathVariable("id") Long id, @PathVariable("revision") Integer revision) {
        return service.findEntityAtRevision(id, revision);
    }

    @RequestMapping(value = "/audit/{id}/all", method = RequestMethod.GET)
    public HttpEntity<PagedResources<Revision<Integer, Study>>> getAllRevision(
            @PathVariable("id") Long id,Pageable pageable, PagedResourcesAssembler assembler){

        Page<Revision<Integer, T>> studies = service.findAllRevisionsPageable(id, pageable);
        return new ResponseEntity<>(assembler.toResource(studies), HttpStatus.OK);
    }
}
