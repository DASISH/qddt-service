package no.nsd.qddt.domain;

import org.hibernate.envers.Audited;
import org.springframework.util.ObjectUtils;

import javax.persistence.*;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Audited
@MappedSuperclass
public abstract class AbstractEntityAudit extends AbstractEntity {


    //UUID part of the URN, saves as binary for most db's (PostgreSQL, SQL Server have native types)
    @Column(name = "guid", columnDefinition = "BINARY(16)")
    private UUID guid = UUID.randomUUID();


    @Column(name = "name")
    private String name;

    @Transient
    private String version;

    protected AbstractEntityAudit(){}

//    @ManyToOne
//    @JoinColumn(name = "change_id")
    private ChangeReason.ChangeKind changeReason;

    @Column(name = "change_comment")
    private String changeComment;

    public UUID getGuid() { return guid;}

    public void setGuid(UUID guid) {this.guid = guid;}

    public abstract Agency getAgency();

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Urn getUrn() {
        return new Urn(getAgency(), getGuid(), getVersion());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public ChangeReason.ChangeKind getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(ChangeReason.ChangeKind changeReason) {
        this.changeReason = changeReason;
    }

    public String getChangeComment() {
        return changeComment;
    }

    public void setChangeComment(String changeComment) {
        this.changeComment = changeComment;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AbstractEntityAudit)) {
            return false;
        }
        AbstractEntityAudit that = (AbstractEntityAudit) obj;
        return ObjectUtils.nullSafeEquals(super.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        int result = super.getId() != null ? super.getId().hashCode() : 0;
        result = 31 * result + (guid != null ? guid.hashCode() : 0);
        result = 31 * result + (super.getCreated() != null ? super.getCreated().hashCode() : 0);
        result = 31 * result + (super.getCreatedBy() != null ? super.getCreatedBy().hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (changeReason != null ? changeReason.hashCode() : 0);
        result = 31 * result + (changeComment != null ? changeComment.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return  " ,id=" + super.getId() +
                ", guid=" + guid +
                ", created=" + super.getCreated() +
                ", createdBy=" + super.getCreatedBy() +
                ", name='" + name + '\'' +
                ", changeReason=" + changeReason +
                ", changeComment='" + changeComment + '\'';
    }

}
