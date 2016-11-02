package no.nsd.qddt.domain.othermaterial;

import no.nsd.qddt.exception.ResourceNotFoundException;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
    @Transactional()
    public OtherMaterial save(OtherMaterial instance) {
        return otherMaterialRepository.save(instance);
    }

    @Override
    public List<OtherMaterial> save(List<OtherMaterial> instances) {

        return otherMaterialRepository.save(instances);

    }

    @Override
    @Transactional()
    public void delete(UUID uuid) {

        deleteFile(findOne(uuid));
        otherMaterialRepository.delete(uuid);

    }

    @Override
    @Transactional()
    public void delete(List<OtherMaterial> instances) {

        instances.forEach(om->deleteFile(om));
        otherMaterialRepository.delete(instances);

    }

    @Override
    public OtherMaterial findBy(UUID owner, String filename) throws ResourceNotFoundException {

        String name = owner + " [" + filename + "]";
        return otherMaterialRepository.findBy(owner,filename).orElseThrow(
                () -> new ResourceNotFoundException(name , OtherMaterial.class)
        );

    }

    @Override
    public File getFile(OtherMaterial om){
        String filepath = Paths.get(getFolder(om.getOwner().toString()), om.getFileName()).toString();
        return new File(filepath);
    }

    @Override
    @Transactional()
    public OtherMaterial saveFile(MultipartFile multipartFile, UUID ownerId) throws FileUploadException {

        String filepath = Paths.get(getFolder(ownerId.toString()), multipartFile.getName()).toString();
        OtherMaterial om;
        try{
            om = findBy(ownerId,multipartFile.getName());
            om.setSize(multipartFile.getSize());
            om.setFileType(multipartFile.getContentType());
            om.setOriginalName(multipartFile.getOriginalFilename());
        } catch (ResourceNotFoundException re){
            om = new OtherMaterial(ownerId,multipartFile, null);
        }

        try {
            Files.copy(multipartFile.getInputStream(), Paths.get(filepath), StandardCopyOption.REPLACE_EXISTING);
            return save(om);
        } catch (IOException e) {
            throw new FileUploadException(multipartFile.getName());
        }
    }

    @Override
    public void deleteFile(OtherMaterial om) {

        String filepath = Paths.get(getFolder(om.getOwner().toString()), om.getFileName()).toString();
        new File(filepath).delete();

    }

    /*
    return absolute path to save folder, creates folder if not exists
     */
    @Transactional()
    private String getFolder(String ownerId) {

        File directory= new File(fileRoot + ownerId.substring(1, 3));

        if(!directory.exists()) {
            directory.mkdir();
        }
        return directory.getAbsolutePath();
    }


}
