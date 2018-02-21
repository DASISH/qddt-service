package no.nsd.qddt.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import no.nsd.qddt.domain.agency.Agency;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.embedded.Version;
import no.nsd.qddt.domain.pdf.PdfReport;
import no.nsd.qddt.domain.user.User;
import no.nsd.qddt.exception.StackTraceFilter;
import no.nsd.qddt.utils.SecurityContext;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Audited
@MappedSuperclass
public abstract class AbstractEntityAudit extends AbstractEntity  {

    /**
     * ChangeKinds are the different ways an entity can be modified by the system/user.
     * First entry will always be CREATED.
     * TYPO, can be used modify without breaking a release.
     * Every other version is a IN_DEVELOPMENT change.
     */
    public enum ChangeKind {
        CREATED("Created","New element status"),
        UPDATED_PARENT("Parent Updated","ChildSaved as part of parent save"),
        UPDATED_CHILD("Child Updated","ParentSaved as part of child save"),
        UPDATED_HIERARCHY_RELATION("Hierarchy Relation Updated","Element added to a collection, no changes to element itself"),
        IN_DEVELOPMENT("In Development","UnfinishedWork"),
        TYPO("NoMeaningChange","Typo or No Meaning Change"),
        CONCEPTUAL("ConceptualImprovement","Conceptual Improvement"),
        EXTERNAL("RealLifeChange","Real Life Change"),
        OTHER("OtherPurpose","Other Purpose"),
        //. when you discover that you didn't completely fill inn the fields when creating an element, and then add this information later on.
        ADDED_CONTENT("AddContentElement","Add Content Element"),
        BASED_ON("Based on","Based on copy"),
        NEW_COPY("New Copy","Copy new"),
        REFERENCED("Reference of","Concepts can be copied as a reference, to facilitate hierarchical revision trees"),
        TRANSLATED("Translated","Translation of source"),
        ARCHIVED("Archived","READ ONLY");

        ChangeKind(String name, String description){
            this.name = name;
            this.description = description;
        }

        private final String name;

        private final String description;

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public static ChangeKind getEnum(String name) {
            if(name == null)
                throw new IllegalArgumentException();
            for(ChangeKind v : values())
                if(name.equalsIgnoreCase(v.getName())) return v;
            throw new IllegalArgumentException();
        }

        @Override
        public String toString() {
            return "{\"_class\":\"ChangeKind\", " +
                    "\"name\":" + (name == null ? "null" : "\"" + name + "\"") + ", " +
                    "\"description\":" + (description == null ? "null" : "\"" + description + "\"") +
                    "}";
        }
    }

    /**
     * I am the beginning of the end, and the end of time and space.
     * I am essential to creation, and I surround every place.
     * What am I?
     */

    private String name;
    private UUID basedOnObject;
    private Long basedOnRevision;
    private Version version;
    private ChangeKind changeKind;
    private String changeComment;
    private Agency agency;
    private Set<Comment> comments = new HashSet<>();


    protected AbstractEntityAudit() {
//        isArchived = false;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


    @ManyToOne
    @JoinColumn(name = "agency_id",updatable = false, nullable = false)
    public Agency getAgency() {
        return agency;
    }
    public void setAgency(Agency agency) {
        this.agency = agency;
    }


    @Column(name = "based_on_object",updatable = false, nullable = false)
    @Type(type="pg-uuid")
    public UUID getBasedOnObject() {
        return basedOnObject;
    }
    private void setBasedOnObject(UUID basedOnObject) {
        this.basedOnObject = basedOnObject;
    }

    @Column(name = "based_on_revision",updatable = false, nullable = false)
    public Long getBasedOnRevision() {
        return basedOnRevision;
    }
    private void setBasedOnRevision(Long basedOnRevision) {
        this.basedOnRevision = basedOnRevision;
    }

    @Embedded
    public Version getVersion() {
        if (version == null)
            version = new Version(true);
        return version;
    }
    public void setVersion(Version version) {
        this.version = version;
    }


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public ChangeKind getChangeKind() {
        return changeKind;
    }
    public void setChangeKind(ChangeKind changeKind) {
        if (this.changeKind == ChangeKind.IN_DEVELOPMENT &&
                (changeKind == ChangeKind.UPDATED_HIERARCHY_RELATION ||
                changeKind == ChangeKind.UPDATED_PARENT ||
                changeKind == ChangeKind.UPDATED_CHILD ))
        {
            //BUGFIX https://github.com/DASISH/qddt-client/issues/546
            return;
        }
        this.changeKind = changeKind;
    }

    @Column(name = "change_comment",nullable = false)
    @ColumnDefault("")
    public String getChangeComment() {
        return changeComment;
    }
    public void setChangeComment(String changeComment) {
        this.changeComment = changeComment;
    }

    @Where(clause = "is_hidden = 'false'")
    @OneToMany(mappedBy="ownerId", fetch = FetchType.EAGER)
    @NotAudited
    public Set<Comment> getComments() {
        return this.comments;
    }
    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }


    @PrePersist
    private void onInsert(){
        LOG.debug("AstractEntityAudit PrePersist " + this.getClass().getSimpleName());
        User user = SecurityContext.getUserDetails().getUser();
        agency = user.getAgency();
        if (changeKind != ChangeKind.BASED_ON)
            changeKind = ChangeKind.CREATED;
        version = new Version(true);
        beforeInsert();
    }

    @PreUpdate
    private void onUpdate(){
        try {
            LOG.debug("AbstractEntityAudit PreUpdate " + this.getClass().getSimpleName() + " - " + getName());
            Version ver = version;
            if (!isOwnAgency()) {

            }
            ChangeKind change = changeKind;
            if ( (change == ChangeKind.CREATED || change == ChangeKind.BASED_ON) & !ver.isNew()) {
                change = ChangeKind.IN_DEVELOPMENT;
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
                    ver.setVersionLabel("");
                    break;
                case TYPO:
                    ver.incMinor();
                    ver.setVersionLabel("");
                    break;
                case ARCHIVED:
                    ((Archivable)this).setArchived(true);
                    ver.setVersionLabel("");
                case CREATED:
                    break;
                case IN_DEVELOPMENT:
                    ver.setVersionLabel(ChangeKind.IN_DEVELOPMENT.getName());
                    break;
                case UPDATED_PARENT:
                    ver.setVersionLabel("");
                    break;
                default:        // UPDATED_PARENT / UPDATED_CHILD / UPDATED_HIERARCHY_RELATION
                    ver.setVersionLabel("Changes in hierarchy");
                    break;
            }
            version = ver;
            beforeUpdate();
        }catch (Exception ex){
            LOG.error("AbstractEntityAudit::onUpdate",ex);
            StackTraceFilter.filter(ex.getStackTrace()).stream()
                .map(a->a.toString())
                .forEach(LOG::info);
        }
    }

    /**
     * override to add before update code to class
     * */
    protected void beforeUpdate() {}

    /**
     * override to add before insert code to class
     * */
    protected void beforeInsert() {}


    @JsonIgnore
    public boolean isOwnAgency() {
        if (agency == null) return  true;

        return SecurityContext.getUserDetails()
            .getUser()
            .getAgency().equals( this.agency );
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

//    @JsonIgnore
//    /*
//    This function should contain all copy code needed to make a complete copy of hierarchy under this element
//    (an override should propagate downward and call makeNewCopy on it's children).
//     */
//    public void makeNewCopy(Long revision){
//        if (hasRun) return;
//        if (revision != null) {
//            setBasedOnObject(getId());
//            setBasedOnRevision(revision);
//            version.setVersionLabel("COPY OF [" + getName() + "]");
//            setChangeKind( ChangeKind.BASED_ON );
//            setChangeComment("Based on " + getName() );
//        }
//        if(this instanceof Archivable)
//            ((Archivable)this).setArchived(false);
//        setId(UUID.randomUUID());
//        hasRun = true;
//    }


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
        return "{" + super.toString() +
                "\"version\":" + (version == null ? "null" : version) + ", " +
                "\"changeKind\":" + (changeKind == null ? "null" : changeKind) + ", " +
                "\"changeComment\":" + (changeComment == null ? "null" : "\"" + changeComment + "\"") + ", " +
                "\"basedOnObject\":" + (basedOnObject == null ? "null" : basedOnObject) + ", " +
                "\"basedOnRevision\":" + (basedOnRevision == null ? "null" : "\"" + basedOnRevision + "\"") + ", " +
                "\"name\":" + (name == null ? "null" : "\"" + name + "\"") + ", " +
                "\"agency\":" + (agency == null ? "null" : agency) + ", ";
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


    public abstract void fillDoc(PdfReport pdfReport,String counter) throws IOException;

    public ByteArrayOutputStream makePdf() {
        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        try (PdfReport pdf = new PdfReport(pdfOutputStream)) {
            fillDoc(pdf,"");
            pdf.createToc();
        } catch (Exception ex) {
            StackTraceFilter.println(ex.getStackTrace());
        }
        return pdfOutputStream;
    }

}