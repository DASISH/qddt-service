package no.nsd.qddt.domain.othermaterial.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import no.nsd.qddt.domain.AbstractEntity;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
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
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "owner_type")
@Table(name = "OTHER_MATERIAL")
public class OtherMaterial extends AbstractEntity implements Cloneable {

    @Type(type="pg-uuid")
    @Column(name = "OWNER_ID", insertable = false, updatable=false)
    private UUID ownerId;

    @Column(name = "OWNER_TYPE",  insertable=false, updatable = false)
    private String ownerType;

    private String fileName;

    private String description;

    private String fileType;

    private String originalName;

    private long size;


    @Type(type="pg-uuid")
    @Column(name="org_ref")
    private UUID orgRef;

    @JsonIgnore
    @JsonBackReference(value = "orgReferences")
    @ManyToOne()
    @JoinColumn(name = "org_ref",updatable = false,insertable = false)
    private OtherMaterial source;

    @JsonIgnore
    @JsonManagedReference(value = "orgReferences")
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE,CascadeType.DETACH})
    @JoinColumn(name = "org_ref")
    private final Set<OtherMaterial> referencesBy = new HashSet<>(0);


    public OtherMaterial(){

    }

    public OtherMaterial(UUID owner, MultipartFile file){
        setOwnerId( owner );
        setFileName(file.getName());
        setOriginalName(file.getOriginalFilename());
        setFileType(file.getContentType());
        setSize(file.getSize());
        setDescription(null);
    }

    public OtherMaterial(UUID owner,String ownerType,  MultipartFile file){
        this(owner, file);
        setField("ownerType", ownerType);
    }



    public OtherMaterial(UUID owner, String name, String fileType, long size, String description) {
        setOwnerId( owner );
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

    public UUID getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
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
    }

    public UUID getOrgRef() {
        return orgRef;
    }
    protected void setOrgRef(UUID orgRef) {
        this.orgRef = orgRef;
    }


    @JsonIgnore
    public Set<OtherMaterial> getReferencesBy() {
        return referencesBy;
    }


    @JsonIgnore
    public OtherMaterial getSource() {
        return source;
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
        result = 31 * result + (ownerId != null ? ownerId.hashCode() : 0);
        result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (fileType != null ? fileType.hashCode() : 0);
        result = 31 * result + (originalName != null ? originalName.hashCode() : 0);
        result = 31 * result + (int) (size ^ (size >>> 32));
        return result;
    }



    @Override
    public OtherMaterial clone() {
        OtherMaterial retval = new OtherMaterial(ownerId,fileName,fileType, size, description);
        retval.setOrgRef(getId());
        retval.setId(UUID.randomUUID());
        return retval;
    }

    @PreRemove
    private void cleanUp(){
        LOG.debug("Removal of file " + getOriginalName() +  ", on hold, due to revision concerns");
    }

}