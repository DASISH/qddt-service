package no.nsd.qddt.domain.othermaterial;

import no.nsd.qddt.domain.BaseService;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface OtherMaterialService extends BaseService<OtherMaterial,UUID> {


    /**
     *
     * @param guid
     * @param pageable
     * @return
     */
    Page<OtherMaterial> findAllByTopicGroup(UUID  guid, Pageable pageable);

    File saveFile(MultipartFile multipartFile, UUID uuid) throws FileUploadException;


}
