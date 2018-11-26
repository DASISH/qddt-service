package no.nsd.qddt.domain.topicgroup;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.IArchived;
import no.nsd.qddt.domain.author.Author;
import no.nsd.qddt.domain.author.IAuthor;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.elementref.ElementKind;
import no.nsd.qddt.domain.elementref.ElementRef;
import no.nsd.qddt.domain.elementref.typed.ElementRefTyped;
import no.nsd.qddt.domain.othermaterial.OtherMaterial;
import no.nsd.qddt.domain.pdf.PdfReport;
import no.nsd.qddt.domain.questionitem.QuestionItem;
import no.nsd.qddt.domain.refclasses.StudyRef;
import no.nsd.qddt.domain.study.Study;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

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
public class TopicGroup extends AbstractEntityAudit implements IAuthor,IArchived {

    @Column(name = "description", length = 10000)
    private String description;

//    @JsonIgnore
    @JsonBackReference(value = "studyRef")
    @ManyToOne()
    @JoinColumn(name="study_id",updatable = false)
    private Study study;

    @Column(name = "study_id", insertable = false, updatable = false)
    private UUID studyId;

    @Column(name = "study_idx" ,insertable = false, updatable = false)
    private Integer index;


    @JsonIgnore
    @OrderColumn(name="_idx")
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "topicGroup", cascade = { CascadeType.MERGE, CascadeType.REMOVE })   // TODO check performance and consequences
//    @AuditMappedBy(mappedBy = "id", positionMappedBy = "index")
    private List<Concept> concepts = new ArrayList<>(0);


    @OrderColumn(name="topicgroup_idx")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "TOPIC_GROUP_QUESTION_ITEM",joinColumns = @JoinColumn(name="topicgroup_id", referencedColumnName = "id"))
    private List<ElementRef>  topicQuestionItems = new ArrayList<>(0);


    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH})
    @JoinTable(name = "TOPIC_GROUP_AUTHORS",
            joinColumns = {@JoinColumn(name ="topicgroup_id")},
            inverseJoinColumns = {@JoinColumn(name = "author_id")})
    private Set<Author> authors = new HashSet<>();

    @OrderColumn(name="owner_idx")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "TOPIC_GROUP_OTHER_MATERIAL",
        joinColumns = {@JoinColumn(name = "owner_id", referencedColumnName = "id")})
    private List<OtherMaterial> otherMaterials = new ArrayList<>(0);


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

    public Integer getIndex() {
        return index;
    }


    public Concept addConcept(Integer index, Concept concept){
        this.setChangeKind(ChangeKind.UPDATED_HIERARCHY_RELATION);
        setChangeComment("Concept added");
        this.concepts.add((index!=null)? index : this.concepts.size() , concept);
        return concept;
    }

    public List<Concept> getConcepts() {
        return concepts;
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


    @JsonIgnore
    protected List<ElementRefTyped<QuestionItem>> getTopicQuestionItemsT() {
        return topicQuestionItems.stream()
            .filter( f -> f!=null)
            .map(c-> new ElementRefTyped<QuestionItem>(c) )
            .collect( Collectors.toList() );
    }

    public List<ElementRef> getTopicQuestionItems() {
        return topicQuestionItems;
    }

    public void setTopicQuestionItems(List<ElementRef> conceptQuestionItems) {
        this.topicQuestionItems = conceptQuestionItems;
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

    public void setParent(Study newParent) {
        setField( "study", newParent );
    }


    public StudyRef getStudyRef() {
        return new StudyRef(getStudy());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TopicGroup)) return false;
        if (!super.equals(o)) return false;

        TopicGroup that = (TopicGroup) o;

        if (study != null ? !study.equals(that.study) : that.study != null) return false;
        if (authors != null ? !authors.equals(that.authors) : that.authors != null) return false;
        if (otherMaterials != null ? !otherMaterials.equals(that.otherMaterials) : that.otherMaterials != null)
            return false;
        return description != null ? description.equals(that.description) : that.description == null;

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
//                "\"otherMaterials\":" + (otherMaterials == null ? "null" : Arrays.toString(otherMaterials.toArray())) + ", " +
                "}";
    }


    @Override
    public void fillDoc(PdfReport pdfReport, String counter) {

        pdfReport.addHeader(this,"Module " + counter );
        pdfReport.addParagraph( this.description );

        if(getComments().size()>0) {
            pdfReport.addheader2("Comments");
            pdfReport.addComments(getComments());
//            // pdfReport.addPadding();
        }

        if (getTopicQuestionItems().size() > 0) {
            pdfReport.addheader2("QuestionItem(s)");
            for (ElementRefTyped<QuestionItem> item : getTopicQuestionItemsT()) {
                pdfReport.addheader2(item.getElement().getName(), String.format("Version %s",item.getElement().getVersion()));
                pdfReport.addParagraph(item.getElement().getQuestion());
                if (item.getElement().getResponseDomain() != null)
                    item.getElement().getResponseDomain().fillDoc(pdfReport, "");
//                // pdfReport.addPadding();
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
//            Hibernate.initialize(this.getConcepts());
            for (Concept concept : getConcepts()) {
                if (!concept.isArchived())
                    concept.setArchived(archived);
            }
        }
    }

    @Override
    protected void beforeUpdate() {}
    @Override
    protected void beforeInsert() {}

}