package no.nsd.qddt.domain.topicgroup;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.itextpdf.layout.element.Paragraph;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.IArchived;
import no.nsd.qddt.domain.author.Author;
import no.nsd.qddt.domain.author.IAuthor;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.elementref.ElementKind;
import no.nsd.qddt.domain.elementref.ElementRef;
import no.nsd.qddt.domain.elementref.typed.ElementRefTyped;
import no.nsd.qddt.domain.othermaterial.OtherMaterialT;
import no.nsd.qddt.domain.pdf.PdfReport;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.study.Study;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.io.IOException;
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
    private String abstractDescription;

//    @JsonIgnore
    @JsonBackReference(value = "studyRef")
    @ManyToOne()
    @JoinColumn(name="study_id",updatable = false)
    private Study study;

    @Column(name = "study_id", insertable = false, updatable = false)
    private UUID studyId;

    @JsonIgnore
    @OrderBy(value = "name asc")
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "topicGroup", cascade = { CascadeType.MERGE, CascadeType.REMOVE })
    private Set<Concept> concepts = new HashSet<>(0);


    @OrderColumn(name="element_idx")
    @OrderBy("element_idx ASC")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "TOPIC_GROUP_QUESTION_ITEM",joinColumns = @JoinColumn(name="element_id"))
    private List<ElementRef>  topicQuestionItems = new ArrayList<>();



    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH})
    @JoinTable(name = "TOPIC_GROUP_AUTHORS",
            joinColumns = {@JoinColumn(name ="topic_id")},
            inverseJoinColumns = {@JoinColumn(name = "author_id")})
    private Set<Author> authors = new HashSet<>();


    @OneToMany(mappedBy = "parent" ,fetch = FetchType.EAGER, cascade =CascadeType.REMOVE)
//    @Audited(targetAuditMode = RelationTargetAuditMode.AUDITED)
    private Set<OtherMaterialT> otherMaterials = new HashSet<>();

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
        concept.setTopicGroup(this);
        setChangeKind(ChangeKind.UPDATED_HIERARCHY_RELATION);
        setChangeComment("Concept ["+ concept.getName() +"] added");
        return concept;
    }

    public Set<Concept> getConcepts() {
        return concepts;
    }
    public void setConcepts(Set<Concept> concepts) {
        this.concepts = concepts;
    }


    public Set<OtherMaterialT> getOtherMaterials() {
        try {
            return otherMaterials;
        } catch (Exception ex ){
            LOG.error( ex.getMessage() );
            return null;
        }
    }

    public void setOtherMaterials(Set<OtherMaterialT> otherMaterials) {
        this.otherMaterials = otherMaterials;
    }
    public OtherMaterialT addOtherMaterial(OtherMaterialT otherMaterial) {
        otherMaterial.setParent( this );
        otherMaterial.setParent(this);
        return  otherMaterial;
    }

    public String getAbstractDescription() {
        return abstractDescription;
    }
    public void setAbstractDescription(String abstractDescription) {
        this.abstractDescription = abstractDescription;
    }

    @JsonIgnore
    public List<ElementRefTyped<QuestionItem>> getTopicQuestionItemsT() {
        return topicQuestionItems.stream()
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
//            this.getParents().forEach( p -> p.setChangeKind( ChangeKind.UPDATED_CHILD ) );
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
//            this.getParents().forEach(p->p.setChangeKind(ChangeKind.UPDATED_CHILD));
        }
        else
            LOG.debug("ConceptQuestionItem not inserted, match found" );
    }

    public void setParent(Study newParent) {
        setField( "study", newParent );
    }

    public void setParentU(UUID studyId) {
        setField("topicGroupId",studyId );
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
        return abstractDescription != null ? abstractDescription.equals(that.abstractDescription) : that.abstractDescription == null;

    }


    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (abstractDescription != null ? abstractDescription.hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return "{\"_class\":\"TopicGroup\", " +
                super.toString() +
                "\"abstractDescription\":" + (abstractDescription == null ? "null" : "\"" + abstractDescription + "\"") +
                "\"concepts\":" + (getConcepts() == null ? "null" : Arrays.toString(getConcepts().toArray())) + ", " +
                "\"authors\":" + (authors == null ? "null" : Arrays.toString(authors.toArray())) + ", " +
//                "\"otherMaterials\":" + (otherMaterials == null ? "null" : Arrays.toString(otherMaterials.toArray())) + ", " +
                "}";
    }


    @Override
    public void fillDoc(PdfReport pdfReport, String counter) throws IOException {

        pdfReport.addHeader(this,"Module " + counter )
        .add(new Paragraph(this.getAbstractDescription())
            .setWidth(pdfReport.width100*0.8F)
            .setPaddingBottom(15));

        if(getComments().size()>0) {
            pdfReport.addheader2("Comments");
            pdfReport.addComments(getComments());
            pdfReport.addPadding();
        }

        if (getTopicQuestionItems().size() > 0) {
            pdfReport.addheader2("QuestionItem(s)");
            for (ElementRefTyped<QuestionItem> item : getTopicQuestionItemsT()) {
                pdfReport.addheader2(item.getName(), String.format("Version %s",item.getVersion()));
                pdfReport.addParagraph(item.getElementAs().getQuestion());
                if (item.getElementAs().getResponseDomain() != null)
                    item.getElementAs().getResponseDomain().fillDoc(pdfReport, "");
                pdfReport.addPadding();
            }
        }

        if (counter.length()>0)
            counter = counter+".";
        int i = 0;
        for (Concept concept : getConcepts()) {
            concept.fillDoc(pdfReport ,counter+ String.valueOf(++i));
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