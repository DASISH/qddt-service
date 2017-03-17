package no.nsd.qddt.domain.conceptquestionitem;

import com.fasterxml.jackson.annotation.JsonBackReference;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.UUID;

/**
 * @author Stig Norland
 */

@Table(name = "CONCEPT_QUESTION_ITEM",
        uniqueConstraints= @UniqueConstraint(columnNames = {"concept_id", "questionitem_id"}))
@Audited
@Entity
public class ConceptQuestionItem  implements java.io.Serializable {


    @Id
    @Type(type="pg-uuid")
    @Column(name = "id")
    @GenericGenerator(name = "uuid-gen", strategy = "uuid2")
    @GeneratedValue(generator = "uuid-gen")
    private UUID id;

    @JsonBackReference(value="conceptQuestionItemsRef")
    @ManyToOne()
    @JoinColumn(name = "concept_id")
    private Concept concept;

    @JsonBackReference(value="questionItemConceptsRef")
    @ManyToOne()
    @JoinColumn(name = "questionitem_id",referencedColumnName = "id")
    private QuestionItem questionItem;

    @Column(name = "questionitem_revision")
    private Integer questionItemRevision;


    public ConceptQuestionItem() {
    }

    public ConceptQuestionItem(Concept concept, QuestionItem questionItem) {
        System.out.println("ConceptQuestionItem created");
        setQuestionItem(questionItem);
        setConcept(concept);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Concept getConcept() {
        return concept;
    }

    public void setConcept(Concept concept) {
        this.concept = concept;
    }

    public QuestionItem getQuestionItem() {
        if (questionItemRevision != questionItem.getVersion().getRevision() )
            questionItem.getVersion().setRevision(questionItemRevision);

        return questionItem;
    }

    public void setQuestionItem(QuestionItem questionItem) {
        if (questionItem.getVersion().getRevision() != null) {
            setQuestionItemRevision(questionItem.getVersion().getRevision());
        }
        this.questionItem = questionItem;
    }


    public Integer getQuestionItemRevision() {
        return questionItemRevision;
    }

    public void setQuestionItemRevision(Integer questionItemRevision) {
        this.questionItemRevision = questionItemRevision;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConceptQuestionItem)) return false;

        ConceptQuestionItem that = (ConceptQuestionItem) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (concept != null ? !concept.equals(that.concept) : that.concept != null) return false;
        if (questionItem != null ? !questionItem.equals(that.questionItem) : that.questionItem != null) return false;
        return questionItemRevision != null ? questionItemRevision.equals(that.questionItemRevision) : that.questionItemRevision == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (concept != null ? concept.hashCode() : 0);
        result = 31 * result + (questionItem != null ? questionItem.hashCode() : 0);
        result = 31 * result + (questionItemRevision != null ? questionItemRevision.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ConceptQuestionItem{" +
                "id=" + id +
                ", concept=" + concept +
                ", questionItem=" + questionItem +
                ", questionItemRevision=" + questionItemRevision +
                '}';
    }


    public void makeNewCopy(Integer revision) {
     //TODO implement
    }
}
