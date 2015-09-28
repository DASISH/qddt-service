package no.nsd.qddt.controller;

import no.nsd.qddt.domain.OtherMaterial;
import no.nsd.qddt.service.OtherMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


/**
 * @author Stig Norland
 */
@RestController
@RequestMapping("/othermaterial")
public class OtherMaterialController extends AbstractController<OtherMaterial,UUID>   {

//    private AttachmentService attachmentService;

    @Autowired
    public OtherMaterialController(OtherMaterialService otherMaterialService){
        super(otherMaterialService);
//        this.attachmentService = attachmentService;
    }

}
