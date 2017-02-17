package no.nsd.qddt.domain.othermaterial.web;

import net.logstash.logback.encoder.org.apache.commons.io.IOUtils;
import no.nsd.qddt.domain.othermaterial.OtherMaterial;
import no.nsd.qddt.domain.othermaterial.OtherMaterialService;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */
@RestController
@RequestMapping("/othermaterial")
public class OtherMaterialController {

    private OtherMaterialService service;

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
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public void delete(@PathVariable("id") UUID id) {
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
    @RequestMapping(value = "/upload/{ownerid}", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
    public
    @ResponseBody
    OtherMaterial handleFileUpload(@PathVariable("ownerid") UUID ownerId,
                                   @RequestParam("file") MultipartFile file) throws FileUploadException {
        if (file.isEmpty())
            throw new FileUploadException("File is empty");

        return service.saveFile(file, ownerId);
    }




    @RequestMapping(value="/files/{fileId}", method=RequestMethod.GET,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE )
    @ResponseBody()
    public FileSystemResource getFile(@PathVariable("fileId") UUID fileId) throws IOException {
        return new FileSystemResource(service.getFile(service.findOne(fileId)));
    }


//    @RequestMapping(value = "/files/{fileId}", method = RequestMethod.GET)
//    public HttpEntity<byte[]> getFile(@PathVariable("fileId") UUID fileId) throws IOException {
//
//        File file = service.getFile(service.findOne(fileId));
//        FileInputStream fis = new FileInputStream(file.getAbsoluteFile());
//        HttpHeaders header = new HttpHeaders();
//        header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//        header.set(HttpHeaders.CONTENT_DISPOSITION,
//                "attachment; filename=" + file.getName().replace(" ", "_"));
//        header.setContentLength(file.length());
//
//        return new HttpEntity<>(IOUtils.toByteArray(fis), header);
//    }

//    @ResponseStatus(value = HttpStatus.ACCEPTED)
//    @RequestMapping(value="/files/{fileId}", method=RequestMethod.GET,produces = MediaType.APPLICATION_OCTET_STREAM_VALUE )
//    public ResponseEntity<Resource> handleFileDownload(@PathVariable("fileId") UUID fileId) throws IOException {
//        ResponseEntity<Resource> resource = service.getFileAsResponseEntity(fileId);
//        resource.getHeaders().forEach((a,b)-> System.out.println("Headers[" + a + "] - " + b));
//        return resource;
//    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/xml/{id}", method = RequestMethod.GET)
    public String getXml(@PathVariable("id") UUID id) {
        return service.findOne(id).toDDIXml();
    }

//    @ResponseStatus(value = HttpStatus.ACCEPTED)
//    @RequestMapping(value = "/files/{fileId}", method = RequestMethod.GET,produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
//    public void handleFileDownload(HttpServletResponse response, @PathVariable("fileId") UUID fileId) throws IOException {
//        System.out.println("file download...");
//        OtherMaterial om = service.findOne(fileId);
//        File file = service.getFile(om);
//
//        response.setHeader("Content-disposition", "attachment;filename=\"" + om.getOriginalName() + "\"");
//        response.setContentLength((int) om.getSize());
//        response.setContentType("application/octet-stream");
//        FileInputStream fis = new FileInputStream(file.getAbsoluteFile());
//        IOUtils.copy(fis, response.getOutputStream());
//        System.out.println("buffer size " + response.getBufferSize());
//        response.getHeaderNames().forEach(name-> System.out.println("Header["+ name + "] - " +response.getHeader(name)));
//        response.flushBuffer();
//
//    }
}