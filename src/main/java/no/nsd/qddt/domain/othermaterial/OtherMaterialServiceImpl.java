package no.nsd.qddt.domain.othermaterial;

import no.nsd.qddt.domain.controlconstruct.pojo.ControlConstruct;
import no.nsd.qddt.domain.controlconstruct.ControlConstructService;
import no.nsd.qddt.domain.othermaterial.pojo.OtherMaterial;
import no.nsd.qddt.domain.othermaterial.pojo.OtherMaterialConstruct;
import no.nsd.qddt.domain.othermaterial.pojo.OtherMaterialTopic;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import no.nsd.qddt.domain.topicgroup.TopicGroupService;
import no.nsd.qddt.exception.ReferenceInUseException;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final OtherMaterialRepository otherMaterialRepository;
    private final ControlConstructService controlConstructService;
    private final TopicGroupService topicGroupService;

    @Autowired
    OtherMaterialServiceImpl(OtherMaterialRepository otherMaterialRepository,
                             ControlConstructService controlConstructService,
                             TopicGroupService topicGroupService){
        this.otherMaterialRepository = otherMaterialRepository;
        this.topicGroupService = topicGroupService;
        this.controlConstructService = controlConstructService;
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
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public OtherMaterial findOne(UUID uuid) {
        return otherMaterialRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(uuid, OtherMaterial.class)
        );
    }

    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public <S extends OtherMaterial> S save(S instance) {
        return otherMaterialRepository.save(instance);
    }



    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public List<OtherMaterial> save(List<OtherMaterial> instances) {
        return otherMaterialRepository.save(instances);
    }

    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public void delete(UUID uuid)  {
        try {
            delete(findOne(uuid));
        } catch (ReferenceInUseException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public void delete(List<OtherMaterial> instances)  {

        instances.forEach(om -> {
            if (om.getSource()== null)
                try {
                    delete(om);
                } catch (ReferenceInUseException e) {
                    // checking before calling no exception
                }
        });
    }

    private void delete(OtherMaterial om) throws ReferenceInUseException{
//        deleteFile(om);
        otherMaterialRepository.delete(om.getId());
    }


    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public OtherMaterial findBy(UUID owner, String filename) throws ResourceNotFoundException {

        return otherMaterialRepository.findByOwnerIdAndOriginalName(owner,filename)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format( "%s [%s]", owner, filename ),
                        OtherMaterial.class)
        );

    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public List<OtherMaterial> findBy(UUID owner) throws ResourceNotFoundException {
        return otherMaterialRepository.findByOwnerId(owner);
    }


    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public OtherMaterial saveFile(MultipartFile multipartFile, UUID ownerId, String kind) throws FileUploadException {

        String filepath = Paths.get(getFolder(ownerId.toString()), multipartFile.getOriginalFilename()).toString();

        OtherMaterial om;
        try {
            om = findBy(ownerId,multipartFile.getOriginalFilename());
            om.setSize(multipartFile.getSize());
            om.setFileType(multipartFile.getContentType());
            om.setOriginalName(multipartFile.getOriginalFilename());
            om.setFileName(multipartFile.getName());

        } catch (ResourceNotFoundException re){
            if (kind.equals( "T" )) {
                TopicGroup topic = topicGroupService.findOne( ownerId );
                om = topic.addOtherMaterial(new OtherMaterialTopic( ownerId, multipartFile ) );
            } else {
                ControlConstruct ctrl = controlConstructService.findOne( ownerId );
                om = ctrl.addOtherMaterial( new OtherMaterialConstruct( ownerId, multipartFile ) );
            }
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
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public File getFile(OtherMaterial om){
        if (om.getSource() != null)
            return getFile(om.getSource());

        String filepath = Paths.get(getFolder(om.getOwnerId().toString()), om.getOriginalName()).toString();
        return new File(filepath);
    }


    /*
        return absolute path to save folder, creates folder if not exists
     */
    private String getFolder(String ownerId) {

        File directory= new File(fileRoot + ownerId.toLowerCase());

        if(!directory.exists()) {
            directory.mkdirs();
        }
        return directory.getAbsolutePath();

    }


    protected OtherMaterial prePersistProcessing(OtherMaterial instance) {
        return instance;
    }


    protected OtherMaterial postLoadProcessing(OtherMaterial instance) {
        return instance;
    }


}