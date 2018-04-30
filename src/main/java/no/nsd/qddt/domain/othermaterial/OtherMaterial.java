package no.nsd.qddt.domain.othermaterial;

import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.UUID;


/**
 * This class is just a placeholder for functionality not implemented.
 * Storing of arbitrary data is best suited for none relational datastores.
 * A simple but not very recommended solution would be to use file system and
 * rename files to guid and store the original filename in the attachment repository.
 *
 * @author Stig Norland
 */

@Audited
@Embeddable
public class OtherMaterial implements Cloneable {

    private String fileName;

    private String description;

    private String fileType;

    @Column(name = "original_name", updatable = false, nullable = false)
    private String originalName;

    private long size;

    @Type(type="pg-uuid")
    @Column(name="org_ref")
    private UUID orgRef;


    public OtherMaterial(){

    }

    public OtherMaterial( MultipartFile file){
        setOriginalName(file.getOriginalFilename());
        setFileType(file.getContentType());
        setSize(file.getSize());
        setDescription(null);
    }

    public OtherMaterial(String name, String fileType, long size, String description) {
        setOriginalName(name);
        setFileType(fileType);
        setSize(size);
        setDescription(description);
    }

    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getSize() {
        return size;
    }
    public void setSize(long size) {
        this.size = size;
    }

    public String getDescription() {
        return description;
    }
    private void setDescription(String description) {
        this.description = description;
    }

    public String getOriginalName() {
        return originalName;
    }
    public void setOriginalName(String originalName) {
        this.originalName = originalName;
        this.fileName = originalName.toUpperCase().replace(' ','_').replace('.','_').concat("00");
    }

    public UUID getOrgRef() {
        return  orgRef ;
    }

    /**
    *   This function is safe to activate, nothing will be overwitten.
    *
    **/
    public OtherMaterial setOrgRef(UUID orgRef) {
        // we want to keep reference to the first path for all decendances of the root...
        if (this.orgRef == null)
            this.orgRef = orgRef;
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OtherMaterial)) return false;
        if (!super.equals(o)) return false;

        OtherMaterial that = (OtherMaterial) o;

        if (size != that.size) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (fileType != null ? !fileType.equals(that.fileType) : that.fileType != null) return false;
        return originalName != null ? originalName.equals(that.originalName) : that.originalName == null;

    }

    @Override
    public String toString() {
        return "OtherMaterial{" +
            ", description='" + description + '\'' +
            ", fileType='" + fileType + '\'' +
            ", originalName='" + originalName + '\'' +
            ", size=" + size +
            "} ";
    }


    @Override
    public int hashCode() {

        int result = 31 * (fileName != null ? fileName.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (fileType != null ? fileType.hashCode() : 0);
        result = 31 * result + (originalName != null ? originalName.hashCode() : 0);
        result = 31 * result + (int) (size ^ (size >>> 32));
        return result;
    }



    @Override
    public OtherMaterial clone() {
        return new OtherMaterial(fileName,fileType, size, description).setOrgRef(this.orgRef);
    }

}