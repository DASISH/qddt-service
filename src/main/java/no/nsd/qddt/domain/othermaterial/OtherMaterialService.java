package no.nsd.qddt.domain.othermaterial;

import no.nsd.qddt.domain.BaseService;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface OtherMaterialService extends BaseService<OtherMaterial,UUID> {

    OtherMaterial findBy(UUID owner, String filename) throws ResourceNotFoundException;

    static ResponseEntity<Resource> getFileAsResponseEntity(OtherMaterial otherMaterial) {
        return null;
    }

    File getFile(OtherMaterial om);

    OtherMaterial saveFile(MultipartFile multipartFile, UUID uuid) throws FileUploadException;

    void deleteFile(OtherMaterial om);
}
