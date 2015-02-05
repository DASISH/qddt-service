package no.nsd.qddt.controller;

import no.nsd.qddt.domain.Attachment;
import no.nsd.qddt.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;


/**
 * @author Stig Norland
 */
@RestController
@RequestMapping("/attachment")
public class AttachmentController  extends AbstractController<Attachment> {

    private AttachmentService attachmentService;

    @Autowired
    public AttachmentController(AttachmentService attachmentService){
        this.attachmentService = attachmentService;
    }


    @Override
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Attachment> getAll() {return attachmentService.findAll();}


    @Override
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public HttpEntity<PagedResources<Attachment>> getAll(Pageable pageable, PagedResourcesAssembler assembler) {
        Page<Attachment> instances = attachmentService.findAll(pageable);
        return new ResponseEntity<>(assembler.toResource(instances), HttpStatus.OK);
    }


    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Attachment getOne(@PathVariable("id") Long id) {
        return attachmentService.findById(id);
    }


    @RequestMapping(value = "/UUID/{id}", method = RequestMethod.GET)
    public Attachment getOne(@PathVariable("id") UUID id) {
        return attachmentService.findById(id);
    }


    @Override
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Attachment create(Attachment instance) {
        return attachmentService.save(instance);}

    @Override
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public void delete(Attachment instance) {
        attachmentService.delete(instance);
    }


    @RequestMapping(value = "/{id}/page", method = RequestMethod.GET)
    public HttpEntity<PagedResources<Attachment>> getThreadbyId( @PathVariable("id") Long moduleId, Pageable pageable, PagedResourcesAssembler assembler) {

        Page<Attachment> instances = attachmentService.findAllByModule(moduleId, pageable);
        return new ResponseEntity<>(assembler.toResource(instances), HttpStatus.OK);
    }


    @RequestMapping(value = "/UUID/{id}/page", method = RequestMethod.GET)
    public HttpEntity<PagedResources<Attachment>> getThreadbyGuid( @PathVariable("id") UUID id, Pageable pageable, PagedResourcesAssembler assembler) {

        Page<Attachment> instances = attachmentService.findAllByGuid(id, pageable);
        return new ResponseEntity<>(assembler.toResource(instances), HttpStatus.OK);
    }
}
