package no.nsd.qddt.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import no.nsd.qddt.domain.agency.Agency;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.comment.CommentJsonEdit;
import no.nsd.qddt.domain.elementref.ElementKind;
import no.nsd.qddt.domain.interfaces.IArchived;
import no.nsd.qddt.domain.interfaces.IDomainObject;
import no.nsd.qddt.domain.interfaces.Version;
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
public abstract class AbstractEntityAudit extends AbstractEntity  implements IDomainObject {

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
        ARCHIVED("Archived","READ ONLY"),
        TO_BE_DELETED("ToBeDeleted","This has been marked for deletion, but we need to see it a tiny bit longer.");

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


    @ManyToOne
    @JoinColumn(name = "agency_id",updatable = false, nullable = false)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Agency agency;

    // @JsonView(View.Simple.class)
    @Column(name = "name")
    private String name;


    // @JsonView(View.Edit.class)
    @Column(name = "based_on_object",updatable = false)
    @Type(type="pg-uuid")
    private UUID basedOnObject;

    // @JsonView(View.Edit.class)
    @Column(name = "based_on_revision",updatable = false)
    private Integer basedOnRevision;

    @Embedded
    private no.nsd.qddt.domain.interfaces.Version version;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChangeKind changeKind;

    @Column(name = "change_comment",nullable = false)
    private String changeComment;

    @Column(name = "xml_lang",nullable = false)
    private String xmlLang;

    @NotAudited
    @OrderColumn(name="owner_idx")
    @OneToMany(mappedBy="ownerId", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER,orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();


    @Transient
    @JsonSerialize
    @JsonDeserialize
    private String classKind;


    protected AbstractEntityAudit() {
        try {
            setChangeKind( ChangeKind.CREATED);
            setChangeComment( ChangeKind.CREATED.getDescription() );
            setClassKind( ElementKind.getEnum( this.getClass().getSimpleName() ).toString());
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

    public no.nsd.qddt.domain.interfaces.Version getVersion() {
        if (version == null)
            version = new Version(true);
        return version;
    }

    public void setVersion(no.nsd.qddt.domain.interfaces.Version version) {
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
//        LOG.info("AstractEntityAudit PrePersist " + this.getClass().getSimpleName());
        User user = SecurityContext.getUserDetails().getUser();
        setAgency( user.getAgency() );
        setModifiedBy( user );
        if (StringTool.IsNullOrEmpty( xmlLang))
            setXmlLang( user.getAgency().getDefaultXmlLang() );
        // if empty, we need to apply a default (CREATED),
        // if an existing entity tries to create itself (except for BASEDON), we need to set changeKind to CREATED
        if (changeKind == null) {
            LOG.info("AstractEntityAudit PrePersist - changeKind = ChangeKind.CREATED");
            setChangeKind( ChangeKind.CREATED);
            setChangeComment( ChangeKind.CREATED.description);
        }
        version = new no.nsd.qddt.domain.interfaces.Version(true);
        beforeInsert();
    }

    @PreUpdate
    private void onUpdate(){
        try {
            no.nsd.qddt.domain.interfaces.Version ver = version;
            ChangeKind change = changeKind;
            User user = SecurityContext.getUserDetails().getUser();
            setModifiedBy( user );

            // it is illegal to update an entity with "Creator statuses" (CREATED...BASEDON)
            if (change.ordinal() <= ChangeKind.REFERENCED.ordinal() &    !ver.isNew()) {
                change = ChangeKind.IN_DEVELOPMENT;
                setChangeKind( change );
            }
            if (StringTool.IsNullOrTrimEmpty(changeComment))        // insert default comment if none was supplied, (can occur with auto touching (hierarchy updates etc))
                setChangeComment( change.description );
            switch (change) {
                case CREATED:
                    if (getChangeComment() == null)
                        setChangeComment( change.getDescription() );
                    break;
                case BASED_ON:
                case NEW_COPY:
                case TRANSLATED:
                    ver = new no.nsd.qddt.domain.interfaces.Version();
                    break;
                case REFERENCED:
                    break;
                case UPDATED_PARENT:
                case UPDATED_CHILD:
                case UPDATED_HIERARCHY_RELATION:
                    ver.setVersionLabel("");
                    break;
                case IN_DEVELOPMENT:
                    ver.setVersionLabel(ChangeKind.IN_DEVELOPMENT.getName());
                    break;
                case TYPO:
                    ver.incMinor();
                    ver.setVersionLabel("");
                    break;
                case CONCEPTUAL:
                case EXTERNAL:
                case OTHER:
                case ADDED_CONTENT:
                    ver.incMajor();
                    ver.setVersionLabel("");
                    break;
                case ARCHIVED:
                    ((IArchived)this).setArchived(true);
                    ver.setVersionLabel("");
                    break;
                case TO_BE_DELETED:
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
        return (getChangeKind() == ChangeKind.BASED_ON
            || getChangeKind() == ChangeKind.NEW_COPY
            || getChangeKind() == ChangeKind.TRANSLATED
            || getChangeKind() == ChangeKind.REFERENCED);
    }

    @JsonIgnore
    public boolean isNewCopy(){
        return (getChangeKind() == ChangeKind.NEW_COPY
                || (getId() == null && getChangeKind() != null && getChangeKind()!= ChangeKind.CREATED)
                );
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
        return "{" +
            "\"id\":" + (getId() == null ? "null" : "\"" + getId() +"\"" ) + ", " +
            "\"name\":" + (name == null ? "null" : "\"" + name + "\"") + ", " +
            "\"classKind\":" + (classKind == null ? "null" : "\"" + classKind + "\"") + ", " +
            "\"modified\":" + (getModified() == null ? "null" : "\"" + getModified()+ "\"" ) + " , " +
            "\"modifiedBy\":" + (getModifiedBy() == null ? "null" : getModifiedBy()) +
            "}";
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
