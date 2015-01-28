package no.nsd.qddt.controller;

import no.nsd.qddt.domain.Attachment;
import no.nsd.qddt.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AttachmentController {

    private AttachmentService attachmentService;

    @Autowired
    public AttachmentController(AttachmentService attachmentService){
        this.attachmentService = attachmentService;
    }


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Attachment> getAll() {return attachmentService.findAll();}

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Attachment getOne(@PathVariable("id") UUID id) {return attachmentService.findById(id);}

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Attachment create(Attachment attachment) {return attachmentService.save(attachment);}

}
