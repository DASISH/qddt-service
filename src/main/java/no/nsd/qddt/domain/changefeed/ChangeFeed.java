package no.nsd.qddt.domain.changefeed;

import no.nsd.qddt.domain.user.User;
import no.nsd.qddt.domain.user.json.UserJson;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Entity
@Table(name = "change_log")
@Immutable
public class ChangeFeed  {

    @EmbeddedId
    private ChangeFeedKey changeFeedKey;

    @Column(name = "ref_rev", insertable=false, updatable=false )
    protected Integer refRev;

    @Column(name = "ref_kind")
    private  String refKind;

    @Column(name = "ref_change_kind")
    private  String refChangeKind;

    @Column(name = "ref_modified")
    private Timestamp modified;

    @ManyToOne
    @JoinColumn(name = "ref_modified_by",  updatable = false)
    private User modifiedBy;

    @Column(name = "element_id")
    private UUID elementId;

    @Column(name = "element_revision")
    private  Integer elementRevision;

    @Column(name = "element_kind")
    private String elementKind;

    @Column(name = "name")
    private String name;

    public UUID getRefId() {
        return changeFeedKey.refId;
    }


    public Integer getRefRev() {
        return refRev;
    }


    public String getRefKind() {
        return refKind;
    }

    public void setRefKind(String refKind) {
        this.refKind = refKind;
    }

    public String getRefChangeKind() {
        return refChangeKind;
    }

    public void setRefChangeKind(String refChangeKind) {
        this.refChangeKind = refChangeKind;
    }

    public ActionKind getRefAction() {
        return changeFeedKey.refAction;
    }

    public void setRefAction(ActionKind refAction) {
        this.changeFeedKey.refAction = refAction;
    }

    public Timestamp getModified() {
        return modified;
    }

    public void setModified(Timestamp modified) {
        this.modified = modified;
    }

    public UserJson getModifiedBy() {
        return new UserJson( modifiedBy);
    }

    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public UUID getElementId() {
        return (elementId == null) ? elementId = UUID.randomUUID() :  elementId;
    }

    public void setElementId(UUID elementId) {
        this.elementId = elementId;
    }

    public Integer getElementRevision() {
        return elementRevision;
    }

    public void setElementRevision(Integer elementRevision) {
        this.elementRevision = elementRevision;
    }

    public String getElementKind() {
        return elementKind;
    }

    public void setElementKind(String elementKind) {
        this.elementKind = elementKind;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ChangeFeed() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChangeFeed that = (ChangeFeed) o;

        if (!Objects.equals( refRev, that.refRev )) return false;
        if (!Objects.equals( refKind, that.refKind )) return false;
        if (!Objects.equals( refChangeKind, that.refChangeKind ))
            return false;
        if (!Objects.equals( modified, that.modified )) return false;
        if (!Objects.equals( modifiedBy, that.modifiedBy )) return false;
        if (!Objects.equals( elementId, that.elementId )) return false;
        if (!Objects.equals( elementRevision, that.elementRevision ))
            return false;
        if (!Objects.equals( elementKind, that.elementKind )) return false;
        return Objects.equals( name, that.name );
    }

    @Override
    public int hashCode() {
        int result = refRev != null ? refRev.hashCode() : 0;
        result = 31 * result + (refKind != null ? refKind.hashCode() : 0);
        result = 31 * result + (refChangeKind != null ? refChangeKind.hashCode() : 0);
        result = 31 * result + (modified != null ? modified.hashCode() : 0);
        result = 31 * result + (modifiedBy != null ? modifiedBy.hashCode() : 0);
        result = 31 * result + (elementId != null ? elementId.hashCode() : 0);
        result = 31 * result + (elementRevision != null ? elementRevision.hashCode() : 0);
        result = 31 * result + (elementKind != null ? elementKind.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{\"ChangeFeed\":{"
            + "\"changeFeedKey\":" + changeFeedKey
            + ", \"refKind\":\"" + refKind + "\""
            + ", \"refChangeKind\":\"" + refChangeKind + "\""
            + ", \"modified\":" + modified
            + ", \"modifiedBy\":" + modifiedBy
            + ", \"elementId\":" + elementId
            + ", \"elementRevision\":\"" + elementRevision + "\""
            + ", \"elementKind\":\"" + elementKind + "\""
            + ", \"name\":\"" + name + "\""
            + "}}";
    }


}
