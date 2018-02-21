package no.nsd.qddt.domain.othermaterial.web;

import no.nsd.qddt.domain.othermaterial.OtherMaterial;
import no.nsd.qddt.domain.othermaterial.OtherMaterialService;
import no.nsd.qddt.exception.RequestAbortedException;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
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

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public OtherMaterial get(@PathVariable("id") UUID id) {
        return service.findOne(id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public OtherMaterial update(@RequestBody OtherMaterial instance) {
        return service.save(instance);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public OtherMaterial create(@RequestBody OtherMaterial instance) {
        return service.save(instance);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") UUID id) throws RequestAbortedException {
        service.delete(id);
    }

    @ResponseStatus(value = HttpStatus.I_AM_A_TEAPOT)
    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public
    @ResponseBody
    String provideUploadInfo() {
        return "You can upload a file by posting to this URL.";
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/upload/{ownerid}/{parentType}", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
    public
    @ResponseBody
    OtherMaterial handleFileUpload(@PathVariable("ownerid") UUID ownerId, @PathVariable("parentType") String parentType,
                                   @RequestParam("file") MultipartFile file) throws FileUploadException {
        if (file.isEmpty())
            throw new FileUploadException("File is empty");

        return service.saveFile(file, ownerId,parentType);
    }


    @RequestMapping(value="/files/{fileId}", method=RequestMethod.GET,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE )
    @ResponseBody()
    public FileSystemResource getFile(@PathVariable("fileId") UUID fileId) throws IOException {
        return new FileSystemResource(service.getFile(service.findOne(fileId)));
    }


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/xml/{id}", method = RequestMethod.GET)
    public String getXml(@PathVariable("id") UUID id) {
        return service.findOne(id).toDDIXml();
    }


}