package no.nsd.qddt.domain.othermaterial.web;

import no.nsd.qddt.domain.othermaterial.OtherMaterial;
import no.nsd.qddt.domain.othermaterial.OtherMaterialService;
import no.nsd.qddt.exception.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */
@RestController
@RequestMapping("/othermaterial")
public class OtherMaterialController {

    private final OtherMaterialService service;

    @Autowired
    public OtherMaterialController(OtherMaterialService service) {
        this.service = service;
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/upload/{ownerid}", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
    public @ResponseBody OtherMaterial handleFileUpload(
        @PathVariable("ownerid") UUID ownerId,
        @RequestParam("file") MultipartFile file) throws IOException, FileUploadException {

        if (file.isEmpty()) throw new FileUploadException("File is empty");

        return service.saveFile(file, ownerId);
    }


    @RequestMapping(value="/files/{root}/{filename}", method=RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE )
    @ResponseBody() public FileSystemResource getFile(@PathVariable("root") UUID root, @PathVariable("filename") String fileName ) {
        return new FileSystemResource(service.getFile(root, fileName));
    }



}