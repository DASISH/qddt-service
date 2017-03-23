package no.nsd.qddt.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import no.nsd.qddt.domain.agency.Agency;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.commentable.Commentable;
import no.nsd.qddt.domain.embedded.Version;
import no.nsd.qddt.domain.user.User;
import no.nsd.qddt.utils.SecurityContext;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

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
     * TYPO, can be used modify without breaking a release.
     * Every other version is a IN_DEVELOPMENT change.
     *
     * @author Stig Norland
     */
    public enum ChangeKind {
        //New element status
        CREATED,
        //ChildSaved as part of parent save
        UPDATED_PARENT,
        //ParentSaved as part of child save
        UPDATED_CHILD,
        //Element added to a collection, no changes to element itself
        UPDATED_HIERARCY_RELATION,
        //UnfinishedWork
        IN_DEVELOPMENT,
        //TypoOrNoMeaningChange
        TYPO,
        //ConceptualImprovement
        CONCEPTUAL,
        //RealLifeChange
        EXTERNAL,
        //OtherPurpose
        OTHER,
        //AddContentElement. when you discover that you didn't completely fill inn the fields when creating an element, and then add this information later on.
        ADDED_CONTENT,
        /* deprecated */
//        milestone status, this version is published.
//        This was removed as publication is no longer part of the model, now uses list of published elements for each publication.
        MILESTONE,
        BASED_ON,
        NEW_COPY, TRANSLATED
    }


    /**
     * I am the beginning of the end, and the end of time and space.
     * I am essential to creation, and I surround every place.
     * What am I?
     */

    @ManyToOne
    @JoinColumn(name = "agency_id",updatable = false)
    private Agency agency;

    @Column(name = "name")
    private String name;

    @Column(name = "based_on_object", nullable = true)
    @Type(type="pg-uuid")
    private UUID basedOnObject;

    @Column(name = "based_on_revision", nullable = true)
    private Integer basedOnRevision;


    @Embedded
    private Version version;

    @Enumerated(EnumType.STRING)
    private ChangeKind changeKind;

    @Column(name = "change_comment")
    private String changeComment;

    protected AbstractEntityAudit() {

    }


    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }

    public UUID getBasedOnObject() {
        return basedOnObject;
    }

    public void setBasedOnObject(UUID basedOnObject) {
        this.basedOnObject = basedOnObject;
    }

    public Integer getBasedOnRevision() {
        return basedOnRevision;
    }

    public void setBasedOnRevision(Integer basedOnRevision) {
        this.basedOnRevision = basedOnRevision;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
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


    @PrePersist
    private void onInsert(){
        User user = SecurityContext.getUserDetails().getUser();
        agency = user.getAgency();
        changeKind = AbstractEntityAudit.ChangeKind.CREATED;
        version = new Version(true);
    }

    @PreUpdate
    private void onUpdate(){
        Version ver = version;
        AbstractEntityAudit.ChangeKind change = changeKind;
        if (change == AbstractEntityAudit.ChangeKind.CREATED & !ver.isNew()) {
            change = AbstractEntityAudit.ChangeKind.IN_DEVELOPMENT;
            changeKind = change;
        }
        switch (change) {
            case BASED_ON:
            case TRANSLATED:
                ver = new Version();
                break;
            case CONCEPTUAL:
            case EXTERNAL:
            case OTHER:
            case ADDED_CONTENT:
                ver.incMajor();
                break;
            case TYPO:
                ver.incMinor();
                break;
            default:        //CREATED / UPDATED_PARENT / UPDATED_CHILD / UPDATED_HIERARCY_RELATION / IN_DEVELOPMENT
                break;
        }
        version =  ver;
    }

    @PostLoad
    private void customfilter(){
        if (this instanceof Commentable){
            // we have to filter comments manually before sending them over to clients, this will be fixed in
            // a later version of Hibernate
            hideComments(((Commentable)this).getComments());
        }
    }

    private void hideComments(Set<Comment> comments){
        comments.removeIf(c->c.getIsHidden()==true);
        comments.stream().forEach(c-> hideComments(c.getComments()));
    }

    /**
     * None null field compare, (ignores null value when comparing)
     * @param o
     * @return
     */
    public boolean fieldCompare(AbstractEntityAudit o){

        if (agency != null && !agency.equals(o.agency)) return false;
        if (name != null && !name.equals(o.name)) return false;
        if (basedOnObject != null && !basedOnObject.equals(o.basedOnObject)) return false;
        if (version != null && !version.equals(o.version)) return false;
        if (changeKind != null && !changeKind.equals(o.changeKind)) return false;
        if (changeComment != null && !changeComment.equals(o.changeComment)) return false;

        return super.fieldCompare(o);
    }

    @JsonIgnore
    public boolean isBasedOn(){
        return (getChangeKind() == ChangeKind.BASED_ON | getChangeKind() == ChangeKind.TRANSLATED);
    }

    @JsonIgnore
    public boolean isNewCopy(){
        return (getChangeKind() == ChangeKind.NEW_COPY )
                | (getId() == null & getChangeKind() != null & getChangeKind()!= ChangeKind.CREATED)
                | (!getVersion().isNew() & getId() == null );
    }

    @JsonIgnore
    @Transient
    protected boolean hasRun = false;

    @JsonIgnore
    /*
    This function should contain all copy code needed to make a complete copy of hierarchy under this element
    (an override should propigate downward and call makeNewCopy on it's children).
     */
    public void makeNewCopy(Integer revision){
        if (hasRun) return;
        if (revision != null) {
            setBasedOnObject(getId());
            setBasedOnRevision(revision);
            version.setVersionLabel("COPY OF [" + getName() + "]");
        }
        setId(UUID.randomUUID());
        hasRun = true;
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
        return "{ " +
                super.toString() +
                ", " + agency +
                ", name='" + name + '\'' +
                ", basedOnObject=" + basedOnObject +
                ", version='" + version + '\'' +
                ", changeKind=" + changeKind +
                ", changeComment='" + changeComment + '\'' +
                "} " ;
    }

    @Override
    public String toDDIXml(){
        return  super.toDDIXml() +
                getAgency().toDDIXml() +
                getVersion().toDDIXml() +
                "<BasedOnObject>" +
                "   <BasedOnReference>" + getBasedOnObject() + "</BasedOnReference>" +
                "</BasedOnObject>";
    }


}