package no.nsd.qddt.domain.othermaterial;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@PropertySource(value={"classpath:application.properties"})
@Service("otherMaterialService")
class OtherMaterialServiceImpl implements OtherMaterialService {

    @Value("${api.fileroot}")
    private String fileRoot;
    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    OtherMaterialServiceImpl() {}

    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public OtherMaterial saveFile(MultipartFile multipartFile, UUID ownerId) throws IOException {
        LOG.info( ownerId.toString() );
        OtherMaterial om= new OtherMaterial( multipartFile ).setOriginalOwner( ownerId );
        Path filePath = Paths.get(getFolder(ownerId.toString()),om.getFileName());
        if (Files.exists(filePath)) {
            om.setFileName(getNextFileName(filePath));
            filePath = Paths.get(getFolder(ownerId.toString()),om.getFileName());
        }

        Files.copy(multipartFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return om;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public File getFile(UUID root, String fileName) {
        String filePath = Paths.get(getFolder(root.toString()), fileName).toString();
        return new File(filePath);
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

    private String getNextFileName(Path filePath) {
        final String matcher = filePath.getFileName().toString().substring(0,filePath.getFileName().toString().length()-2);
        File[] matchingFiles = filePath.getParent().toFile().listFiles( (dir, name) -> name.startsWith(matcher) );
        String fileIndex = (matchingFiles != null) ? String.valueOf(matchingFiles.length) : "";
        
        return matcher + ("00" + fileIndex).substring(fileIndex.length()); 
    }





}
