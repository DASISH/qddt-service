package no.nsd.qddt.domain;

import no.nsd.qddt.domain.agency.Agency;
import no.nsd.qddt.domain.version.SemVer;
import org.hibernate.envers.Audited;

import javax.persistence.*;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Audited
@MappedSuperclass
public abstract class AbstractEntityAudit extends AbstractEntity {

    /**
     * ChangeKinds are the different ways an entity can be modified by the system/user.
     * First entry will always be CREATED.
     * NEW_REVISION used for taging a version as a release.
     * TYPO, can be used modify without breaking a release.
     * NEW_COPY_OF, used when someone reuses an existing Responsedomain, but want to modify it.
     * Every other version is a IN_DEVELOPMENT change.
     *
     * @author Stig Norland
     */
    public enum ChangeKind {
        CREATED,
        NEW_REVISION,
        NEW_MAJOR,
        NEW_MINOR,
        NEW_PATCH,
        TYPO,
        NEW_COPY_OF,
        IN_DEVELOPMENT
    }

    /**
     * I am the beginning of the end, and the end of time and space.
     * I am essential to creation, and I surround every place.
     * What am I?
     */

    @ManyToOne
    @JoinColumn(name = "agency_id")
    private Agency agency;


    @Column(name = "name")
    private String name;

    @Transient
    private SemVer version;

    @Enumerated(EnumType.STRING)
    private ChangeKind changeKind = ChangeKind.CREATED;

    @Column(name = "change_comment")
    private String changeComment;

    protected AbstractEntityAudit() { }

    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }

    public SemVer getVersion() {
        return version;
    }

    public void setVersion(SemVer version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ChangeKind getChangeKind() {
        return changeKind;
    }

    public void setChangeKind(ChangeKind changeKind) {
        this.changeKind = changeKind;
    }

    public String getChangeComment() {
        return changeComment;
    }

    public void setChangeComment(String changeComment) {
        this.changeComment = changeComment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        AbstractEntityAudit that = (AbstractEntityAudit) o;

        if (agency != null ? !agency.equals(that.agency) : that.agency != null) return false;
         return !(name != null ? !name.equals(that.name) : that.name != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (agency != null ? agency.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return  super.toString()+ '\'' +
                ", name='" + name + '\'' +
                ", changeKind=" + changeKind +
                ", changeComment='" + changeComment + '\'';
    }


    /**
     * The letter "E".
     */
}