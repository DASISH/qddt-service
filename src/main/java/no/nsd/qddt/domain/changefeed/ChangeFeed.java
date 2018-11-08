package no.nsd.qddt.domain.changefeed;

import com.google.common.base.Objects;
import no.nsd.qddt.domain.user.User;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Entity
@Table(name = "change_log")
@Immutable
public class ChangeFeed  {

//    @SuppressWarnings("unused")
//    @Id
//    private Integer id;
//
//    @Column(name = "ref_id")
//    protected UUID refId;
//
//    @Column(name = "ref_rev")
//    protected Integer refRev;

    @EmbeddedId
    private ChangeFeedKey changeFeedKey;

    @Column(name = "ref_kind")
    private  String refKind;

    @Column(name = "ref_change_kind")
    private  String refChangeKind;

    @Column(name = "ref_modified")
    private Timestamp refModified;

    @ManyToOne
    @JoinColumn(name = "ref_modified_by",  updatable = false)
//    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private User modifiedBy;

    @Column(name = "ref_action")
    private Short refAction;

    @Column(name = "element_id")
    private  UUID elementId;

    @Column(name = "element_revision")
    private  Integer elementRevision;

    @Column(name = "element_kind")
    private String elementKind;

    @Column(name = "name")
    private String name;

    public ChangeFeedKey getChangeFeedKey() {
        return changeFeedKey;
    }

    public void setChangeFeedKey(ChangeFeedKey changeFeedKey) {
        this.changeFeedKey = changeFeedKey;
    }

    //    public UUID getRefId() {
//        return refId;
//    }
//
//    public void setRefId(UUID refId) {
//        this.refId = refId;
//    }
//
//    public Integer getRefRev() {
//        return refRev;
//    }
//
//    public void setRefRev(Integer refRev) {
//        this.refRev = refRev;
//    }

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

    public Timestamp getRefModified() {
        return refModified;
    }

    public void setRefModified(Timestamp refModified) {
        this.refModified = refModified;
    }

    public User getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Short getRefAction() {
        return refAction;
    }

    public void setRefAction(Short refAction) {
        this.refAction = refAction;
    }

    public UUID getElementId() {
        return elementId;
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
        return Objects.equal( changeFeedKey, that.changeFeedKey ) &&
            Objects.equal( refKind, that.refKind ) &&
            Objects.equal( refChangeKind, that.refChangeKind ) &&
            Objects.equal( refModified, that.refModified ) &&
            Objects.equal( modifiedBy, that.modifiedBy ) &&
            Objects.equal( refAction, that.refAction ) &&
            Objects.equal( elementId, that.elementId ) &&
            Objects.equal( elementRevision, that.elementRevision ) &&
            Objects.equal( elementKind, that.elementKind ) &&
            Objects.equal( name, that.name );
    }

    @Override
    public int hashCode() {
        return Objects.hashCode( changeFeedKey, refKind, refChangeKind, refModified, modifiedBy, refAction, elementId, elementRevision, elementKind, name );
    }

    @Override
    public String toString() {
        return "{\"ChangeFeed\":{"
            + "\"changeFeedKey\":" + changeFeedKey
            + ", \"refKind\":\"" + refKind + "\""
            + ", \"refChangeKind\":\"" + refChangeKind + "\""
            + ", \"refModified\":" + refModified
            + ", \"modifiedBy\":" + modifiedBy
            + ", \"refAction\":\"" + refAction + "\""
            + ", \"elementId\":" + elementId
            + ", \"elementRevision\":\"" + elementRevision + "\""
            + ", \"elementKind\":\"" + elementKind + "\""
            + ", \"name\":\"" + name + "\""
            + "}}";
    }


}
