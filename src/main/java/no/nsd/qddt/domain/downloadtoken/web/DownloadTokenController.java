package no.nsd.qddt.domain.downloadtoken.web;

import no.nsd.qddt.domain.downloadtoken.DownloadToken;
import no.nsd.qddt.domain.downloadtoken.DownloadTokenService;
import no.nsd.qddt.domain.othermaterial.OtherMaterial;
import no.nsd.qddt.domain.othermaterial.OtherMaterialService;
import org.springframework.web.bind.annotation.RestController;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/download/")
public class DownloadTokenController {

    private OtherMaterialService otherMaterialService;

    private DownloadTokenService downloadTokenService;

    @Autowired
    public DownloadTokenController(OtherMaterialService otherMaterialService,
                                   DownloadTokenService downloadTokenService) {
        this.otherMaterialService = otherMaterialService;
        this.downloadTokenService = downloadTokenService;
    }

    @RequestMapping(value = "{downloadTokenUUID}", method = RequestMethod.GET)
    public ResponseEntity<Resource> getFile(@PathVariable("downloadTokenUUID") UUID downloadTokenUUID) throws IOException {
        DownloadToken downloadToken = downloadTokenService.findByUUID(downloadTokenUUID);
        downloadTokenService.verifyValidity(downloadToken);
        return OtherMaterialService.getFileAsResponseEntity(downloadToken.getotherMaterial());
    }

    @RequestMapping(value = "request-download-token",method = RequestMethod.POST)
    public DownloadToken getOtherMaterialDownloadToken(@RequestBody OtherMaterial otherMaterial) {
        return null;
//        TODO
//        downloadTokenService.getDownloadToken(.findOne(OtherMaterial.getId()),
//                SecurityContext.getUser());
    }
}
