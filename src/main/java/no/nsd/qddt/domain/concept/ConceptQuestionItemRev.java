
package no.nsd.qddt.domain.concept;

import java.lang.reflect.Field;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import no.nsd.qddt.domain.questionItem.QuestionItem;


/**
 * @author Stig Norland
 */
@Audited
@Embeddable
public class ConceptQuestionItemRev  {

    @Column(name="QUESTIONITEM_ID")
    private UUID questionId;

    @Column(name = "QUESTIONITEM_REVISION")
    private Long questionItemRevision;

    @Transient
    @JsonSerialize
    private QuestionItem questionItem;


    public ConceptQuestionItemRev() {
    }

    public ConceptQuestionItemRev(UUID id,Long rev) {
        setQuestionId(id);
        setQuestionItemRevision(rev);
    }

    public UUID getQuestionId() {
        return questionId;
    }

    public void setQuestionId( UUID id) {
        questionId = id;
    }

    public Long getQuestionItemRevision() {
        if (questionItemRevision==null)
            questionItemRevision=0L;
        return questionItemRevision;
    }

    public void setQuestionItemRevision(Long questionItemRevision) {
        this.questionItemRevision = questionItemRevision;
        if  (getQuestionItem() != null)
            getQuestionItem().getVersion().setRevision(questionItemRevision);
    }

    public QuestionItem getQuestionItem() {
        return questionItem;
    }

    public void setQuestionItem(QuestionItem questionItem) {
        this.questionItem = questionItem;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConceptQuestionItemRev)) return false;

        ConceptQuestionItemRev that = (ConceptQuestionItemRev) o;

/*         if (questionItemLateBound != null ? !questionItemLateBound.equals(that.questionItemLateBound) : that.questionItemLateBound != null)
            return false;
 */        return (questionItemRevision != null ? questionItemRevision.equals(that.questionItemRevision) : that.questionItemRevision != null);
        
    }

    @Override
    public int hashCode() {
        return questionId != null ? questionId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "{\"_class\":\"ConceptQuestionItem\", " +
                "\"questionItemRevision\":" + (questionItemRevision == null ? "null" : "\"" + questionItemRevision + "\"") + 
                "}";
    }
   
}