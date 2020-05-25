package no.nsd.qddt.domain.topicgroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.hibernate.envers.AuditMappedBy;
import org.hibernate.envers.Audited;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.IArchived;
import no.nsd.qddt.domain.author.Author;
import no.nsd.qddt.domain.author.IAuthor;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.elementref.ElementKind;
import no.nsd.qddt.domain.elementref.ElementRef;
import no.nsd.qddt.domain.othermaterial.OtherMaterial;
import no.nsd.qddt.domain.parentref.IParentRef;
import no.nsd.qddt.domain.parentref.IRefs;
import no.nsd.qddt.domain.parentref.Leaf;
import no.nsd.qddt.domain.pdf.PdfReport;
import no.nsd.qddt.domain.questionitem.QuestionItem;
import no.nsd.qddt.domain.study.Study;
import no.nsd.qddt.domain.xml.AbstractXmlBuilder;

/**
 *
 * <ul class="inheritance">
 *     <li>A Topic Group (Module) will have one or more Concepts.
 *         <ul class="inheritance">
 *             <li>A Concept consist of one or more QuestionItems.
 *                 <ul class="inheritance">
 *                     <li>Every QuestionItem will have a Question.</li>
 *                 </ul>
 *                 <ul class="inheritance">
 *                     <li>Every QuestionItem will have a ResponseDomain.</li>
 *                 </ul>
 *             </li>
 *          </ul>
 *      </li>
 * </ul>
 * <br>
 * A Topic Group (Module) should be a collection of QuestionItems and Concepts that has a theme that is broader than a Concept.
 * All QuestionItems that doesn't belong to a specific Concept, will be collected in a default Concept that
 * every Module should have. This default Concept should not be visualized as a Concept, but as a
 * "Virtual Topic Group (Module)". The reason for this is a simplified data model.
 *
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */

@Audited
@Entity
@Table(name = "TOPIC_GROUP")
public class TopicGroup extends AbstractEntityAudit implements IAuthor, IArchived, IParentRef {

    @Column(name = "description", length = 10000)
    private String description;

    @ManyToOne()
    @JsonBackReference(value = "studyRef")
    @JoinColumn(name="study_id",updatable = false)
    private Study study;

    @Column(name = "study_id", insertable = false, updatable = false)
    protected UUID studyId;

    @Column(name = "study_idx", insertable = false, updatable = false)
    private Integer studyIdx;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "topicGroup", cascade = {CascadeType.REMOVE,CascadeType.PERSIST})
    @OrderColumn(name="concept_idx")
    @AuditMappedBy(mappedBy = "topicGroup", positionMappedBy = "conceptIdx")
    private List<Concept> concepts = new ArrayList<>(0);


    @OrderColumn(name="topicgroup_idx")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "TOPIC_GROUP_QUESTION_ITEM",joinColumns = @JoinColumn(name="topicgroup_id", referencedColumnName = "id"))
    private List<ElementRef<QuestionItem>>  topicQuestionItems = new ArrayList<>();


    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH})
    @JoinTable(name = "TOPIC_GROUP_AUTHORS",
            joinColumns = {@JoinColumn(name ="topicgroup_id")},
            inverseJoinColumns = {@JoinColumn(name = "author_id")})
    private Set<Author> authors = new HashSet<>();

    @OrderColumn(name="owner_idx")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "TOPIC_GROUP_OTHER_MATERIAL",
        joinColumns = {@JoinColumn(name = "owner_id", referencedColumnName = "id")})
        private List<OtherMaterial> otherMaterials = new ArrayList<>();

    @Transient
    private Leaf<Study> parentRef;


    private boolean isArchived;

    public TopicGroup() {
        super();
    }

    @Override
    public void addAuthor(Author user) {
        authors.add(user);
    }

    @Override
    public Set<Author> getAuthors() {
        return this.authors;
    }

    @Override
    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }


    public Study getStudy() {
        return study;
    }

    public void setStudy(Study study) {
        this.study = study;
    }


    public Concept addConcept(Concept concept){
        if(concept == null) return null;
        getConcepts().add(concept);
        concept.setTopicGroup(this);
        setChangeKind(ChangeKind.UPDATED_HIERARCHY_RELATION);
        setChangeComment("Concept ["+ concept.getName() +"] added");
        return concept;
    }

    public List<Concept> getConcepts() {
        return concepts.stream()
            .filter( Objects::nonNull )
            .collect( Collectors.toList());
    }

    public void setConcepts(List<Concept> concepts) {
        this.concepts = concepts;
    }

    public List<OtherMaterial> getOtherMaterials() {
        try {
            return otherMaterials;
        } catch (Exception ex ){
            LOG.error( ex.getMessage() );
            return null;
        }
    }

    public void setOtherMaterials(List<OtherMaterial> otherMaterials) {
        this.otherMaterials = otherMaterials;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }


    public List<ElementRef<QuestionItem>> getTopicQuestionItems() {
        return topicQuestionItems;
    }

    public void setTopicQuestionItems(List<ElementRef<QuestionItem>> topicQuestionItems) {
        this.topicQuestionItems = topicQuestionItems;
    }

    // no update for QI when removing (it is bound to a revision anyway...).
    public void removeQuestionItem(UUID questionItemId, Integer rev) {
        ElementRef toDelete = new ElementRef( ElementKind.QUESTION_ITEM, questionItemId,rev );
        if (topicQuestionItems.removeIf( q -> q.equals( toDelete ) )) {
            this.setChangeKind( ChangeKind.UPDATED_HIERARCHY_RELATION );
            this.setChangeComment( "QuestionItem assosiation removed" );
        }
    }

    public void addQuestionItem(UUID questionItemId, Integer rev) {
        addQuestionItem( new ElementRef( ElementKind.QUESTION_ITEM, questionItemId,rev ) );
    }

    public void addQuestionItem(ElementRef qef) {
        if (this.topicQuestionItems.stream().noneMatch(cqi->cqi.equals( qef ))) {

            topicQuestionItems.add(qef);
            this.setChangeKind(ChangeKind.UPDATED_HIERARCHY_RELATION);
            this.setChangeComment("QuestionItem assosiation added");
        }
        else
            LOG.debug("ConceptQuestionItem not inserted, match found" );
    }

    @Override
    public IRefs getParentRef() {
        if (parentRef == null )
            parentRef = new Leaf<Study>(getStudy());
        return parentRef;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TopicGroup)) return false;
        if (!super.equals(o)) return false;

        TopicGroup that = (TopicGroup) o;

        if (!Objects.equals( study, that.study )) return false;
        if (!Objects.equals( authors, that.authors )) return false;
        if (!Objects.equals( otherMaterials, that.otherMaterials ))
            return false;
        return Objects.equals( description, that.description );

    }


    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return "{\"_class\":\"TopicGroup\", " +
                super.toString() +
                "\"description\":" + (description == null ? "null" : "\"" + description + "\"") +
                "\"concepts\":" + (getConcepts() == null ? "null" : Arrays.toString(getConcepts().toArray())) + ", " +
                "\"authors\":" + (authors == null ? "null" : Arrays.toString(authors.toArray())) + ", " +
                "}";
    }

    @Override
    public AbstractXmlBuilder getXmlBuilder() {
        return new TopicGroupFragmentBuilder(this);
    }


    @Override
    public void fillDoc(PdfReport pdfReport, String counter) {

        pdfReport.addHeader(this,"Module " + counter );
        pdfReport.addParagraph( this.description );

        if(getComments().size()>0) {
            pdfReport.addheader2("Comments");
            pdfReport.addComments(getComments());
        }

        if (getTopicQuestionItems().size() > 0) {
            pdfReport.addheader2("QuestionItem(s)");
            for (ElementRef<QuestionItem> item : getTopicQuestionItems()) {
                pdfReport.addheader2(item.getElement().getName(), String.format("Version %s",item.getElement().getVersion()));
                pdfReport.addParagraph(item.getElement().getQuestion());
                if (item.getElement().getResponseDomainRef().getElement() != null)
                    item.getElement().getResponseDomainRef().getElement().fillDoc(pdfReport, "");
            }
        }
        pdfReport.addPadding();

        if (counter.length()>0)
            counter = counter+".";
        int i = 0;
        for (Concept concept : getConcepts()) {
            concept.fillDoc(pdfReport ,counter+ ++i );
        }
    }

    @PreRemove
    public void preRemove(){
        LOG.debug("Topic pre remove");
        getAuthors().clear();
        getOtherMaterials().clear();
    }

    @Override
    public boolean isArchived() {
        return isArchived;
    }

    @Override
    public void setArchived(boolean archived) {
        isArchived = archived;

        if (archived) {
            LOG.info( getName() + " isArchived(" + getConcepts().size() +")" );
            setChangeKind(ChangeKind.ARCHIVED);
            for (Concept concept : getConcepts()) {
                if (!concept.isArchived())
                    concept.setArchived(archived);
            }
        }
    }

    @Override
    protected void beforeUpdate() {
    }
    @Override
    protected void beforeInsert() {}

}
