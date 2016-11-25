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
import java.util.ArrayList;
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
        return otherMaterialRepository.findByOwnerAndOriginalName(owner,filename).orElseThrow(
                () -> new ResourceNotFoundException(name , OtherMaterial.class)
        );

    }

    @Override
    public List<OtherMaterial> findBy(UUID owner) throws ResourceNotFoundException {
        return (List<OtherMaterial>) otherMaterialRepository.findByOwner(owner).orElseThrow(
                () -> new ResourceNotFoundException(owner , ArrayList.class));
    }

    @Override
    public File getFile(OtherMaterial om){
        String filepath = Paths.get(getFolder(om.getOwner().toString()), om.getFileName()).toString();
        return new File(filepath);
    }

    @Override
    @Transactional()
    public OtherMaterial saveFile(MultipartFile multipartFile, UUID ownerId) throws FileUploadException {

        String filepath = Paths.get(getFolder(ownerId.toString()), multipartFile.getOriginalFilename()).toString();

        OtherMaterial om;
        try{
            om = findBy(ownerId,multipartFile.getOriginalFilename());
            om.setSize(multipartFile.getSize());
            om.setFileType(multipartFile.getContentType());
            om.setOriginalName(multipartFile.getOriginalFilename());
            om.setFileName(multipartFile.getName());
//            om.setDescription();
        } catch (Exception re){
            om = new OtherMaterial(ownerId,multipartFile, null);
        }


        try {
            Files.copy(multipartFile.getInputStream(), Paths.get(filepath), StandardCopyOption.REPLACE_EXISTING);
            return save(om);
        } catch (IOException e) {
            String message = String.format("Failed to save file [%s]. \n\r%s"
                    ,multipartFile.getOriginalFilename(),
                    e.getMessage());
            throw new FileUploadException(message);
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

        File directory= new File(fileRoot + ownerId.toLowerCase());

        if(!directory.exists()) {
            directory.mkdirs();
        }
        return directory.getAbsolutePath();

    }


}
