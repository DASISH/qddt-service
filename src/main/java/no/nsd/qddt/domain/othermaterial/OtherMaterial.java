package no.nsd.qddt.domain.othermaterial;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.classes.elementref.ElementKind;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.persistence.Embeddable;
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

    @Type(type="pg-uuid")
    @Column(name="original_owner")
    private UUID originalOwner;

    @Column(name = "original_name", updatable = false, nullable = false)
    private String originalName;

    private String fileName;

    private String fileType;

    private long size;

    private String description;


    public OtherMaterial(){

    }

    public OtherMaterial( MultipartFile file){
        setOriginalName(file.getOriginalFilename());
        setFileType(file.getContentType());
        setSize(file.getSize());
        setDescription(null);
    }

    public OtherMaterial(String originalName, String fileType, long size, String description) {
        setOriginalName(originalName);
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
    private OtherMaterial setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getOriginalName() {
        return originalName;
    }
    public void setOriginalName(String originalName) {
        this.originalName = originalName;
        this.fileName = originalName.toUpperCase().replace(' ','_').replace('.','_').concat("00");
    }

    public UUID getOriginalOwner() {
        return originalOwner;
    }

    /**
    *   This function is safe to activate, nothing will be overwritten.
    *
    **/
    public OtherMaterial setOriginalOwner(UUID originalOwner) {
        // we want to keep reference to the first path for all descendants of the root...
        if (this.originalOwner == null)
            this.originalOwner = originalOwner;
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
        return new OtherMaterial(originalName,fileType, size, description).setOriginalOwner(this.originalOwner );
    }

    private static final String OM_REF_FORMAT=
        "%1$s<r:ExternalAid scopeOfUniqueness = \"Maintainable\" isUniversallyUnique = \"true\">\n" +
        "%1$s\t%3$s\n" +
        "%1$s\t<MaintainableObject>\n" +
        "%1$s\t\t<TypeOfObject>%4$s</TypeOfObject>\n" +
        "%1$s\t\t<MaintainableID type=\"ID\">%5$s</MaintainableID>\n" +
        "%1$s\t</MaintainableObject>\n" +
        "%1$s\t<Description>%6$s</Description>\n" +
        "%1$s\t<ExternalURLReference>https://qddt.nsd.no/preview/%7$s</ExternalURLReference>\n" +
        "%1$s\t<MIMEType>%8$s</MIMEType>\n" +
        "%1$s</r:ExternalAid>\n";

    public String toDDIXml(AbstractEntityAudit entity, String tabs) {

        return String.format( OM_REF_FORMAT, tabs,
            "",
            "<r:URN type=\"URN\" typeOfIdentifier=\"Canonical\">urn:ddi:" +getUrnId(entity) + "</r:URN>",
            ElementKind.getEnum( entity.getClassKind()).getClassName(),
            entity.getId() + ":" + entity.getVersion().toDDIXml(),
            getDescription(),
            entity.getId().toString() + '/' + this.fileName ,
            getFileType()
        );
    }

    public String getUrnId(AbstractEntityAudit entity) {
        return  String.format( "%1$s:%2$s:%3$s",  entity.getAgency().getName() , entity.getId() , this.fileName);
    }
}
