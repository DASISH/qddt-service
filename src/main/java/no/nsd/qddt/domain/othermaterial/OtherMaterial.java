package no.nsd.qddt.domain.othermaterial;

import com.fasterxml.jackson.annotation.JsonIgnore;
import no.nsd.qddt.domain.AbstractEntity;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.UUID;

//import no.nsd.qddt.domain.downloadtoken.DownloadToken;

/**
 * This class is just a placeholder for functionality not implemented.
 * Storing of arbitrary data is best suited for none relational datastores.
 * A simple but not very recommended solution would be to use file system and
 * rename files to guid and store the original filename in the attachment repository.
 *
 * @author Stig Norland
 */

@Entity
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Table(name = "OTHER_MATERIAL")
public class OtherMaterial extends AbstractEntity {

    @Type(type="pg-uuid")
    private UUID owner;

    private String fileName;

    private String description;

    private String fileType;

    private String originalName;

    private long size;

    @Type(type="pg-uuid")
    @JsonIgnore
    private UUID orgRef;

    public OtherMaterial(){

    }

    public OtherMaterial(UUID owner,MultipartFile file, String description){
        setOwner(owner);
        setFileName(file.getName());
        setOriginalName(file.getOriginalFilename());
        setFileType(file.getContentType());
        setSize(file.getSize());
        setDescription(description);
    }

    public OtherMaterial(UUID owner, String name, String fileType, long size, String description) {
        setOwner(owner);
        setFileName(name);
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

    public UUID getOwner() {
        if (orgRef == null)
            return owner;
        else
            return orgRef;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
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

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public UUID getOrgRef() {
        return orgRef;
    }

    public void setOrgRef(UUID orgRef) {
        this.orgRef = orgRef;
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
                "} " + super.toString();
    }


    @Override
    public int hashCode() {

        int result = super.hashCode();
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (fileType != null ? fileType.hashCode() : 0);
        result = 31 * result + (originalName != null ? originalName.hashCode() : 0);
        result = 31 * result + (int) (size ^ (size >>> 32));
        return result;
    }

    @JsonIgnore
    private boolean hasRun = false;


    public void makeNewCopy(UUID newOwner){
        if (hasRun) return;
        setOrgRef(getOwner());
        setOwner(newOwner);
        setId(UUID.randomUUID());
        hasRun = true;

    }

}