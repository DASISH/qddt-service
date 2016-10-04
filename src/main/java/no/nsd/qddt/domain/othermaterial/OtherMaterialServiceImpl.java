package no.nsd.qddt.domain.othermaterial;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@PropertySource(value={"classpath:application.properties"})
@Service("otherMaterialService")
class OtherMaterialServiceImpl implements OtherMaterialService {

    @Value("${fileroot}")
    private String fileRoot;

    private OtherMaterialRepository otherMaterialRepository;

    @Autowired
    OtherMaterialServiceImpl(OtherMaterialRepository otherMaterialRepository){
        this.otherMaterialRepository = otherMaterialRepository;
    }

    @Override
    public long count() {
        return otherMaterialRepository.count();
    }

    @Override
    public boolean exists(UUID uuid) {
        return otherMaterialRepository.exists(uuid);
    }

    @Override
    public OtherMaterial findOne(UUID uuid) {
        return otherMaterialRepository.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException(uuid, OtherMaterial.class)
        );
    }

    @Override
    @Transactional(readOnly = false)
    public OtherMaterial save(OtherMaterial instance) {
        return otherMaterialRepository.save(instance);
    }

    @Override
    public List<OtherMaterial> save(List<OtherMaterial> instances) {
        return otherMaterialRepository.save(instances);
    }

    @Override
    public void delete(UUID uuid) {
        otherMaterialRepository.delete(uuid);
    }

    @Override
    public void delete(List<OtherMaterial> instances) {
        otherMaterialRepository.delete(instances);
    }

    @Override
    public File saveFile(MultipartFile multipartFile, UUID uuid) throws FileUploadException {

        String directory = createFolder(uuid.toString());
        String filepath = Paths.get(directory, multipartFile.getName()).toString();

        OtherMaterial om = findOne(uuid);
        om.setSize(multipartFile.getSize());
        om.setFileType(multipartFile.getContentType());
        om.setPath(filepath);
        om.setChangeKind(AbstractEntityAudit.ChangeKind.CREATED);
        om.setOriginalName(multipartFile.getOriginalFilename());
        om.setName(multipartFile.getName());
        save(om);

        try {
            Files.copy(multipartFile.getInputStream(), Paths.get(filepath), StandardCopyOption.REPLACE_EXISTING);
            return new File(filepath);
        } catch (IOException e) {
            throw new FileUploadException(multipartFile.getName());
        }
    }

    @Override
    public void deleteFile(UUID id) {

    }

    private String createFolder(String uuid) {

        File directory= new File(fileRoot + uuid.substring(1, 3));

        if(!directory.exists()) {
            directory.mkdir();
        }
        return directory.getAbsolutePath();
    }
}
