package no.nsd.qddt.domain.othermaterial.web;

import no.nsd.qddt.domain.othermaterial.OtherMaterial;
import no.nsd.qddt.domain.othermaterial.OtherMaterialService;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private OtherMaterialService otherMaterialService;

    @Autowired
    public OtherMaterialController(OtherMaterialService otherMaterialService){
        this.otherMaterialService = otherMaterialService;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public OtherMaterial get(@PathVariable("id") UUID id) {
        return otherMaterialService.findOne(id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public OtherMaterial update(@RequestBody OtherMaterial instance) {
        return otherMaterialService.save(instance);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public OtherMaterial create(@RequestBody OtherMaterial instance) {
        return otherMaterialService.save(instance);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public void delete(@PathVariable("id") UUID id) {
        otherMaterialService.delete(id);
    }

    @ResponseStatus(value = HttpStatus.I_AM_A_TEAPOT)
    @RequestMapping(value="/upload", method=RequestMethod.GET)
    public @ResponseBody String provideUploadInfo() {
        return "You can upload a file by posting to this URL.";
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value="/upload/{ownerid}", method=RequestMethod.POST , headers = "content-type=multipart/form-data" )
    public @ResponseBody OtherMaterial handleFileUpload(@PathVariable("ownerid") UUID ownerId,
                                                 @RequestParam("file") MultipartFile file) throws FileUploadException {
        if (file.isEmpty())
            throw  new FileUploadException("File is empty");

        return otherMaterialService.saveFile(file,ownerId);
    }

    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @RequestMapping(value="/files/{fileId}", method=RequestMethod.GET)
    public ResponseEntity<Resource> handleFileDownload(@PathVariable("fileId") UUID fileId) throws IOException {
        System.out.println("file download...");
        return otherMaterialService.getFileAsResponseEntity(fileId);
    }


}
