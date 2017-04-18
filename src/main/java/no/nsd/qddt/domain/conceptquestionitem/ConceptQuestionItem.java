package no.nsd.qddt.domain.conceptquestionitem;

import com.fasterxml.jackson.annotation.JsonBackReference;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author Stig Norland
 */

@Entity
@Audited
@Table(name = "CONCEPT_QUESTION_ITEM")
@AssociationOverrides({
        @AssociationOverride(name = "id.concept", joinColumns = @JoinColumn(name = "CONCEPT_ID")),  //, foreignKey = @ForeignKey(name="id")
        @AssociationOverride(name = "id.questionItem", joinColumns = @JoinColumn(name = "QUESTIONITEM_ID")) //, foreignKey = @ForeignKey(name="id")
})
public class ConceptQuestionItem  implements java.io.Serializable {

    @EmbeddedId
    private ConceptQuestionItemId id = new ConceptQuestionItemId();

    @JsonBackReference(value = "ConceptQuestionItemConceptRef")
    @ManyToOne(cascade = CascadeType.ALL)
    @MapsId("id")
    @JoinColumn(name = "CONCEPT_ID",insertable = false, updatable = false)
//    @Type(type="pg-uuid")
    private Concept concept;


    @JsonBackReference(value = "ConceptQuestionItemQuestionItemRef")
    @ManyToOne(cascade = CascadeType.ALL)
    @MapsId("id")
    @JoinColumn(name = "QUESTIONITEM_ID",insertable = false, updatable = false)
//    @Type(type="pg-uuid")
    private QuestionItem questionItem;


    @Column(name = "QUESTIONITEM_REVISION")
    private Integer questionItemRevision;

    @Version
    @Column(name = "updated")
    private Timestamp updated;

    public ConceptQuestionItem() {
    }

    public ConceptQuestionItem(Concept concept, QuestionItem questionItem) {

//        System.out.println("ConceptQuestionItem(2) created");
        setConcept(concept);
        setQuestionItem(questionItem);
    }

    public ConceptQuestionItem(Concept concept, QuestionItem questionItem, Integer questionItemRevision) {
//        System.out.println("ConceptQuestionItem(3) created");
        setConcept(concept);
        setQuestionItem(questionItem);
        setQuestionItemRevision(questionItemRevision);
    }


    public ConceptQuestionItemId getId() {
        return id;
    }

    public void setId(ConceptQuestionItemId id) {
        this.id = id;
    }


    public Concept getConcept() {
        return concept;
    }

    public void setConcept(Concept concept) {
        this.id.setConceptId(concept.getId());
//        this.concept = concept;
    }


    public QuestionItem getQuestionItem() {
        return questionItem;
    }

    public void setQuestionItem(QuestionItem questionItem) {
        this.id.setQuestionItemId(questionItem.getId());
        if (getQuestionItemRevision() != questionItem.getVersion().getRevision() &  questionItem.getVersion().getRevision() != null )
            setQuestionItemRevision(questionItem.getVersion().getRevision());
    }


    public Integer getQuestionItemRevision() {
        return questionItemRevision;
    }

    public void setQuestionItemRevision(Integer questionItemRevision) {
        this.questionItemRevision = questionItemRevision;
        if  (getQuestionItem() != null)
            getQuestionItem().getVersion().setRevision(questionItemRevision);
    }


    public Timestamp getUpdated() {
        return updated;
    }

    private void setUpdated(Timestamp updated) {
        this.updated = updated;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConceptQuestionItem)) return false;

        ConceptQuestionItem that = (ConceptQuestionItem) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ConceptQuestionItem{" +
                "pk=" + id +
                '}';
    }

    public void makeNewCopy(Integer revision) {
     //TODO implement
    }


}
