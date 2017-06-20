package no.nsd.qddt.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import no.nsd.qddt.domain.agency.Agency;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.embedded.Version;
import no.nsd.qddt.domain.user.User;
import no.nsd.qddt.utils.SecurityContext;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.domain.Example;

import javax.persistence.*;
import java.io.*;
import java.util.Arrays;
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
        UPDATED_HIERARCY_RELATION("Hierarcy Relation Updated","Element added to a collection, no changes to element itself"),
        IN_DEVELOPMENT("In Development","UnfinishedWork"),
        TYPO("NoMeaningChange","Typo or No Meaning Change"),
        CONCEPTUAL("ConceptualImprovement","Conceptual Improvement"),
        EXTERNAL("RealLifeChange","Real Life Change"),
        OTHER("OtherPurpose","Other Purpose"),
        //. when you discover that you didn't completely fill inn the fields when creating an element, and then add this information later on.
        ADDED_CONTENT("AddContentElement","Add Content Element"),
        /* deprecated */
//        milestone status, this version is published.
//        This was removed as publication is no longer part of the model, now uses list of published elements for each publication.
        MILESTONE("Deprecated",""),
        BASED_ON("Based on","Based on copy"),
        NEW_COPY("New Copy","Copy new"),
        REFERENCED("Reference of","Concepts can be copied as a reference, to facilitate hierarchical revision trees"),
        TRANSLATED("Translated","Translation of source");

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


    @Where(clause = "is_hidden = 'false'")
    @OneToMany(mappedBy="ownerId", fetch = FetchType.EAGER)
    @NotAudited
//    @Transient
//    @JsonSerialize
//    @JsonDeserialize
    private Set<Comment> comments = new HashSet<>();


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
        this.changeKind = changeKind;
    }

    public String getChangeComment() {
        return changeComment;
    }

    public void setChangeComment(String changeComment) {
        this.changeComment = changeComment;
    }

    public Set<Comment> getComments() {
        return this.comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }


    @PrePersist
    private void onInsert(){
        System.out.println("PrePersist");
        User user = SecurityContext.getUserDetails().getUser();
        agency = user.getAgency();
        changeKind = AbstractEntityAudit.ChangeKind.CREATED;
        version = new Version(true);
    }

    @PreUpdate
    private void onUpdate(){
        try {
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
                    ver.setVersionLabel("");
                    break;
                case TYPO:
                    ver.incMinor();
                    ver.setVersionLabel("");
                    break;
                case CREATED:
                    break;
                default:        // UPDATED_PARENT / UPDATED_CHILD / UPDATED_HIERARCY_RELATION / IN_DEVELOPMENT
                    ver.setVersionLabel(AbstractEntityAudit.ChangeKind.IN_DEVELOPMENT.getName());
                    break;
            }
            version = ver;
        }catch (Exception ex){
            System.out.println("Exception in AbstracEntityAudit::onUpdate");
            System.out.println(ex.getStackTrace()[0]);
            System.out.println(ex.getMessage());
            System.out.println(this);
        }
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


    public abstract void fillDoc(Document document) throws IOException;

    public ByteArrayOutputStream makePdf() {

        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        Document doc = new Document(
                        new PdfDocument(
                            new PdfWriter( pdfOutputStream)));
        try {
            fillDoc(doc);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.out.println(ex);
            ex.printStackTrace();
        }
        finally {
            doc.close();
        }

        return pdfOutputStream;
    }

}