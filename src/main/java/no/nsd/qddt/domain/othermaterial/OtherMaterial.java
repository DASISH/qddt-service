package no.nsd.qddt.domain.othermaterial;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

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
@Entity
@Table(name = "OTHER_MATERIAL")
public class OtherMaterial extends AbstractEntityAudit {

    @Column(name = "owner_uuid")
    @Type(type="pg-uuid")
    private UUID ownerGuid;

    @ManyToOne
    @JoinColumn(name="owner_uuid", insertable=false, updatable=false)
    private TopicGroup topicGroup;

    private String path;

    private String description;

    public TopicGroup getTopicGroup() {
        return topicGroup;
    }

    public void setTopicGroup(TopicGroup topicGroup) {
        this.topicGroup = topicGroup;
    }

    public UUID getOwnerGuid() {
        return ownerGuid;
    }

    public void setOwnerGuid(UUID ownerGuid) {
        this.ownerGuid = ownerGuid;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OtherMaterial)) return false;
        if (!super.equals(o)) return false;

        OtherMaterial that = (OtherMaterial) o;

        if (getTopicGroup() != null ? !getTopicGroup().equals(that.getTopicGroup()) : that.getTopicGroup() != null)
            return false;
        if (getPath() != null ? !getPath().equals(that.getPath()) : that.getPath() != null) return false;
        return !(getDescription() != null ? !getDescription().equals(that.getDescription()) : that.getDescription() != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getTopicGroup() != null ? getTopicGroup().hashCode() : 0);
        result = 31 * result + (getPath() != null ? getPath().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OtherMaterial{" +
                "topicGroup=" + topicGroup +
                ", path='" + path + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
