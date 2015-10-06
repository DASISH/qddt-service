//package no.nsd.qddt.domain;
//
//import no.nsd.qddt.domain.study.Study;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.history.Revision;
//import org.springframework.data.web.PagedResourcesAssembler;
//import org.springframework.hateoas.PagedResources;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//
///**
// * @author Stig Norland
// */
//public abstract class AbstractAuditController<T,ID> extends AbstractController<T,ID> {
//
//    protected BaseServiceAudit<T,ID> service;
//
//    @Autowired
//    public AbstractAuditController(BaseServiceAudit<T,ID> service) {
//        super(service);
//        this.service = service;
//    }
//
//
//
//    @RequestMapping(value = "/audit/{id}", method = RequestMethod.GET)
//    public Revision<Integer, T> getLastRevision(@PathVariable("id") ID id) {
//        return service.findLastChange(id);
//    }
//
//    @RequestMapping(value = "/audit/{id}/{revision}", method = RequestMethod.GET)
//    public Revision<Integer, T> getByRevision(@PathVariable("id") ID id, @PathVariable("revision") Integer revision) {
//        return service.findEntityAtRevision(id, revision);
//    }
//
//    @RequestMapping(value = "/audit/{id}/all", method = RequestMethod.GET)
//    public HttpEntity<PagedResources<Revision<Integer, Study>>> getAllRevision(
//            @PathVariable("id") ID id,Pageable pageable, PagedResourcesAssembler assembler){
//
//        Page<Revision<Integer, T>> studies = service.findAllRevisionsPageable(id, pageable);
//        return new ResponseEntity<>(assembler.toResource(studies), HttpStatus.OK);
//    }
//}
