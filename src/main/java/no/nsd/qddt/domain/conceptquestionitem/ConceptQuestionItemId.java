package no.nsd.qddt.domain.conceptquestionitem;

import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Embeddable
public class ConceptQuestionItemId implements Serializable {

    private static final long serialVersionUID = -7261887879839337877L;

    @Type(type="pg-uuid")
    @Column(name = "QUESTIONITEM_ID")
    private UUID questionItemId;

    @Type(type="pg-uuid")
    @Column(name = "CONCEPT_ID")
    private UUID conceptId;


    public ConceptQuestionItemId() {
    }

    public ConceptQuestionItemId(Concept concept, QuestionItem questionItem) {
        this.setQuestionItemId(questionItem.getId());
        this.setConceptId(concept.getId());
    }

    public ConceptQuestionItemId(UUID conceptId,UUID questionItemId) {
        this.questionItemId = questionItemId;
        this.conceptId = conceptId;
    }

    public UUID getQuestionItemId() {
        return questionItemId;
    }

    public void setQuestionItemId(UUID questionItemId) {
        this.questionItemId = questionItemId;
    }

    public UUID getConceptId() {
        return conceptId;
    }

    public void setConceptId(UUID conceptId) {
        this.conceptId = conceptId;
    }

    @Override
    public String toString() {
        return "Id{" +
                " QuestionId=" + questionItemId +
                ", conceptId=" + conceptId +
                '}';
    }
}
