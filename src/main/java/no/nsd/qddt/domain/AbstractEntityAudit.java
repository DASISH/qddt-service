package no.nsd.qddt.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import no.nsd.qddt.domain.agency.Agency;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.comment.CommentJsonEdit;
import no.nsd.qddt.domain.elementref.ElementKind;
import no.nsd.qddt.domain.embedded.Version;
import no.nsd.qddt.domain.pdf.PdfReport;
import no.nsd.qddt.domain.user.User;
import no.nsd.qddt.exception.StackTraceFilter;
import no.nsd.qddt.security.SecurityContext;
import no.nsd.qddt.utils.StringTool;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Audited
@MappedSuperclass
public abstract class AbstractEntityAudit extends AbstractEntity  implements IElementRefType {

    /**
     * ChangeKinds are the different ways an entity can be modified by the system/user.
     * First entry will always be CREATED.
     * TYPO, can be used modify without breaking a release.
     * Every other version is a IN_DEVELOPMENT change.
     */
    public enum ChangeKind {
        CREATED("Created","New element status"),
        BASED_ON("Based on","Based on copy"),
        NEW_COPY("New Copy","Copy new"),
        TRANSLATED("Translated","Translation of source"),
        REFERENCED("Reference of","Concepts can be copied as a reference, to facilitate hierarchical revision trees"),
        UPDATED_PARENT("Parent Updated","ChildSaved as part of parent save"),
        UPDATED_CHILD("Child Updated","ParentSaved as part of child save"),
        UPDATED_HIERARCHY_RELATION("Hierarchy Relation Updated","Element added to a collection, no changes to element itself"),
        IN_DEVELOPMENT("In Development","UnfinishedWork"),
        TYPO("NoMeaningChange","Typo or No Meaning Change"),
        CONCEPTUAL("ConceptualImprovement","Conceptual Improvement"),
        EXTERNAL("RealLifeChange","Real Life Change"),
        OTHER("OtherPurpose","Other Purpose"),
        ADDED_CONTENT("AddContentElement","Added content later on"),
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
            return "{ " +
                    "\"ChangeKind\": " + (name == null ? "null" : "\"" + name + "\"") +
                    "}";
        }
    }


    /**
     * I am the beginning of the end, and the end of time and space.
     * I am essential to creation, and I surround every place.
     * What am I?
     */

//    @NotAudited
    @ManyToOne
    @JoinColumn(name = "agency_id",updatable = false, nullable = false)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Agency agency;

    @Column(name = "name")
    private String name;


    @Column(name = "based_on_object",updatable = false)
    @Type(type="pg-uuid")
    private UUID basedOnObject;

    @Column(name = "based_on_revision",updatable = false)
    private Integer basedOnRevision;


    @Embedded
    private Version version;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChangeKind changeKind;

    @Column(name = "change_comment",nullable = false)
    private String changeComment;

    @Transient
    @JsonSerialize
    @JsonDeserialize
    private String xmlLang = "eng-GB";

    @NotAudited
    @OrderBy("owner_idx desc")
    @OrderColumn(name="owner_idx")
    @OneToMany(mappedBy="ownerId", cascade = CascadeType.ALL, fetch = FetchType.EAGER,orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();


    @Transient
    @JsonSerialize
    @JsonDeserialize
    private String classKind;


    protected AbstractEntityAudit() {
        try {
            classKind =ElementKind.getEnum( this.getClass().getSimpleName() ).toString();
        } catch (Exception e) {
            classKind = this.getClass().getSimpleName();
        }
    }

    public String getClassKind() {
        return classKind;
    }

    public void setClassKind(String classKind) {
        this.classKind = classKind;
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

    protected void setBasedOnObject(UUID basedOnObject) {
        this.basedOnObject = basedOnObject;
    }

    public Integer getBasedOnRevision() {
        return basedOnRevision;
    }

    protected void setBasedOnRevision(Integer basedOnRevision) {
        this.basedOnRevision = basedOnRevision;
    }

    public Version getVersion() {
        if (version == null)
            version = new Version(true);
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

    public String getChangeComment() {
        return changeComment;
    }

    public void setChangeComment(String changeComment) {
        this.changeComment = changeComment;
    }

    public List<CommentJsonEdit> getComments() {
        return this.comments.stream().map(CommentJsonEdit::new).collect(Collectors.toList());
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getXmlLang() {
        return xmlLang;
    }

    public void setXmlLang(String xmlLang) {
        this.xmlLang = xmlLang;
    }


    @PrePersist
    private void onInsert(){
        LOG.info("AstractEntityAudit PrePersist " + this.getClass().getSimpleName());
        User user = SecurityContext.getUserDetails().getUser();
        setAgency( user.getAgency() );
        setModifiedBy( user );

        if (changeKind == null || changeKind.ordinal() > ChangeKind.REFERENCED.ordinal()) {
            changeKind = ChangeKind.CREATED;
            changeComment = ChangeKind.CREATED.description;
        }
        version = new Version(true);
        beforeInsert();
    }

    @PreUpdate
    private void onUpdate(){
        try {
            LOG.info("AbstractEntityAudit PreUpdate " + this.getClass().getSimpleName() + " - " + getName());
            Version ver = version;
            ChangeKind change = changeKind;
            User user = SecurityContext.getUserDetails().getUser();
            setModifiedBy( user );

            if (change.ordinal() <= ChangeKind.REFERENCED.ordinal() & !ver.isNew()) {
                change = ChangeKind.IN_DEVELOPMENT;
                setChangeKind( change );
            }
            if (StringTool.IsNullOrTrimEmpty(changeComment))        // insert default comment if none was supplied, (can occur with auto touching (hierarchy updates etc))
                setChangeComment( change.description );
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
                    ((IArchived)this).setArchived(true);
                    ver.setVersionLabel("");
                case CREATED:
                    break;
                case IN_DEVELOPMENT:
                    ver.setVersionLabel(AbstractEntityAudit.ChangeKind.IN_DEVELOPMENT.getName());
                    break;
                case UPDATED_PARENT:
                    ver.setVersionLabel("");
                    break;
                default:        // UPDATED_PARENT / UPDATED_CHILD / UPDATED_HIERARCHY_RELATION
                    ver.setVersionLabel("Changes in hierarchy");
                    break;
            }
            setVersion (ver);
            beforeUpdate();
        } catch (Exception ex){
            LOG.error("AbstractEntityAudit::onUpdate",ex);

        }
    }

    protected abstract void beforeUpdate();

    protected abstract void beforeInsert();


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
    (an override should propagate downward and call makeNewCopy on it's children).
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
        return  super.toString() +
                "\"version\":" + (version == null ? "null" : version) + ", " +
//                "\"changeKind\":" + (changeKind == null ? "null" : changeKind) + ", " +
//                "\"changeComment\":" + (changeComment == null ? "null" : "\"" + changeComment + "\"") + ", " +
                "\"basedOnObject\":" + (basedOnObject == null ? "null" : basedOnObject) + ", " +
                "\"basedOnRevision\":" + (basedOnRevision == null ? "null" : "\"" + basedOnRevision + "\"") + ", " +
                "\"name\":" + (name == null ? "null" : "\"" + name + "\"") + ", " +
                "\"agency\":" + (agency == null ? "null" : agency) + ", ";
    }



    public abstract void fillDoc(PdfReport pdfReport,String counter);

    public ByteArrayOutputStream makePdf() {
        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        try (PdfReport pdf = new PdfReport(pdfOutputStream)) {
            fillDoc(pdf,"");
            pdf.createToc();
        } catch (Exception ex) {
            LOG.error( "makePDF",ex );
            LOG.debug(
                StackTraceFilter.filter(
                    ex.getStackTrace() ).stream()
                    .map( e->e.toString() )
                    .collect( Collectors.joining(",")
                )
            );
        }
        return pdfOutputStream;
    }

}