package no.nsd.qddt.controller;

import no.nsd.qddt.domain.Attachment;
import no.nsd.qddt.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Stig Norland
 */
@RestController
@RequestMapping("/attachment")
public class AttachmentController  extends AbstractController<Attachment>   {

//    private AttachmentService attachmentService;

    @Autowired
    public AttachmentController(AttachmentService attachmentService){
        super(attachmentService);
//        this.attachmentService = attachmentService;
    }




//    @RequestMapping(value = "/{id}/page", method = RequestMethod.GET)
//    public HttpEntity<PagedResources<Attachment>> getThreadbyId( @PathVariable("id") Long moduleId, Pageable pageable, PagedResourcesAssembler assembler) {
//
//        Page<Attachment> instances = attachmentService.findAllByModule(moduleId, pageable);
//        return new ResponseEntity<>(assembler.toResource(instances), HttpStatus.OK);
//    }
//
//
//    @RequestMapping(value = "/UUID/{id}/page", method = RequestMethod.GET)
//    public HttpEntity<PagedResources<Attachment>> getThreadbyGuid( @PathVariable("id") UUID id, Pageable pageable, PagedResourcesAssembler assembler) {
//
//        Page<Attachment> instances = attachmentService.findAllByGuid(id, pageable);
//        return new ResponseEntity<>(assembler.toResource(instances), HttpStatus.OK);
//    }
}
