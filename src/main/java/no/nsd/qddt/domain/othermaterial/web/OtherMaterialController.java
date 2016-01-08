package no.nsd.qddt.domain.othermaterial.web;

import no.nsd.qddt.domain.othermaterial.OtherMaterial;
import no.nsd.qddt.domain.othermaterial.OtherMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    @RequestMapping(value="/upload", method=RequestMethod.GET)
    public @ResponseBody String provideUploadInfo() {
        return "You can upload a file by posting to this URL.";
    }

    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public @ResponseBody String handleFileUpload(@RequestParam("id") UUID id,
                                                 @RequestParam("file") MultipartFile file){
        if (!file.isEmpty()) {
            try {
                otherMaterialService.saveFile(file,id);
                return "You successfully uploaded " + file.getName() + "!";
            } catch (Exception e) {
                return "You failed to upload file => " + e.getMessage();
            }
        } else {
            return "You failed to upload, because the file was empty.";
        }
    }

    @RequestMapping(value="/files/{id}", method=RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<InputStreamResource> handleFileDownload(@PathVariable("id") UUID id) {
        try {
            OtherMaterial om = otherMaterialService.findOne(id);
            Path path = Paths.get(om.getPath());
            File file= path.toFile();
            return ResponseEntity
                    .ok()
//                    .headers(headers)
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType(om.getFileType()))
                    .body(new InputStreamResource(new FileInputStream(file)));
        } catch (IOException e) {
            e.printStackTrace();
            return  null;
        }
    }



}
