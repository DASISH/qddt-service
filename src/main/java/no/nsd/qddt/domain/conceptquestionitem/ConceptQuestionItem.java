package no.nsd.qddt.domain.conceptquestionitem;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.utils.JsonDateSerializer;
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
        @AssociationOverride(name = "id.concept", joinColumns = @JoinColumn(name = "CONCEPT_ID")),
        @AssociationOverride(name = "id.questionItem", joinColumns = @JoinColumn(name = "QUESTIONITEM_ID"))
})
public class ConceptQuestionItem  implements java.io.Serializable {

    @EmbeddedId
    private ConceptQuestionItemId id = new ConceptQuestionItemId();

    @JsonBackReference(value = "ConceptQuestionItemConceptRef")
    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = "CONCEPT_ID",insertable = false, updatable = false)
    private Concept concept;

    /*
    This is the reference to the current QuestionItem, it has to be here in order for the framework
    to map the reference from and to Concept/ConceptQuestionItem/QuestionItem
     */
    @JsonIgnore
    @JsonBackReference(value = "ConceptQuestionItemQuestionItemRef")
    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = "QUESTIONITEM_ID",insertable = false, updatable = false)
    private QuestionItem questionItemLateBound;


    /*
    This is the versioned QuestionItem, it has to be filled in manually, by the fetching service
     */
    @Transient
    @JsonSerialize
    @JsonDeserialize
    private QuestionItem questionItem;


    @Column(name = "QUESTIONITEM_REVISION")
    private Integer questionItemRevision;

    @Version
    @Column(name = "updated")
//    @JsonSerialize(using = JsonDateSerializer.class)
    private Timestamp updated;

    public ConceptQuestionItem() {
    }

    public ConceptQuestionItem(Concept concept, QuestionItem questionItem) {
        setConcept(concept);
        setQuestionItemLateBound(questionItem);
        if (concept.getConceptQuestionItems().stream().noneMatch(c->c.getId().getQuestionItemId().equals(questionItem.getId()))){
            concept.getConceptQuestionItems().add(this);
        }
        System.out.println("Created " + this);
    }

    public ConceptQuestionItem(Concept concept, QuestionItem questionItem, Integer questionItemRevision) {
        this(concept,questionItem);
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
        this.concept = concept;
        this.id.setConceptId(concept.getId());
    }


    public QuestionItem getQuestionItemLateBound() {
        return questionItemLateBound;
    }

    public void setQuestionItemLateBound(QuestionItem questionItemLateBound) {
        this.questionItemLateBound = questionItemLateBound;
        this.getId().setQuestionItemId(questionItemLateBound.getId());
    }

    public QuestionItem getQuestionItem() {
        return questionItem;
    }

    public void setQuestionItem(QuestionItem questionItem) {
        this.questionItem = questionItem;
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

//    private void setUpdated(Timestamp updated) {
//        this.updated = updated;
//    }


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
