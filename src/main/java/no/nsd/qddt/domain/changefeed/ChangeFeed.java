package no.nsd.qddt.domain.changefeed;

import com.google.common.base.Objects;
import no.nsd.qddt.domain.elementref.AbstractElementRef;
import no.nsd.qddt.domain.elementref.IElementRef;
import no.nsd.qddt.domain.user.User;
import no.nsd.qddt.utils.SecurityContext;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author Stig Norland
 */
@Entity
@Table(name = "change_feed")
public class ChangeFeed extends AbstractElementRef {

    @Id
    private Long id;

    @Column(name = "updated", nullable = false)
    @Version
    private Timestamp modified;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private User modifiedBy;

    @Enumerated(EnumType.STRING)
    private ActionKind action;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getModified() {
        return modified;
    }

    public void setModified(Timestamp modified) {
        this.modified = modified;
    }

    public User getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public ActionKind getAction() {
        return action;
    }

    public void setAction(ActionKind action) {
        this.action = action;
    }

    public ChangeFeed(IElementRef ref, ActionKind action) {
        super( ref.getElementKind(), ref.getElementId(), ref.getElementRevision() );
        this.modifiedBy = SecurityContext.getUserDetails().getUser();
        this.action = action;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals( o )) return false;
        ChangeFeed that = (ChangeFeed) o;
        return Objects.equal( id, that.id ) &&
            Objects.equal( modified, that.modified ) &&
            action == that.action;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode( super.hashCode(), id, modified, action );
    }

    @Override
    public String toString() {
        return "{\"ChangeFeed\": { "
            + ", \"id\":\"" + id + "\""
            + ", \"modified\":" + modified
            + ", \"action\":\"" + action + "\""
            + ", \"refId\":" + getElementId()
            + ", \"refRevision\":\"" + getElementRevision() + "\""
            + ", \"refKind\":" + getElementKind()
            + ", \"name\":\"" + getName() + "\""
            + ", \"version\":" + getVersion()
            + "} }";
    }


}
