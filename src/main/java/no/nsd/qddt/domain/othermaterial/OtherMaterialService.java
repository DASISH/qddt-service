package no.nsd.qddt.domain.othermaterial;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface OtherMaterialService  {


    OtherMaterial saveFile(MultipartFile multipartFile, UUID uuid) throws IOException;

    File getFile(UUID root, String fileName);


}