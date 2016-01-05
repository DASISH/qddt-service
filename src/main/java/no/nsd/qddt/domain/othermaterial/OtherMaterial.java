package no.nsd.qddt.domain.othermaterial;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
@Table(name = "OTHER_MATERIAL")
public class OtherMaterial extends AbstractEntityAudit {

    @ManyToOne
    @JoinColumn(name="topicgroup_id", insertable=false, updatable=false)
    private TopicGroup topicGroup;

    private String path;

    private String description;

    private String fileType;

    private String originalName;

    private long size;

    public OtherMaterial(){

    }

    public OtherMaterial(String name, String fileType, long size) {
        setName(name);
        this.fileType = fileType;
        this.size = size;
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

    public TopicGroup getTopicGroup() {
        return topicGroup;
    }

    public void setTopicGroup(TopicGroup topicGroup) {
        this.topicGroup = topicGroup;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OtherMaterial)) return false;
        if (!super.equals(o)) return false;

        OtherMaterial that = (OtherMaterial) o;

        if (size != that.size) return false;
        if (topicGroup != null ? !topicGroup.equals(that.topicGroup) : that.topicGroup != null) return false;
        if (path != null ? !path.equals(that.path) : that.path != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (fileType != null ? !fileType.equals(that.fileType) : that.fileType != null) return false;
        return originalName != null ? originalName.equals(that.originalName) : that.originalName == null;

    }

    @Override
    public String toString() {
        return "OtherMaterial{" +
                "topicGroup=" + topicGroup +
                ", path='" + path + '\'' +
                ", description='" + description + '\'' +
                ", fileType='" + fileType + '\'' +
                ", originalName='" + originalName + '\'' +
                ", size=" + size +
                "} " + super.toString();
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (topicGroup != null ? topicGroup.hashCode() : 0);
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (fileType != null ? fileType.hashCode() : 0);
        result = 31 * result + (originalName != null ? originalName.hashCode() : 0);
        result = 31 * result + (int) (size ^ (size >>> 32));
        return result;
    }


}