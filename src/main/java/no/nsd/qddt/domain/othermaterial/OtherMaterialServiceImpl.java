package no.nsd.qddt.domain.othermaterial;

import no.nsd.qddt.domain.othermaterial.OtherMaterial;
import no.nsd.qddt.exception.ReferenceInUseException;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
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
import java.io.FilenameFilter;
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

    @Value("${fileroot}")
    private String fileRoot;
    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

//    private final ControlConstructService controlConstructService;
//    private final TopicGroupService topicGroupService;

    @Autowired
    OtherMaterialServiceImpl() {}

    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public OtherMaterial saveFile(MultipartFile multipartFile, UUID ownerId) throws IOException {

        OtherMaterial om= new OtherMaterial( multipartFile );

        Path filepath = Paths.get(getFolder(ownerId.toString()),om.getFileName());
        if (Files.exists(filepath)) {
            final String matcher = filepath.getFileName().toString().substring(0,filepath.getFileName().toString().length()-2);

            File[] matchingFiles = filepath.getParent().toFile().listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) { return name.startsWith(matcher); 
                }});

            String fileIndex = String.valueOf(matchingFiles.length);
            fileIndex = ("00" + fileIndex).substring(fileIndex.length());
            om.setFileName(matcher + fileIndex);
            filepath = Paths.get(getFolder(ownerId.toString()),om.getFileName());
        }
        Files.copy(multipartFile.getInputStream(), filepath, StandardCopyOption.REPLACE_EXISTING);
        return om;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
	public File getFile(UUID root, String fileName) {
        String filepath = Paths.get(getFolder(root.toString()), fileName).toString();
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






}