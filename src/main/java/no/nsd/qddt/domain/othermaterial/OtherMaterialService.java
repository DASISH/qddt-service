package no.nsd.qddt.domain.othermaterial;

import no.nsd.qddt.domain.BaseService;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface OtherMaterialService extends BaseService<OtherMaterial,UUID> {

    OtherMaterial findBy(UUID owner, String filename) throws ResourceNotFoundException;

    List<OtherMaterial> findBy(UUID owner) throws ResourceNotFoundException;

//    void deleteFile(OtherMaterial om) throws ReferenceInUseException;

    OtherMaterial saveFile(MultipartFile multipartFile, UUID uuid, String kind) throws FileUploadException;

    File getFile(OtherMaterial om);

}